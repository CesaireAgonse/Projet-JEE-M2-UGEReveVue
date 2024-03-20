package fr.uge.revevue.controller;

import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.form.ReviewForm;
import fr.uge.revevue.information.user.AuthInformation;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;

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
    public String review(@PathVariable("reviewId") long reviewId,
                         @ModelAttribute("commentForm") CommentForm commentForm,
                         @ModelAttribute("reviewForm") ReviewForm reviewForm,
                         @RequestParam(value = "reviewPageNumber", required = false) Integer reviewPageNumber,
                         @RequestParam(value = "sortBy", required = false, defaultValue = "newest") String sortBy,
                         @RequestParam(value = "commentPageNumber", required = false) Integer commentPageNumber,
                         @RequestParam(value = "oldContentPageNumber", required = false) Integer oldContentPageNumber,
                         Model model){
        var user = userService.currentUser();
        if (user != null){
            model.addAttribute("auth", AuthInformation.from(user));
            model.addAttribute("oldContentsReview", reviewService.reviewsContents(user.getUsername(), oldContentPageNumber));
        }
        var review = reviewService.getInformation(reviewId);
        if (review == null){
            throw new IllegalStateException("review not found");
        }
        model.addAttribute("review", review);
        model.addAttribute("reviewPageInformation", reviewService.getReviews(review.id(), sortBy, reviewPageNumber));
        model.addAttribute("commentPageInformation", commentService.getComments(review.id(), commentPageNumber));
        return "reviews/reviewReview";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reviews/vote/{reviewId}")
    public String reviewVoted(@PathVariable("reviewId") long reviewId,
                              @RequestParam("voteType") String voteType,
                              HttpServletRequest request){
        if (!reviewService.isExisted(reviewId)){
            return "redirect:/";
        }
        voteService.postVotedWithOptimisticLock(userService.currentUser().getId(),reviewId,Vote.VoteType.valueOf(voteType));
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/reviews/" + reviewId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reviews/comment/{reviewId}")
    public String reviewCommented(@PathVariable("reviewId") long reviewId,
                                  @ModelAttribute("commentForm") @Valid CommentForm commentForm,
                                  BindingResult result){
        if (result.hasErrors()){
            return "redirect:/reviews/" + reviewId;
        }
        if (!reviewService.isExisted(reviewId)){
            return "redirect:/";
        }
        commentService.postCommented(userService.currentUser().getId(), reviewId, commentForm.getContent(), commentForm.getCodeSelection());
        return "redirect:/reviews/" + reviewId;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/reviews/review/{reviewId}")
    public String reviewReviewed(@PathVariable("reviewId") long reviewId,
                                 @ModelAttribute("reviewForm") @Valid ReviewForm reviewForm,
                                 BindingResult result){
        if (result.hasErrors()){
            return "redirect:/reviews/" + reviewId;
        }
        if (!reviewService.isExisted(reviewId)){
            return "redirect:/";
        }
        reviewService.create(reviewId,  reviewForm.getTitle(), reviewForm.getContent());
        return "redirect:/reviews/" + reviewId;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/reviews/delete/{reviewId}")
    public String delete(@PathVariable("reviewId") long reviewId,
                         HttpServletRequest request) {
        if (!reviewService.isExisted(reviewId)){
            return "redirect:/";
        }
        var reviewInformation = reviewService.getInformation(reviewId);
        reviewService.delete(reviewId);
        String referer = request.getHeader("Referer");
        if (referer != null){
            var split = Arrays.stream(referer.split("/")).toList();
            var number = Long.parseLong(split.get(split.size() - 1));
            if (number == reviewId){
                var type = reviewInformation.typePost();
                var id = reviewInformation.idPost();
                if (type.equals("Code")){
                    return "redirect:/codes/" + id;
                }
                if (type.equals("Review")){
                    return "redirect:/reviews/" + id;
                }
            } else {
                return "redirect:" + referer;
            }
        }
        return "redirect:/";
    }
}
