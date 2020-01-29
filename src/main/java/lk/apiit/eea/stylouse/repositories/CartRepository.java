package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Cart;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findByUserAndProductAndSize(User user, Product product, String size);
    List<Cart> findByUser(User user);
}
