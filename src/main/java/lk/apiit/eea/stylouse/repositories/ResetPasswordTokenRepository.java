package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.ResetPasswordToken;
import lk.apiit.eea.stylouse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {
    ResetPasswordToken findByUser(User user);
}
