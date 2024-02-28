package fr.uge.revevue.controller.rest;

import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.form.ReviewForm;
import fr.uge.revevue.information.*;
import fr.uge.revevue.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @PostMapping("/vote/{postId}")
    public ResponseEntity<Long> postVoted(@PathVariable("postId") @Valid long postId,
                                             @RequestParam("voteType") String voteType) {
        return ResponseEntity.ok(voteService.postVoted(userService.currentUser().getId(), postId, Vote.VoteType.valueOf(voteType)));
    }

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

    @PostMapping("/review/{postId}")
    public ResponseEntity<Void> postReviewed(@PathVariable("postId") @Valid long postId,
                               @RequestBody @Valid ReviewForm reviewForm,
                               BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.notFound().build();
        }
        reviewService.create(userService.currentUser().getId(),postId,reviewForm.getContent());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<CommentPageInformation> comments(@PathVariable("postId") @Valid long postId,
                                                           @RequestParam(value = "pageNumber", required = false) int pageNumber) {
        return ResponseEntity.ok(commentService.getComments(postId, pageNumber));
    }

    @GetMapping("/reviews/{postId}")
    public ResponseEntity<ReviewPageInformation> reviews(@PathVariable("postId") @Valid long postId,
                                                          @RequestParam(value = "pageNumber", required = false) int pageNumber) {
        return ResponseEntity.ok(reviewService.getReviews(postId, pageNumber));
    }
}
