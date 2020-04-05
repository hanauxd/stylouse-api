package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Category;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {
    List<ProductCategory> findByCategory(Category category);
    List<ProductCategory> findByProduct(Product product);
}
