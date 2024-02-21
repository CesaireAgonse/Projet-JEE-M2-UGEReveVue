package fr.uge.revevue.controller.rest;

import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.form.ReviewForm;
import fr.uge.revevue.information.ReviewInformation;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/reviews")
public class ReviewRestController {
    private final ReviewService reviewService;
    @Autowired
    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewInformation> review(@PathVariable("reviewId") @Valid long reviewId,
                                                    @ModelAttribute("commentForm") CommentForm commentForm,
                                                    @ModelAttribute("reviewForm") ReviewForm reviewForm){
        var review = reviewService.getInformation(reviewId);
        if (review == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(review);
    }
}
