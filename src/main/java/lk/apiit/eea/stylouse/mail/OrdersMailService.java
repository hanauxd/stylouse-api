package lk.apiit.eea.stylouse.mail;

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
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrdersMailService extends MailService {
    private final Logger LOGGER = LoggerFactory.getLogger(OrdersMailService.class);

    @Autowired
    protected OrdersMailService(JavaMailSender sender, Configuration configuration) {
        super(sender, configuration);
    }

    @Override
    protected String htmlContent(User user, Object ordersModel) {
        try {
            Orders orders = (Orders) ordersModel;
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
