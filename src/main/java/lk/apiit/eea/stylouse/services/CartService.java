package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.dto.request.CartRequest;
import lk.apiit.eea.stylouse.dto.request.ShippingDetailsRequest;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.mail.OrdersMailService;
import lk.apiit.eea.stylouse.models.*;
import lk.apiit.eea.stylouse.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final OrdersService ordersService;
    private final OrdersMailService mailService;

    @Autowired
    public CartService(
            CartRepository cartRepository,
            ProductService productService,
            OrdersService ordersService,
            OrdersMailService mailService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.ordersService = ordersService;
        this.mailService = mailService;
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
        if (qty < 1) {
            throw new CustomException("The quantity must be at least 1.", HttpStatus.BAD_REQUEST);
        }
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
    public Orders checkout(User user, ShippingDetailsRequest request) {
        List<Cart> carts = getUserCarts(user);
        if (carts.size() > 0) {
            return createOrder(user, carts, request);
        } else {
            throw new CustomException("Cart is empty.", HttpStatus.BAD_REQUEST);
        }
    }

    private Orders createOrder(User user, List<Cart> carts, ShippingDetailsRequest request) {
        Orders order = new Orders(
                user,
                request.getAddress(),
                request.getCity(),
                request.getPostalCode(),
                request.getPaymentMethod()
        );
        for (Cart cart : carts) {
            order.getOrderItems().add(createOrderItem(cart, order));
            removeCart(cart.getId());
        }
        new Thread(() -> mailService.sendMail(user, "Order Confirmation", order)).start();
        return ordersService.createOrder(order);
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
