package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.Category;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.ProductCategory;
import lk.apiit.eea.stylouse.repositories.ProductCategoryRepository;
import lk.apiit.eea.stylouse.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private ProductCategoryRepository prodCateRepository;
    private ProductRepository productRepository;
    private CategoryService categoryService;

    @Autowired
    public ProductService(ProductCategoryRepository prodCateRepository, ProductRepository productRepository, CategoryService categoryService) {
        this.prodCateRepository = prodCateRepository;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new CustomException("Product not found.", HttpStatus.NOT_FOUND));
    }

    public List<Product> getProductsByCategory(Category category) {
        List<ProductCategory> productCategoryList = prodCateRepository.findByCategory(category);
        List<Product> products = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            products.add(productCategory.getProduct());
        }
        return products;
    }

    public Product createProduct(Product product, List<String> categories) {
        addCategories(product, categories);
        return productRepository.save(product);
    }

    public Product updateProduct(Product product, List<String> categories) {
        addCategories(product, categories);
        return productRepository.save(product);
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    private void addCategories(Product product, List<String> categories) {
        if (product.getId() != null) removeOldCategories(product);
        for (String cate : categories) {
            Category category = categoryService.getByCategory(cate);
            ProductCategory productCategory = new ProductCategory(product, category);
            product.getProductCategories().add(productCategory);
            category.getProductCategories().add(productCategory);
        }
    }

    private void removeOldCategories(Product product) {
        List<ProductCategory> productCategories = prodCateRepository.findByProduct(product);
        for (ProductCategory prodCate : productCategories) {
            prodCate.getProduct().removeProductCategory(prodCate);
            prodCate.getCategory().removeProductCategory(prodCate);
            prodCate.setProduct(null);
            prodCate.setCategory(null);
            prodCateRepository.delete(prodCate);
        }
    }
}
