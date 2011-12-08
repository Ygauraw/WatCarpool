package com.uw.watcarpool.shared;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Emailer
{
	
  public void sendEmail(String subject, String msgBody, String to) throws UnsupportedEncodingException
  {
	Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);


    try {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("watcarpoolapp@gmail.com", "Watcarpool"));
        msg.addRecipient(Message.RecipientType.TO,
                         new InternetAddress(to, "Watcarpool User"));
        msg.setSubject(subject);
        msg.setText(msgBody);
        Transport.send(msg);

    } catch (AddressException e) {
        e.printStackTrace();
    } catch (MessagingException e) {
        e.printStackTrace();
    }
  }
}

