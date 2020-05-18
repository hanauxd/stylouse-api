package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, String> {
    Inquiry findByUserAndProduct(User user, Product product);

    List<Inquiry> findByUser(User user);
}
