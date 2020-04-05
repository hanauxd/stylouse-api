package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.Orders;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {
    private OrdersRepository ordersRepository;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public List<Orders> getUserOrders(User user, Pageable pageable) {
        return ordersRepository.findByUser(user, pageable);
    }

    public Orders getOrderById(String id) {
        return ordersRepository.findById(id).orElseThrow(
                () -> new CustomException("Order not found", HttpStatus.NOT_FOUND)
        );
    }
}
