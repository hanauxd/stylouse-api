package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.dto.request.CartRequest;
import lk.apiit.eea.stylouse.dto.request.ShippingDetailsRequest;
import lk.apiit.eea.stylouse.models.Cart;
import lk.apiit.eea.stylouse.models.Orders;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.services.CartService;
import lk.apiit.eea.stylouse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private CartService cartService;
    private UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createOrUpdateCart(@RequestBody CartRequest cartRequest, Authentication auth) {
        List<Cart> carts = cartService.createOrUpdateCart(userService.getUserByEmail(auth.getName()), cartRequest);
        return new ResponseEntity<>(carts, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}/{quantity}")
    public ResponseEntity<?> updateCart(@PathVariable String id, @PathVariable int quantity, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        List<Cart> carts = cartService.updateCart(id, quantity, user);
        return ResponseEntity.ok(carts);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable String id, Authentication auth) {
        cartService.removeCart(id);
        List<Cart> carts = cartService.getUserCarts(userService.getUserByEmail(auth.getName()));
        return ResponseEntity.ok(carts);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody ShippingDetailsRequest request, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        Orders orders = cartService.checkout(user, request);
        return ResponseEntity.ok(orders);
    }
}
