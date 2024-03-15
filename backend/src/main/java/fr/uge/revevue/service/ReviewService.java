package fr.uge.revevue.service;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.entity.Review;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.information.code.CodeInformation;
import fr.uge.revevue.information.code.CodePageInformation;
import fr.uge.revevue.information.review.ReviewInformation;
import fr.uge.revevue.information.review.ReviewPageInformation;
import fr.uge.revevue.information.user.UserInformation;
import fr.uge.revevue.repository.CommentRepository;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.ReviewRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    private static int LIMIT_REVIEW_PAGE = 4;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ReviewService(ReviewRepository reviewRepository, PostRepository postRepository,UserRepository userRepository, CommentRepository commentRepository) {
        this.reviewRepository = reviewRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }
    @Transactional
    public void create(long userId, long postId, String title, List<CommentForm> commentForms){
        var findUser = userRepository.findById(userId);
        if (findUser.isEmpty()){
            throw new IllegalStateException("User not found");
        }
        var findPost = postRepository.findById(postId);
        if (findPost.isEmpty()){
            throw new IllegalStateException("Post not found");
        }
        var user = findUser.get();
        var post = findPost.get();
        List<Comment> content = new ArrayList<>();
        if (commentForms != null) {
            for (var contentForm : commentForms) {
                var comment = new Comment(contentForm.getContent(), user, null);
                comment.setCodeSelection(contentForm.getCodeSelection());
                content.add(comment);
                commentRepository.save(comment);
            }
        }
        var review = new Review(title, content, user, post);
        reviewRepository.save(review);
    }

    @Transactional
    public ReviewInformation getInformation(long reviewId){
        var review = reviewRepository.findById(reviewId);
        if(review.isEmpty()){
            throw new IllegalArgumentException("Review not found");
        }
        return ReviewInformation.from(review.get());
    }

    @Transactional
    public ReviewPageInformation getReviews(long postId, int page){
        if (page < 0){
            page = 0;
        }
        var count = reviewRepository.countByPostId(postId);
        int maxPageNumber = (int) ((count - 1) / LIMIT_REVIEW_PAGE);
        Pageable pageable = PageRequest.of(page, LIMIT_REVIEW_PAGE);
        var reviews = reviewRepository.findByPostIdOrderByDateDesc(pageable, postId).stream().map(ReviewInformation::from).toList();
        return new ReviewPageInformation(reviews, page, maxPageNumber);
    }

    public boolean isExisted(long id){
        return reviewRepository.existsById(id);
    }

    @Transactional
    public ReviewInformation delete (long reviewId){
        var review = reviewRepository.findById(reviewId);
        if(review.isEmpty()){
            throw new IllegalArgumentException("Review not found");
        }
        reviewRepository.delete(review.get());
        return ReviewInformation.from(review.get());
    }

    @Transactional
    public ReviewPageInformation getReviewPageFromUsername(String username, int offset){
        var count = reviewRepository.countByUserUsername(username);
        int maxPageNumber = (int) ((count - 1) / LIMIT_REVIEW_PAGE);
        Pageable page = PageRequest.of(offset, LIMIT_REVIEW_PAGE);
        var reviewInformations = reviewRepository.findAllByUserUsername(username, page).stream().map(ReviewInformation::from).toList();
        return new ReviewPageInformation(reviewInformations, offset, maxPageNumber);
    }

    @Transactional
    public long countReviewsFromUser(UserInformation user){
        var realUser = userRepository.findByUsername(user.username());
        return reviewRepository.countByUserId(realUser.get().getId());
    }

}
