package fr.uge.revevue.controller;

import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.form.ReviewForm;
import fr.uge.revevue.information.PagingInformation;
import fr.uge.revevue.information.user.SimpleUserInformation;
import fr.uge.revevue.service.CommentService;
import fr.uge.revevue.service.ReviewService;
import fr.uge.revevue.service.UserService;
import fr.uge.revevue.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class ReviewController {

    private final VoteService voteService;

    private final UserService userService;

    private final CommentService commentService;

    private final ReviewService reviewService;
    @Autowired
    public ReviewController(VoteService voteService, UserService userService,CommentService commentService, ReviewService reviewService) {
        this.voteService = voteService;
        this.userService = userService;
        this.commentService = commentService;
        this.reviewService = reviewService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/reviews/{reviewId}")
    public String review(@PathVariable("reviewId") @Valid long reviewId,
                         @ModelAttribute("commentForm") CommentForm commentForm,
                         @ModelAttribute("reviewForm") ReviewForm reviewForm,
                         @RequestParam(value = "reviewPageNumber", required = false) Integer reviewPageNumber,
                         @RequestParam(value = "commentPageNumber", required = false) Integer commentPageNumber,
                         Model model){
        var user = userService.currentUser();
        if (user != null){
            model.addAttribute("auth", SimpleUserInformation.from(user));
        }
        var review = reviewService.getInformation(reviewId);
        if (review == null){
            throw new IllegalStateException("review not found");
        }
        model.addAttribute("review", review);

        PagingInformation pagingInfo = new PagingInformation(0, reviewPageNumber, commentPageNumber,0);
        pagingInfo = pagingInfo.setDefaultsIfNull();
        model.addAttribute("pagingInfo", pagingInfo);

        var reviewsFromPost = reviewService.getReviews(review.id(), pagingInfo.reviewPageNumber());
        var commentsFromPost = commentService.getComments(review.id(), pagingInfo.commentPageNumber());
        model.addAttribute("reviewsFromPost", reviewsFromPost);
        model.addAttribute("commentsFromPost", commentsFromPost);
        return "reviews/reviewReview";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reviews/vote/{reviewId}")
    public String reviewVoted(@PathVariable("reviewId") @Valid Long reviewId,
                              @RequestParam("voteType")Vote.VoteType voteType,
                              BindingResult result){
        if (result.hasErrors()){
            return "redirect:/reviews/" + reviewId;
        }
        voteService.postVoted(userService.currentUser().getId(),reviewId,voteType);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reviews/comment/{reviewId}")
    public String reviewCommented(@PathVariable("reviewId") @Valid long reviewId,
                                  @ModelAttribute("commentForm") @Valid CommentForm commentForm,
                                  BindingResult result){
        if (result.hasErrors()){
            return "redirect:/reviews/" + reviewId;
        }
        commentService.postCommented(userService.currentUser().getId(), reviewId, commentForm.getContent(), commentForm.getCodeSelection());
        return "redirect:/reviews/" + reviewId;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/reviews/review/{reviewId}")
    public String reviewReviewed(@PathVariable("reviewId") @Valid long reviewId,
                                 @ModelAttribute("reviewForm") @Valid ReviewForm reviewForm,
                                 BindingResult result){
        if (result.hasErrors()){
            return "redirect:/reviews/" + reviewId;
        }
        reviewService.create(userService.currentUser().getId(), reviewId, reviewForm.getContent());
        return "redirect:/reviews/" + reviewId;
    }
}
