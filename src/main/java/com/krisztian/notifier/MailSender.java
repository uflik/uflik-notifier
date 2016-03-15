package com.krisztian.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by uflik on 3/14/16.
 */
public class MailSender {

    private static final String USERNAME = "adv.notifier@gmail.com";
    private static final String PASSWORD = "Ground123Ground";

    public static final String SENDER_ADDRESS = "adv.notifier@gmail.com";
    public static final String DEFAULT_MESSAGE_SUBJECT = "Albérletek";
    public static final String[] DEFAULT_PARTICIPANTS = new String[]{"jozsak20@gmail.com", "konczkinga92@gmail.com"};


    private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);


    public static void sendMailWithDefaultSettings(String messageText){
        sendMail(messageText, DEFAULT_MESSAGE_SUBJECT, DEFAULT_PARTICIPANTS);
    }

    public static void sendMail(String messageText, String subject, String... participants) {
        try {
            Transport.send(buildMessage(messageText, subject, getSession(getGmailProperties(), USERNAME, PASSWORD), participants));
            LOGGER.info("E-mail sent to the required participants ");
        } catch (MessagingException e) {
            LOGGER.error("E-mail sending failed", e);
            throw new RuntimeException(e);
        }
    }

    private static Message buildMessage(String messageText, String subject, Session session, String... participants) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_ADDRESS));

        for (String p : participants) {
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(p));
        }
        message.setSubject(subject);
        message.setText(messageText);

        return message;
    }

    private static Session getSession(Properties props, final String username, final String password) {
        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    private static Properties getGmailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

}
