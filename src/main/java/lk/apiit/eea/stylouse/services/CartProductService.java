package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.models.Cart;
import lk.apiit.eea.stylouse.models.CartProduct;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.repositories.CartProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartProductService {
    private CartProductRepository cartProductRepository;

    @Autowired
    public CartProductService(CartProductRepository cartProductRepository) {
        this.cartProductRepository = cartProductRepository;
    }

    public CartProduct getByCart(Cart cart) {
        return cartProductRepository.findByCart(cart);
    }

    public CartProduct getProductByCart(Cart cart, Product product) {
        return cartProductRepository.findByCartAndProduct(cart, product);
    }

    public void deleteCartProduct(CartProduct cartProduct) {
        cartProductRepository.delete(cartProduct);
    }
}
