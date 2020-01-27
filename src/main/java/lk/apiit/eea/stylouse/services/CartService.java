package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.dto.request.CartRequest;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.Cart;
import lk.apiit.eea.stylouse.models.CartProduct;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private CartRepository cartRepository;
    private ProductService productService;
    private CartProductService cartProductService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductService productService, CartProductService cartProductService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.cartProductService = cartProductService;
    }

    public Cart getCartById(String id) {
        return cartRepository.findById(id).orElseThrow(() -> new CustomException("Cart not found.", HttpStatus.NOT_FOUND));
    }

    public List<Cart> getCartsByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public Cart createOrUpdateCart(List<CartRequest> requests, User user) {
        Cart activeCart = cartRepository.findByUserAndStatus(user, "Active");
        if (activeCart != null) {
            if (activeCart.getCartProducts().size() > 0) {
                for (CartRequest req : requests) {
                    Product product = productService.getProductById(req.getProductId());
                    CartProduct cartProduct = cartProductService.getProductByCart(activeCart, product);
                    if (cartProduct != null) {
                        cartProduct.setQuantity(cartProduct.getQuantity() + req.getQuantity());
                    } else {
                        addProducts(activeCart, product, req.getQuantity());
                    }
                }
            } else {
                for (CartRequest req : requests) {
                    Product product = productService.getProductById(req.getProductId());
                    addProducts(activeCart, product, req.getQuantity());
                }
            }
            return cartRepository.save(activeCart);
        } else {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setStatus("Active");
            for (CartRequest req : requests) {
                Product product = productService.getProductById(req.getProductId());
                addProducts(cart, product, req.getQuantity());
            }
            return cartRepository.save(cart);
        }
    }

    public void deleteCart(Cart cart) {
        cart.getUser().removeCart(cart);
        cartRepository.delete(cart);
    }

    public Cart removeProduct(Cart cart, Product product) {
        CartProduct cartProduct = cartProductService.getByCart(cart);
        if (product.getId().equals(cartProduct.getProduct().getId())) {
            cartProduct.getCart().removeCartProduct(cartProduct);
            cartProduct.getProduct().removeCartProduct(cartProduct);
            cartProductService.deleteCartProduct(cartProduct);
        }
        return cart;
    }

    private void addProducts(Cart cart, Product product, int quantity) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProduct(product);
        cartProduct.setCart(cart);
        cartProduct.setQuantity(quantity);
        cart.getCartProducts().add(cartProduct);
    }
}
