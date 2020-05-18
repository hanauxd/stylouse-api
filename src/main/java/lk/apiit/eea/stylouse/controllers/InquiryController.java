package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.dto.request.InquiryRequest;
import lk.apiit.eea.stylouse.dto.response.InquiryResponse;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.Reply;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.services.InquiryService;
import lk.apiit.eea.stylouse.services.ProductService;
import lk.apiit.eea.stylouse.services.ReplyService;
import lk.apiit.eea.stylouse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inquiries")
public class InquiryController {
    private final InquiryService inquiryService;
    private final UserService userService;
    private final ProductService productService;
    private final ReplyService replyService;

    @Autowired
    public InquiryController(InquiryService inquiryService, UserService userService, ProductService productService, ReplyService replyService) {
        this.inquiryService = inquiryService;
        this.userService = userService;
        this.productService = productService;
        this.replyService = replyService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> createInquiry(@RequestBody InquiryRequest request, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        Product product = productService.getProductById(request.getProductIdOrInquiryId());
        Inquiry existingInquiry = inquiryService.getInquiryByUserAndProduct(user, product);
        if (existingInquiry != null) {
            Reply reply = replyService.createReply(request, user, existingInquiry);
            return new ResponseEntity<>(reply, HttpStatus.CREATED);
        } else {
            Inquiry inquiry = inquiryService.createInquiry(request, user, product);
            return new ResponseEntity<>(inquiry, HttpStatus.CREATED);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<?> getInquiriesByUser(Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        List<Inquiry> inquiries = inquiryService.getInquiriesByUser(user);
        return ResponseEntity.ok(new InquiryResponse(inquiries));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllInquiries() {
        List<Inquiry> inquiries = inquiryService.getAllInquiries();
        return ResponseEntity.ok(new InquiryResponse(inquiries));
    }
}
