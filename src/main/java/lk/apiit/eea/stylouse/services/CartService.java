package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.dto.request.CartRequest;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.*;
import lk.apiit.eea.stylouse.repositories.CartRepository;
import lk.apiit.eea.stylouse.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional
    public List<Cart> updateCart(String id, int qty, User user) {
        Cart cart = getCartById(id);
        int stock = cart.getProduct().getQuantity();
        if (qty <= stock) {
            cart.setQuantity(qty);
            return getUserCarts(user);
        } else {
            throw new CustomException("Requested quantity not available.", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
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
        checkProductAvailability(cart.getProduct(), cart.getQuantity());
        return new OrderItem(
                cart.getProduct(),
                cart.getQuantity(),
                cart.getSize(),
                order
        );
    }

    private void checkProductAvailability(Product product, int quantity) {
        Product stock = productService.getProductById(product.getId());
        if (quantity <= stock.getQuantity()) {
            stock.setQuantity(stock.getQuantity() - quantity);
        } else {
            throw new CustomException("Product does not have enough stock.", HttpStatus.BAD_REQUEST);
        }
    }

    public void removeCart(String id) {
        Cart cart = getCartById(id);
        cart.getProduct().removeCart(cart);
        cart.getUser().removeCart(cart);
        cartRepository.delete(cart);
    }
}
