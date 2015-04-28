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

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Messages.Get;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.ilimi.gmailproject.DAO.MsgDTO;

public class IGmailServiceImpl implements IGmailService {

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
			// System.out.println(StringUtils.newStringUtf8(Base64.decodeBase64(msg.getPayload().getParts()[0].body.data)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MessagePartBody body = msg.getPayload().getBody();
		//System.out.println("Message Body:::" + msg.getPayload().getParts().get(0).getBody().getData());
		// msg.getPayload().getParts()[0].body.data;

		//model.addObject("Body", org.apache.commons.codec.binary.Base64.decodeBase64(msg.getPayload().getParts().get(0).getBody().getData().getBytes()));
		

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

		/**
		 * for (Message msg : message) { Message msgData = service.users().messages().get("me", msg.getId()).execute(); //
		 * System.out.println(msgData.getPayload().getBody().getData().toString() + "------" + i++);
		 * System.out.println("----------------------------------------------------------------");
		 * System.out.println(msgData.getPayload().getHeaders().get(0).getName() + "\n");
		 * System.out.println(msgData.getPayload().getHeaders().get(1).getName() + "\n");
		 * System.out.println(msgData.getPayload().getHeaders().get(2).getName() + "\n");
		 * System.out.println(msgData.getPayload().getHeaders().get(3).getName() + "\n");
		 * System.out.println(msgData.getPayload().getHeaders().get(4).getName() + "\n");
		 * System.out.println("----------------------------------------------------------------");
		 * System.out.println(msgData.getPayload().getHeaders().get(0).getValue() + "\n");
		 * System.out.println(msgData.getPayload().getHeaders().get(1).getValue() + "\n");
		 * System.out.println(msgData.getPayload().getHeaders().get(2).getValue() + "\n");
		 * System.out.println(msgData.getPayload().getHeaders().get(3).getValue() + "\n");
		 * System.out.println(msgData.getPayload().getHeaders().get(4).getValue() + "\n"); break; }
		 */

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
