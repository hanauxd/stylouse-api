package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Cart;
import lk.apiit.eea.stylouse.models.CartProduct;
import lk.apiit.eea.stylouse.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, String> {
    CartProduct findByCart(Cart cart);
    CartProduct findByCartAndProduct(Cart cart, Product product);
}
