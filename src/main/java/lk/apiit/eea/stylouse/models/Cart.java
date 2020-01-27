package lk.apiit.eea.stylouse.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String status;
    @JsonFormat(pattern = "dd-MM-yyyy hh:MM")
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.date = new Date();
        this.status = "Active";
    }

    public void removeCartProduct(CartProduct cartProduct) {
        cartProduct.setCart(null);
        this.cartProducts.remove(cartProduct);
    }

    @Transient
    public double getTotalCartPrice() {
        double sum = 0;
        for (CartProduct cartProduct : this.cartProducts) {
            sum += cartProduct.getTotalPrice();
        }
        return sum;
    }
}
