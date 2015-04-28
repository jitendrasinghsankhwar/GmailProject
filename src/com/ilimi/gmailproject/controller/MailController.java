package com.ilimi.gmailproject.controller;

import java.io.FileNotFoundException;

import com.ilimi.gmailproject.service.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Messages.Get;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.ilimi.gmailproject.DAO.UserDAO;
import com.ilimi.gmailproject.entity.UserEntity;

@Controller
public class MailController {
	@Autowired
	private UserDAO userDao;

	private static final String SCOPE[] = { "https://www.googleapis.com/auth/userinfo.profile",
			"https://www.googleapis.com/auth/gmail.compose", "https://www.googleapis.com/auth/gmail.modify",
			"https://www.googleapis.com/auth/gmail.readonly", "email", "https://www.googleapis.com/auth/plus.login",
			"https://mail.google.com/" };
	private static final String APP_NAME = "Gmail API ";
	// Email address of the user, or "me" can be used to represent the currently authorized user.
	private static final String USER = "me";
	// Path to the client_secret.json file downloaded from the Developer Console.
	private static final String CLIENT_SECRET_PATH = "client_secret.json";

	private static GoogleClientSecrets clientSecrets;
	GoogleAuthorizationCodeFlow flow;
	Gmail service;

	@RequestMapping(value = "/accessToken.action")
	public void getAuthorizationCode(HttpServletResponse resp) {
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		try {
			clientSecrets = GoogleClientSecrets.load(jsonFactory, new FileReader("F:/WorkspaceMaven/GmailProject/client_secret.json"));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Allow user to authorize via url.
		flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, Arrays.asList(SCOPE))
				.setAccessType("online").setApprovalPrompt("auto").build();
		String url = flow.newAuthorizationUrl().setRedirectUri("http://dev.ilimi.in:8080/GmailProject/redirectUri.action").build();

		try {
			System.out.println("url redirect");
			resp.sendRedirect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("url redirect Exception");
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/redirectUri.action")
	public ModelAndView generateAccessToken(@RequestParam("code") String code) throws IOException {

		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		// GoogleTokenResponse response = generateAccessToken(code);
		GoogleTokenResponse response = flow.newTokenRequest(code)
				.setRedirectUri("http://dev.ilimi.in:8080/GmailProject/redirectUri.action").execute();
		GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

		String accessToken = credential.getAccessToken();

		// Create a new authorized Gmail API client
		service = new Gmail.Builder(httpTransport, jsonFactory, credential).setApplicationName(APP_NAME).build();

		Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName(APP_NAME).build();
		Userinfoplus userinfo = oauth2.userinfo().get().execute();
		// Check User Already Registered
		boolean userCheck = userDao.fetchUserData(userinfo.getEmail());
		if (userCheck) {
			ModelAndView model = new ModelAndView("mailBox");
			return new IGmailServiceImpl().receiveMail(service, model);

		} else {
			ModelAndView model = new ModelAndView("userInfo");
			model.addObject("imgSource", userinfo.getPicture());
			model.addObject("email", userinfo.getEmail());
			model.addObject("fullName", userinfo.getName());
			model.addObject("gender", userinfo.getGender());
			model.addObject("accessToken", accessToken);
			System.out.println(userinfo.getGender());
			return model;
		}

		// System.out.println("Email::::" + userinfo.getEmail());

		// // Retrieve a page of Threads; max of 100 by default.
		// ListThreadsResponse threadsResponse = service.users().threads().list(USER).execute();
		// List<Thread> threads = threadsResponse.getThreads();
		// int i = 0;
		// // Print ID of each Thread.
		// for (Thread thread : threads) {
		// System.out.println("Thread ID: " + thread.toPrettyString() + "::" + i++);
		// Message message = thread.getMessages().get(0);
		//
		// }

	}

	@RequestMapping(value = "/saveUserInfo.action", method = RequestMethod.POST)
	public ModelAndView saveUserInformation(@ModelAttribute UserEntity user) {

		userDao.insertUserInfo(user);
		ModelAndView model = new ModelAndView("mailBox");
		return model;

	}

	
	 @RequestMapping(value = "/sendMail.action", method = RequestMethod.POST)
	 public void sendMail() {
		 SendMailTest smt = new SendMailTest();
		 MimeMessage email = null;
		try {
			email = smt.createEmail("jitendras@ilimi.in", "jitendras@ilimi.in", "First Send Mail", "bodyText:::sdkjnldksfjnvdfj");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			smt.sendMessage(service, "me", email);
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }

}
