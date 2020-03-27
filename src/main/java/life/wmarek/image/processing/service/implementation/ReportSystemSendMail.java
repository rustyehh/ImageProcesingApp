package life.wmarek.image.processing.service.implementation;

import life.wmarek.image.processing.config.AppConfig;
import life.wmarek.image.processing.service.ReportSystem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.*;

@Slf4j
@Service


public class ReportSystemSendMail implements ReportSystem {

    @Autowired
    AppConfig appConfig;

    static Properties mailServerProperties = System.getProperties();
    static Session getMailSession = Session.getDefaultInstance(mailServerProperties, null);
    static MimeMessage generateMailMessage = new MimeMessage(getMailSession);


    @Override
    public boolean sendMail(String message) {


        try {
            log.info("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");


            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");
            log.info("Mail Server Properties have been setup successfully..");


            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(appConfig.getEmailTo()));
            generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(appConfig.getEmailTOSnowRoot()));
            generateMailMessage.setSubject("Rusty app test");
            String emailBody = message + " greetings from rusty developer";
            generateMailMessage.setContent(emailBody, "text/html");
            log.info("Mail Session has been created successfully..");

            Transport transport = getMailSession.getTransport("smtp");

            transport.connect("smtp.gmail.com", appConfig.getEmailFrom(), appConfig.getPassword());
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
            return true;
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            return false;

        }
    }

    @Override
    public void crashLog(String errorLog) {
        if (StringUtils.isBlank(errorLog)) {
            sendMail("error occured but message is empty");
        } else if (!sendMail(errorLog)) {
            try (FileWriter myWriter = new FileWriter(appConfig.getPatchToErrorLogs() + "log.txt")) {
                myWriter.write(errorLog);
            } catch (IOException e) {
                log.error("falied to create errorLog file" + e);

            }
        }
    }
}


