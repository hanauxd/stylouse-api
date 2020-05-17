package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.dto.request.ReviewRequest;
import lk.apiit.eea.stylouse.dto.response.ReviewResponse;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.Review;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductService productService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProductService productService) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
    }

    public Review createReview(ReviewRequest request, User user) {
        Product product = productService.getProductById(request.getProductId());
        Review oldReview = getReviewByUserAndProduct(user, product);
        if (oldReview != null) {
            throw new CustomException("User has already reviewed and rated the product.", HttpStatus.BAD_REQUEST);
        }
        Review review = new Review(user, product, request.getMessage(), request.getRate());

        return reviewRepository.save(review);
    }

    public ReviewResponse getReviewsForProduct(String productId, Pageable pageable) {
        Product product = productService.getProductById(productId);
        List<Review> reviews = reviewRepository.findAllByProduct(product, pageable);
        return new ReviewResponse(reviews);
    }

    public ReviewResponse deleteReview(String reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException("Review not found", HttpStatus.BAD_REQUEST));
        String reviewProductId = review.getProduct().getId();
        reviewRepository.delete(review);
        Product product = productService.getProductById(reviewProductId);
        List<Review> reviews = reviewRepository.findAllByProductOrderByDateDesc(product);
        return new ReviewResponse(reviews);
    }

    public Review getReviewByUserAndProduct(User user, Product product) {
        return reviewRepository.findByUserAndProduct(user, product);
    }
}
