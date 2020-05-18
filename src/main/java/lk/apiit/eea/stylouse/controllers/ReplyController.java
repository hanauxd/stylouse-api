package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.dto.request.InquiryRequest;
import lk.apiit.eea.stylouse.dto.response.InquiryResponse;
import lk.apiit.eea.stylouse.dto.response.ReplyResponse;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.Reply;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.services.InquiryService;
import lk.apiit.eea.stylouse.services.ReplyService;
import lk.apiit.eea.stylouse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/replies")
public class ReplyController {
    private final ReplyService replyService;
    private final InquiryService inquiryService;
    private final UserService userService;

    @Autowired
    public ReplyController(ReplyService replyService, InquiryService inquiryService, UserService userService) {
        this.replyService = replyService;
        this.inquiryService = inquiryService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createReply(@RequestBody InquiryRequest request, Authentication auth) {
        Inquiry inquiry = inquiryService.getInquiryById(request.getProductIdOrInquiryId());
        User user = userService.getUserByEmail(auth.getName());
        Reply reply = replyService.createReply(request, user, inquiry);
        Inquiry updatedInquiry = inquiryService.getInquiryById(inquiry.getId());
        return new ResponseEntity<>(updatedInquiry, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{inquiryId}")
    public ResponseEntity<?> getRepliesByInquiry(@PathVariable String inquiryId) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        List<Reply> replies = replyService.getRepliesByInquiry(inquiry);
        return ResponseEntity.ok(new ReplyResponse(replies));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/read")
    public ResponseEntity<?> markMessageAsRead(@RequestBody List<String> replyIds, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        replyService.markMessageAsRead(replyIds, user);
        List<Inquiry> inquiries = new ArrayList<>();
        switch (user.getRole()) {
            case "ROLE_USER": {
                inquiries = inquiryService.getInquiriesByUser(user);
                break;
            }
            case "ROLE_ADMIN": {
                inquiries = inquiryService.getAllInquiries();
                break;
            }
            default:
                break;
        }
        return ResponseEntity.ok(new InquiryResponse(inquiries));
    }
}
