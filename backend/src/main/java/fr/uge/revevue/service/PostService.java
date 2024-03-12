/*package fr.uge.revevue.service;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.entity.Review;
import fr.uge.revevue.information.review.ReviewInformation;
import fr.uge.revevue.repository.PostRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final ReviewService reviewService;

    public PostService(PostRepository postRepository, UserService userService, CommentService commentService, ReviewService reviewService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService = commentService;
        this.reviewService = reviewService;
    }

    @Transactional
    public void postCommented(long postId, String content, String codeSelection){
        var user = userService.currentUser();
        if (user == null){
            throw new IllegalStateException("user not logged");
        }
        var findPost = postRepository.findById(postId);
        if (findPost.isEmpty()){
            throw new IllegalStateException("post not found");
        }
        var post = findPost.get();
        var comment = new Comment(content, user, post);
        comment.setCodeSelection(codeSelection);
        commentService.create(comment);
    }

    @Transactional
    public void postReviewed(long postId, String content){
        var user = userService.currentUser();
        if (user == null){
            throw new IllegalStateException("user not logged");
        }
        var findPost = postRepository.findById(postId);
        if (findPost.isEmpty()){
            throw new IllegalStateException("post not found");
        }
        var post = findPost.get();
        var review = new Review(post.getTitle(), content, user, post);
        reviewService.create(review);
    }
}
*/