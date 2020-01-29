package lk.apiit.eea.stylouse.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Orders {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    String address;
    String city;
    String postalCode;
    String paymentMethod;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @JsonFormat(pattern = "dd-MM-yyyy hh:MM")
    private Date date;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.date = new Date();
    }

    public Orders(User user, String address, String city, String postalCode, String paymentMethod) {
        this.user = user;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.paymentMethod = paymentMethod;
    }
}