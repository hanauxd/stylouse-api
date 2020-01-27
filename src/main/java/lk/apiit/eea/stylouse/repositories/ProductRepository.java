package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
