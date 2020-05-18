package lk.apiit.eea.stylouse.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.OrderItem;
import lk.apiit.eea.stylouse.models.Orders;
import lk.apiit.eea.stylouse.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {
    private final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender sender;
    private final Configuration configuration;
    @Value("${spring.mail.username}")
    private String stylouseEmail;

    @Autowired
    public MailService(JavaMailSender sender, Configuration configuration) {
        this.sender = sender;
        this.configuration = configuration;
    }

    public void sendMailWithAttachment(User user, Orders orders) {
        MimeMessage message = createMimeMessage(sender.createMimeMessage(), user, orders);
        sender.send(message);
        LOGGER.debug("Mail sent successfully.");
    }

    private MimeMessage createMimeMessage(MimeMessage message, User user, Orders orders) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setFrom(new InternetAddress(stylouseEmail, "Stylouse"));
            helper.setSubject("Order confirmed");
            helper.setText(htmlContent(user, orders), true);
            return helper.getMimeMessage();
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.warn("Failed to construct MimeMessageHelper. Error message: {}", e.getMessage());
            throw new CustomException("Failed to construct message helper", HttpStatus.BAD_REQUEST);
        }
    }

    private String htmlContent(User user, Orders orders) {
        try {
            double total = 0;
            for (OrderItem orderItem : orders.getOrderItems()) {
                total += orderItem.getQuantity() * orderItem.getProduct().getPrice();
            }

            SimpleDateFormat format = new SimpleDateFormat("EEEEE, dd MMM yyyy HH:mm aa");
            String date = format.format(orders.getDate());
            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getLastName());
            model.put("address", orders.getAddress());
            model.put("city", orders.getCity());
            model.put("postalCode", orders.getPostalCode());
            model.put("paymentMethod", orders.getPaymentMethod());
            model.put("date", date);
            model.put("orders", orders.getOrderItems());
            model.put("total", total);

            configuration.setClassForTemplateLoading(this.getClass(), "/templates");
            Template template = configuration.getTemplate("email-template.ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            LOGGER.warn("Failed to load email template. Error message: {}", e.getMessage());
            throw new CustomException("Failed to load email template", HttpStatus.BAD_REQUEST);
        }
    }
}
