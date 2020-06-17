package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.dto.request.InquiryRequest;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.repositories.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final ReplyService replyService;

    @Autowired
    public InquiryService(InquiryRepository inquiryRepository, ReplyService replyService) {
        this.inquiryRepository = inquiryRepository;
        this.replyService = replyService;
    }

    public Inquiry createInquiry(InquiryRequest request, User user, Product product) {
        Inquiry inquiry = new Inquiry(user, product);
        replyService.createReply(request, user, inquiry);
        return inquiryRepository.save(inquiry);
    }

    public Inquiry getInquiryByUserAndProduct(User user, Product product) {
        return inquiryRepository.findByUserAndProduct(user, product);
    }

    public Inquiry getInquiryById(String id) {
        return inquiryRepository.findById(id).orElseThrow(() -> new CustomException("Inquiry not found", HttpStatus.BAD_REQUEST));
    }

    public List<Inquiry> getInquiriesByUser(User user) {
        return inquiryRepository.findByUser(user);
    }

    public List<Inquiry> getAllInquiries() {
        return inquiryRepository.findAll();
    }

    public List<Inquiry> getInquiryByProduct(Product product) {
        return inquiryRepository.findByProduct(product);
    }
}
