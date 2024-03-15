package fr.uge.revevue.controller.rest;

import fr.uge.revevue.information.review.ReviewInformation;
import fr.uge.revevue.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/reviews")
public class  ReviewRestController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewInformation> review(@PathVariable("reviewId") @Valid long reviewId){
        var review = reviewService.getInformation(reviewId);
        if (review == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(review);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> reviewDeleted(@PathVariable("reviewId") @Valid long reviewId) {
        if (!reviewService.isExisted(reviewId)){
            return ResponseEntity.notFound().build();
        }
        reviewService.delete(reviewId);
        return ResponseEntity.ok().build();
    }
}
