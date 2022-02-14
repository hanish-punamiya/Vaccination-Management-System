package edu.sjsu.cmpe275.Util;


import edu.sjsu.cmpe275.Config.EmailConfig;
import edu.sjsu.cmpe275.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class NotificationHelper {

    JavaMailSender mailSender;

    public void sendEmail(EmailConfig emailConfig, String sender, String receiver, String message, String subject){

//        public void sendEmail() {
            Mail mail = new Mail();
            mail.setMailFrom(sender);
            mail.setMailTo(receiver);
            mail.setMailSubject(subject);
            mail.setMailContent("Appointment Status: ");
            mailSender = emailConfig.getJavaMailSender();
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            try {

//                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//
//                mimeMessageHelper.setSubject(mail.getMailSubject());
//                mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "sakethreddy.333@gmail.com"));
//                mimeMessageHelper.setTo(mail.getMailTo());
//                mimeMessageHelper.setText(mail.getMailContent());
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

                simpleMailMessage.setFrom(mail.getMailFrom());
                simpleMailMessage.setTo(mail.getMailTo());
                simpleMailMessage.setSubject(subject);
                simpleMailMessage.setText(message);

                mailSender.send(simpleMailMessage);
//                mailSender.send(mimeMessageHelper.getMimeMessage());

            }
            catch (Exception e){
                e.printStackTrace();
            }
//            catch (MessagingException e) {
//                e.printStackTrace();
//            }
//            catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
