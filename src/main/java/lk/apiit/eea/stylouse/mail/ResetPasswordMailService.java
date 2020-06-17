package lk.apiit.eea.stylouse.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.ResetPasswordToken;
import lk.apiit.eea.stylouse.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResetPasswordMailService extends MailService {
    private final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordMailService.class);

    @Autowired
    protected ResetPasswordMailService(JavaMailSender sender, Configuration configuration) {
        super(sender, configuration);
    }

    @Override
    protected String htmlContent(User user, Object resetPasswordToken) {
        try {
            ResetPasswordToken token = (ResetPasswordToken) resetPasswordToken;
            Map<String, Object> model = new HashMap<>();
            model.put("user", user.getLastName());
            model.put("token", token.getToken());

            configuration.setClassForTemplateLoading(this.getClass(), "/templates");
            Template template = configuration.getTemplate("reset-password.ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            LOGGER.warn("Failed to load email template. Error message: {}", e.getMessage());
            throw new CustomException("Failed to load email template", HttpStatus.BAD_REQUEST);
        }
    }
}
