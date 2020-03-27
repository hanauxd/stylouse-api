package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.Category;
import lk.apiit.eea.stylouse.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> createCategory(Category category) {
        boolean isPresent = categoryRepository.findByCategory(category.getCategory()).isPresent();
        if (isPresent) throw new CustomException("Category already exist.", HttpStatus.BAD_REQUEST);
        categoryRepository.save(category);
        return getAllCategories();
    }

    public Category getCategoryById(String id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CustomException("Categoty not found.", HttpStatus.NOT_FOUND));
    }

    public Category getByCategory(String name) {
        return categoryRepository.findByCategory(name).orElseThrow(() -> new CustomException("Category not found.", HttpStatus.NOT_FOUND));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategoryById(String id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}
