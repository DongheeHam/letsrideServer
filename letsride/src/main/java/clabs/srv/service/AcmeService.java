package clabs.srv.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;



@Service("AcmeService")
public class AcmeService {
private final static Logger logger = Logger.getLogger(AcmeService.class.getName());
	

private String mailSenderInfo="kshham73@gmail.com/hah9717abc";
public String accessUser="kshham@naver.com";


/*
 *  구글계정은 반드시 운영중인 경우여야 하고, 
 *  해당 사용자의 IMAP 사용 설정이 완료돼야 함.
 *  
 *  설정은 아이디/비번으로 구성
 *  예) GSuiteLink.mailSenderInfo = cbchung@sysoit.com/password
 *  
 */
public void sendMail(String adress, String name, String subject, String htmlBody) {
	Properties props = new Properties();
    props.setProperty("mail.smtp.host", "smtp.gmail.com");
    props.setProperty("mail.smtp.auth", "true"); 
	props.setProperty("mail.smtp.port", "587"); 
	props.setProperty("mail.smtp.starttls.enable", "true");

	String[] userInfo = mailSenderInfo.split("/");

	try {
    	Session session = Session.getInstance(props, new javax.mail.Authenticator() {
  				protected PasswordAuthentication getPasswordAuthentication() {
  					return new PasswordAuthentication(userInfo[0], userInfo[1]);
  				}
    	});

    	Message msg = new MimeMessage(session);
    	msg.setFrom(new InternetAddress(accessUser, "Gsuitelink System Account"));
    	msg.addRecipient(Message.RecipientType.TO, (name == null || "".equals(name)) ? new InternetAddress(adress) : new InternetAddress(adress, new String(name.getBytes("UTF-8"), "8859_1")));
    	msg.setSubject(subject);	 
    	
    	Multipart mp = new MimeMultipart();

    	MimeBodyPart htmlPart = new MimeBodyPart();
    	htmlPart.setContent(htmlBody, "text/html; charset=utf-8");
    	mp.addBodyPart(htmlPart);

    	msg.setContent(mp);
    	Transport.send(msg);

	} catch (AddressException e) {
		logger.error("AddressException:" + e.getMessage());
	} catch (MessagingException e) {
		logger.error("MessagingException:" + e.getMessage());
	} catch (UnsupportedEncodingException e) {
		logger.error("UnsupportedEncodingException:" + e.getMessage());
	} catch(Exception e) {
		logger.error("Exception:" + e.getMessage());
	}
}
}
