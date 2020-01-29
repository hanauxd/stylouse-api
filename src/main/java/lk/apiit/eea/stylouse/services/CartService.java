package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.dto.request.CartRequest;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.*;
import lk.apiit.eea.stylouse.repositories.CartRepository;
import lk.apiit.eea.stylouse.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private CartRepository cartRepository;
    private ProductService productService;
    private OrdersRepository ordersRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductService productService, OrdersRepository ordersRepository) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.ordersRepository = ordersRepository;
    }

    public List<Cart> createOrUpdateCart(User user, CartRequest request) {
        Product product = productService.getProductById(request.getProductId());
        Cart persistedCart = cartRepository.findByUserAndProductAndSize(user, product, request.getSize());
        if (persistedCart != null) {
            persistedCart.setQuantity(request.getQuantity() + persistedCart.getQuantity());
        } else {
            persistedCart = new Cart(user, product, request.getQuantity(), request.getSize());
        }
        cartRepository.save(persistedCart);
        return getUserCarts(user);
    }

    public List<Cart> getUserCarts(User user) {
        return cartRepository.findByUser(user);
    }

    public Cart getCartById(String id) {
        return cartRepository.findById(id).orElseThrow(
                () -> new CustomException("Cart not found", HttpStatus.NOT_FOUND)
        );
    }

    public Orders checkout(User user) {
        List<Cart> carts = getUserCarts(user);
        if (carts.size() > 0) {
            return createOrder(user, carts);
        } else {
            throw new CustomException("Cart is empty.", HttpStatus.BAD_REQUEST);
        }
    }

    private Orders createOrder(User user, List<Cart> carts) {
        Orders order = new Orders(user);
        for (Cart cart : carts) {
            order.getOrderItems().add(createOrderItem(cart, order));
            removeCart(cart.getId());
        }
        return ordersRepository.save(order);
    }

    private OrderItem createOrderItem(Cart cart, Orders order) {
        return new OrderItem(
                cart.getProduct(),
                cart.getQuantity(),
                cart.getSize(),
                order
        );
    }

    public void removeCart(String id) {
        Cart cart = getCartById(id);
        cart.getProduct().removeCart(cart);
        cart.getUser().removeCart(cart);
        cartRepository.delete(cart);
    }
}
