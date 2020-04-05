package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.ProductImage;
import lk.apiit.eea.stylouse.repositories.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class ProductImageService {
    private ProductImageRepository productImageRepository;
    private FileService fileService;

    @Autowired
    public ProductImageService(ProductImageRepository productImageRepository, FileService fileService) {
        this.productImageRepository = productImageRepository;
        this.fileService = fileService;
    }

    public void createProductImage(Product product, MultipartFile[] files) {
        for (MultipartFile file : files) {
            String filename = fileService.store(file);
            ProductImage image = new ProductImage(product, filename);
            productImageRepository.save(image);
        }
    }

    public void deleteProductImage(String filename) {
        ProductImage productImage = getProductImage(filename);
        deleteImage(productImage);
    }

    public void deleteProductImages(Product product) {
        List<ProductImage> productImages = getImagesByProduct(product);
        for (ProductImage productImage : productImages) {
            deleteImage(productImage);
        }
    }

    private List<ProductImage> getImagesByProduct(Product product) {
        return productImageRepository.findByProduct(product);
    }

    private ProductImage getProductImage(String filename) {
        return productImageRepository.findByFilename(filename);
    }

    private void deleteImage(ProductImage productImage) {
        deleteImageFromStorage(productImage.getFilename());
        productImage.setProduct(null);
        productImageRepository.delete(productImage);
    }

    private void deleteImageFromStorage(String filename) {
        try {
            Path path = fileService.getPath(filename);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new CustomException("Failed to delete the file.", HttpStatus.BAD_REQUEST);
        }
    }
}
