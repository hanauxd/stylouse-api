package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.dto.request.ProductRequest;
import lk.apiit.eea.stylouse.models.Category;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.services.CategoryService;
import lk.apiit.eea.stylouse.services.ProductImageService;
import lk.apiit.eea.stylouse.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    private CategoryService categoryService;
    private ProductImageService productImageService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, ProductImageService productImageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productImageService = productImageService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestParam("product") ProductRequest request, @RequestParam("file") MultipartFile[] files) {
        Product product = productService.createProduct(request.getProduct(), request.getCategories());
        productImageService.createProductImage(product, files);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(Pageable pageable) {
        List<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String id) {
        Category category = categoryService.getCategoryById(id);
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody ProductRequest request) {
        Product persistedProduct = productService.getProductById(id);
        Product product = request.updateProduct(persistedProduct);
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        Product product = productService.getProductById(id);
        productService.deleteProduct(product);
        return ResponseEntity.ok("Product deleted.");
    }
}
