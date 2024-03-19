package fr.uge.revevue.controller.rest;

import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.form.ReviewForm;
import fr.uge.revevue.information.comment.CommentPageInformation;
import fr.uge.revevue.information.review.ReviewPageInformation;
import fr.uge.revevue.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/posts")
public class PostRestController {
    private final UserService userService;
    private final VoteService voteService;
    private final CommentService commentService;
    private final ReviewService reviewService;
    private final PostService postService;

    @Autowired
    public PostRestController(UserService userService, VoteService voteService, CommentService commentService, ReviewService reviewService, PostService postService){
        this.userService = userService;
        this.voteService = voteService;
        this.commentService = commentService;
        this.reviewService = reviewService;
        this.postService = postService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/comments/{postId}")
    public ResponseEntity<CommentPageInformation> getComments(@PathVariable("postId") long postId,
                                                              @RequestParam(value = "pageNumber", required = false) int pageNumber) {
        if (!postService.isExisted(postId)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commentService.getComments(postId, pageNumber));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/reviews/{postId}")
    public ResponseEntity<ReviewPageInformation> getReviews(@PathVariable("postId") long postId,
                                                            @RequestParam(value = "pageNumber", required = false) int pageNumber,
                                                            @RequestParam(value = "sortBy", required = false, defaultValue = "newest") String sortBy) {
        if (!postService.isExisted(postId)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviewService.getReviews(postId, sortBy, pageNumber));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/vote/{postId}")
    public ResponseEntity<Long> postVoted(@PathVariable("postId") long postId,
                                          @RequestParam("voteType") String voteType) {
        if (!postService.isExisted(postId)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(voteService.postVotedWithOptimisticLock(userService.currentUser().getId(), postId, Vote.VoteType.valueOf(voteType)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/{postId}")
    public ResponseEntity<Void> postCommented(@PathVariable("postId") long postId,
                                              @RequestBody @Valid CommentForm commentForm,
                                              BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        if (!postService.isExisted(postId)){
            return ResponseEntity.notFound().build();
        }
        commentService.postCommented(userService.currentUser().getId(), postId, commentForm.getContent(), commentForm.getCodeSelection());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/review/{postId}")
    public ResponseEntity<Void> postReviewed(@PathVariable("postId") long postId,
                                             @RequestBody @Valid ReviewForm reviewForm,
                                             BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        if (!postService.isExisted(postId)){
            return ResponseEntity.notFound().build();
        }
        reviewService.create(postId, reviewForm.getTitle(), reviewForm.getContent());
        return ResponseEntity.ok().build();
    }
}
