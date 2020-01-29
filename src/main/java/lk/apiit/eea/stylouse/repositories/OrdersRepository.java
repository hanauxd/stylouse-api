package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Orders;
import lk.apiit.eea.stylouse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {
    List<Orders> findByUser(User user);
}
