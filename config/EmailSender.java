package ng.optisoft.rosabon.config;

import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.dto.SendEmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j

public class EmailSender {

    @Value("${spring.profiles.active}")
    private String activeProfile;

	@Autowired
    private JavaMailSender emailSender;

    @Autowired
    private MailProperties mailProperties;


	
	@Async("threadPoolTaskExecutor")
    public void sendEmail(SendEmailDto sendEmail) throws MessagingException, UnsupportedEncodingException {
            emailSender = mailProperties.getMailSender();

            log.info("MAIL TO : "+ sendEmail.getMailTo() + " SUBJECT : "+ sendEmail.getSubject() + " MAIL FROM "+ sendEmail.getFrom());

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(sendEmail.getMailTo());
            helper.setText(sendEmail.getBody(), true);
            helper.setSubject(sendEmail.getSubject());
            helper.setFrom(sendEmail.getFrom(),"ROSABON FINANCIAL SERVICES");
            emailSender.send(message);
            log.info("Email Sent to {}", sendEmail.getMailTo());

    }
	
	 
}