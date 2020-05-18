package lk.apiit.eea.stylouse.repositories;

import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, String> {
    List<Reply> findByInquiry(Inquiry inquiry);
}
