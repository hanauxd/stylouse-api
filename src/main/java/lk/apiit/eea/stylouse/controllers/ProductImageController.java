package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.services.FileService;
import lk.apiit.eea.stylouse.services.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/product/images")
public class ProductImageController {
    private FileService fileService;
    private ProductImageService productImageService;

    @Autowired
    public ProductImageController(FileService fileService, ProductImageService productImageService) {
        this.fileService = fileService;
        this.productImageService = productImageService;
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<?> downloadImage(@PathVariable String filename, HttpServletRequest request) {
        try {
            UrlResource resource = fileService.getResource(filename);
            String mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename())
                    .body(resource);
        } catch (IOException e) {
            throw new CustomException("File not found.", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{filename}")
    public ResponseEntity<?> deleteProductImage(@PathVariable String filename) {
        productImageService.deleteProductImage(filename);
        return ResponseEntity.ok("File deleted.");
    }
}