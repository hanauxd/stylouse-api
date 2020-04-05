package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
    List<ProductImage> findByProduct(Product product);
    ProductImage findByFilename(String filename);
}
