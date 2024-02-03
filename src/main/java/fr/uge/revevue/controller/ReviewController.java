package fr.uge.revevue.controller;

import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.form.ReviewForm;
import fr.uge.revevue.service.CommentService;
import fr.uge.revevue.service.ReviewService;
import fr.uge.revevue.service.UserService;
import fr.uge.revevue.service.VoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class ReviewController {
    private VoteService voteService;

    private UserService userService;

    private CommentService commentService;

    private ReviewService reviewService;

    public ReviewController(VoteService voteService, UserService userService,CommentService commentService, ReviewService reviewService) {
        this.voteService = voteService;
        this.userService = userService;
        this.commentService = commentService;
        this.reviewService = reviewService;
    }
    @GetMapping("/reviews/{reviewId}")
    public String review(@PathVariable("reviewId") @Valid long reviewId,
                       @ModelAttribute("commentForm") @Valid CommentForm commentForm,
                       @ModelAttribute("reviewForm") @Valid ReviewForm reviewForm,
                       Model model){
        model.addAttribute("auth", userService.getInformation(userService.currentUser().getUsername()));
        var review = reviewService.getInformation(reviewId);
        model.addAttribute("review", review);
        return "reviews/reviewReview";
    }
    @PostMapping("/reviews/vote/{reviewId}")
    public String reviewVoted(@PathVariable("reviewId") @Valid Long reviewId, @RequestParam("voteType")Vote.VoteType voteType){
        voteService.postVoted(userService.currentUser().getId(),reviewId,voteType);
        return "redirect:/";
    }

    @PostMapping("/reviews/comment/{reviewId}")
    public String reviewCommented(@PathVariable("reviewId") @Valid long reviewId,
                                  @ModelAttribute("commentForm") CommentForm commentForm){
        commentService.postCommented(userService.currentUser().getId(),reviewId,commentForm.getContent());
        return "redirect:/reviews/" + reviewId;
    }

    @PostMapping("/reviews/review/{reviewId}")
    public String reviewReviewed(@PathVariable("reviewId") @Valid long reviewId,
                                 @ModelAttribute("reviewForm")ReviewForm reviewForm){
        reviewService.postReview(userService.currentUser().getId(),reviewId,reviewForm.getContent());
        return "redirect:/reviews/" + reviewId;
    }
}
