package lk.apiit.eea.stylouse.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "orders", referencedColumnName = "id")
    @JsonIgnore
    private Orders orders;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "product", referencedColumnName = "id")
    private Product product;

    private int quantity;

    private String size;

    public OrderItem(Product product, int quantity, String size, Orders orders) {
        this.product = product;
        this.quantity = quantity;
        this.size = size;
        this.orders = orders;
    }
}