package lk.apiit.eea.stylouse.dto.request;

import lk.apiit.eea.stylouse.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private int quantity;
    private double price;
    private String description;
    private List<String> categories = new ArrayList<>();

    public Product getProduct() {
        Product product = new Product();
        product.setName(this.name);
        product.setQuantity(this.quantity);
        product.setPrice(this.price);
        product.setDescription(this.description);
        return product;
    }

    public Product updateProduct(Product product) {
        product.setName(this.name);
        product.setQuantity(this.quantity);
        product.setPrice(this.price);
        product.setDescription(this.description);
        return product;
    }
}
