package lk.apiit.eea.stylouse.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cart {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product", referencedColumnName = "id")
    @JsonIgnore
    private Product product;

    private int quantity;

    private String size;

    public Cart(User user, Product product, int quantity, String size) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.size = size;
    }

    @Transient
    public double getTotalPrice() {
        return quantity * product.getPrice();
    }
}
