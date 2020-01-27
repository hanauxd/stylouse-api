package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.dto.request.CartRequest;
import lk.apiit.eea.stylouse.models.Cart;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.services.CartService;
import lk.apiit.eea.stylouse.services.ProductService;
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
    private ProductService productService;

    @Autowired
    public CartController(CartService cartService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getCartsByUser(@PathVariable String id) {
        User user = userService.getUserById(id);
        List<Cart> carts = cartService.getCartsByUser(user);
        return ResponseEntity.ok(carts);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable String id) {
        Cart cart = cartService.getCartById(id);
        return ResponseEntity.ok(cart);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createOrUpdateCart(@RequestBody List<CartRequest> cartItems, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        Cart cart = cartService.createOrUpdateCart(cartItems, user);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{cartId}/product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String cartId, @PathVariable String productId) {
        Product product = productService.getProductById(productId);
        Cart oldCart = cartService.getCartById(cartId);
        Cart cart = cartService.removeProduct(oldCart, product);
        return ResponseEntity.ok(cart);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable String id) {
        Cart cart = cartService.getCartById(id);
        cartService.deleteCart(cart);
        return ResponseEntity.ok("Cart deleted.");
    }
}
