package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.dto.request.InquiryRequest;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.Reply;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.repositories.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Reply createReply(InquiryRequest request, User user, Inquiry inquiry) {
        Reply reply = new Reply(inquiry, user, request.getMessage());
        inquiry.getReplies().add(reply);
        return replyRepository.save(reply);
    }

    public List<Reply> getRepliesByInquiry(Inquiry inquiry) {
        return replyRepository.findByInquiry(inquiry);
    }

    public Reply getReplyById(String replyId) {
        return replyRepository.findById(replyId).orElseThrow(
                () -> new CustomException("Reply not found", HttpStatus.NOT_FOUND)
        );
    }

    public void markMessageAsRead(List<String> replyIds, User user) {
        List<Reply> replies = replyRepository.findAllById(replyIds);
        for (Reply reply : replies) {
            if (!user.equals(reply.getUser())) {
                reply.setRead(true);
            }
        }
        replyRepository.saveAll(replies);
    }
}
