package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByCategory(String category);
}
