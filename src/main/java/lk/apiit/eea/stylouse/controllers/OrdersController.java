package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.models.Orders;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.services.OrdersService;
import lk.apiit.eea.stylouse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/orders")
public class OrdersController {
    private OrdersService ordersService;
    private UserService userService;

    @Autowired
    public OrdersController(OrdersService ordersService, UserService userService) {
        this.ordersService = ordersService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<?> getUserOrders(Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        List<Orders> orders = ordersService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        Orders order = ordersService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
}
