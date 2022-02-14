package edu.sjsu.cmpe275.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service
public class EmailService implements EmailSender {
	private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	JavaMailSender javaMailSender;

	@Override
	@Async
	public void send(String to, String email) {
		try {
			System.out.println("hello world");
			MimeMessage mimeMessage=javaMailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,"utf-8");
			helper.setText(email,true);
			helper.setTo(to);
			helper.setSubject("Confirme your email adress");
			helper.setFrom("Cfbm.Training@gmail.com");
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			LOGGER.error("Failed to send email !");
			throw new IllegalStateException("Failed to send email !");
		}
	}

}
