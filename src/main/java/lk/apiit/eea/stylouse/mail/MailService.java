package lk.apiit.eea.stylouse.mail;

import freemarker.template.Configuration;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
abstract class MailService {
    private final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender sender;
    protected final Configuration configuration;
    @Value("${spring.mail.username}")
    private String stylouseEmail;

    @Autowired
    protected MailService(JavaMailSender sender, Configuration configuration) {
        this.sender = sender;
        this.configuration = configuration;
    }

    public void sendMail(User user, String subject, Object object) {
        MimeMessage message = createMimeMessage(user, subject, object);
        sender.send(message);
        LOGGER.debug("Mail sent successfully.");
    }

    private MimeMessage createMimeMessage(User user, String subject, Object model) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(sender.createMimeMessage(), true);
            helper.setTo(user.getEmail());
            helper.setFrom(new InternetAddress(stylouseEmail, "Stylouse"));
            helper.setSubject(subject);
            helper.setText(htmlContent(user, model), true);
            return helper.getMimeMessage();
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.warn("Failed to construct MimeMessageHelper. Error message: {}", e.getMessage());
            throw new CustomException("Failed to construct message helper", HttpStatus.BAD_REQUEST);
        }
    }

    protected abstract String htmlContent(User user, Object model);
}
