package com.ilimi.gmailproject.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Messages.Get;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.ilimi.gmailproject.dao.MsgDTO;

public class GmailServiceImpl implements IGmailService {

	@Override
	public List<MsgDTO> getMessage(Thread id) {
		List<MsgDTO> dto = null;

		return dto;
	}

	public ModelAndView receiveMail(Gmail service, ModelAndView model) {
		ListMessagesResponse messageResponse = null;
		try {
			messageResponse = service.users().messages().list("me").execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Message> message = messageResponse.getMessages();

		Get get = null;
		try {
			get = service.users().messages().get("me", message.get(0).getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		get.setMetadataHeaders(Arrays.asList("To", "Subject"));
		Message msg = null;
		try {
			msg = get.setFormat("full").execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MessagePartBody body = msg.getPayload().getBody();
		if(msg.getPayload().getBody().getData() != null){
			System.out.println("Message Body:::" + new String(Base64.decodeBase64(msg.getPayload().getBody().getData().getBytes())));
			model.addObject("Body", new String(Base64.decodeBase64(msg.getPayload().getBody().getData().getBytes())));
		}
		if(msg.getPayload().getBody().getAttachmentId() != null){
			System.out.println("Message Attacement:::::"+ new String(Base64.decodeBase64(msg.getPayload().getBody().getAttachmentId().getBytes())));
			model.addObject("Body", new String(Base64.decodeBase64(msg.getPayload().getBody().getAttachmentId().getBytes())));
		}
				
		model.addObject("Snnippt", msg.getSnippet());
		List<MessagePartHeader> headers = msg.getPayload().getHeaders();
		for (MessagePartHeader mph : headers) {
			// System.out.println("Key:" + mph.getName() + " | value:" + mph.getValue());
			if (mph.getName().equals("From")) {
				model.addObject("From", mph.getValue());
			}
			if (mph.getName().equals("Subject")) {
				model.addObject("Subject", mph.getValue());
			}
			if (mph.getName().equals("Date")) {
				model.addObject("Date", mph.getValue());
			}

		}
		return model;


	}
	
	public  MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);
		InternetAddress tAddress = new InternetAddress(to);
		InternetAddress fAddress = new InternetAddress(from);

		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}

	public  Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		email.writeTo(baos);
		String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;
	}

	public  void sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
		Message message = createMessageWithEmail(email);
		message = service.users().messages().send(userId, message).execute();

		System.out.println("Message id: " + message.getId());
		System.out.println(message.toPrettyString());
	}
	
	
}
