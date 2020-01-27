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
    private String size;
    private int quantity;
    private double price;
    private List<String> categories = new ArrayList<>();

    public Product getProduct() {
        Product product = new Product();
        product.setName(this.name);
        product.setSize(this.size);
        product.setQuantity(this.quantity);
        product.setPrice(this.price);
        return product;
    }

    public Product updateProduct(Product product) {
        product.setName(this.name);
        product.setSize(this.size);
        product.setQuantity(this.quantity);
        product.setPrice(this.price);
        return product;
    }
}
