package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.dto.request.ReviewRequest;
import lk.apiit.eea.stylouse.dto.response.ReviewResponse;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.Review;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.services.ReviewService;
import lk.apiit.eea.stylouse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request, Authentication auth, Pageable pageable) {
        User user = userService.getUserByEmail(auth.getName());
        reviewService.createReview(request, user);
        return getReviewsForProduct(request.getProductId(), pageable, auth);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getReviewsForProduct(@PathVariable String productId, Pageable pageable, Authentication auth) {
        ReviewResponse response = reviewService.getReviewsForProduct(productId, pageable);
        if (auth != null) {
            if (response.getReviews().size() > 0) {
                User user = userService.getUserByEmail(auth.getName());
                Product product = response.getReviews().get(0).getProduct();
                Review review = reviewService.getReviewByUserAndProduct(user, product);
                response.setHasUserRated(review != null);
            }
        }
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }
}
