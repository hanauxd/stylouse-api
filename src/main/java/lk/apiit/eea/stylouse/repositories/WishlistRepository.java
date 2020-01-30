package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, String> {
    List<Wishlist> findByUser(User user);
    Wishlist findByUserAndProduct(User user, Product product);
}