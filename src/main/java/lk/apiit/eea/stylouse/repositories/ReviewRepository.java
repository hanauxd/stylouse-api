package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.Review;
import lk.apiit.eea.stylouse.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findAllByProduct(Product product, Pageable pageable);

    Review findByUserAndProduct(User user, Product product);

    List<Review> findAllByProductOrderByDateDesc(Product product);
}
