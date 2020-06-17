package lk.apiit.eea.stylouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reset_password_token")
public class ResetPasswordToken {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;

    private String token;

    private Date expiryDate;

    private long expireIn;

    public ResetPasswordToken(User user, long expireIn) {
        this.user = user;
        this.expiryDate = new Date(new Date().getTime()+expireIn);
        this.token = String.valueOf(new Random().nextInt(899999) + 100000);
    }
}
