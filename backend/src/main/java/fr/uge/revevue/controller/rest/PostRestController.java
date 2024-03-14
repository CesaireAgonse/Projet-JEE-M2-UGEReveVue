package fr.uge.revevue.controller.rest;

import fr.uge.revevue.entity.Comment;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
public class PostRestController {
    private final UserService userService;
    private final CodeService codeService;
    private final VoteService voteService;
    private final CommentService commentService;
    private final ReviewService reviewService;

    @Autowired
    public PostRestController(CodeService codeService, UserService userService, VoteService voteService,CommentService commentService,ReviewService reviewService){
        this.userService = userService;
        this.codeService = codeService;
        this.voteService = voteService;
        this.commentService = commentService;
        this.reviewService = reviewService;
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/vote/{postId}")
    public ResponseEntity<Long> postVoted(@PathVariable("postId") @Valid long postId,
                                             @RequestParam("voteType") String voteType) {
        return ResponseEntity.ok(voteService.postVoted(userService.currentUser().getId(), postId, Vote.VoteType.valueOf(voteType)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/{postId}")
    public ResponseEntity<Void> postCommented(@PathVariable("postId") @Valid long postId,
                                @RequestBody @Valid CommentForm commentForm,
                                BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.notFound().build();
        }
        commentService.postCommented(userService.currentUser().getId(),postId,commentForm.getContent(), commentForm.getCodeSelection());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/review/{postId}")
    public ResponseEntity<Void> postReviewed(@PathVariable("postId") @Valid long postId,
                               @RequestBody @Valid ReviewForm reviewForm,
                               BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.notFound().build();
        }
        reviewService.create(userService.currentUser().getId(), postId, reviewForm.getTitle(), reviewForm.getContent());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/comments/{postId}")
    public ResponseEntity<CommentPageInformation> comments(@PathVariable("postId") @Valid long postId,
                                                           @RequestParam(value = "pageNumber", required = false) int pageNumber) {
        return ResponseEntity.ok(commentService.getComments(postId, pageNumber));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/reviews/{postId}")
    public ResponseEntity<ReviewPageInformation> reviews(@PathVariable("postId") @Valid long postId,
                                                         @RequestParam(value = "pageNumber", required = false) int pageNumber) {
        return ResponseEntity.ok(reviewService.getReviews(postId, pageNumber));
    }
}
