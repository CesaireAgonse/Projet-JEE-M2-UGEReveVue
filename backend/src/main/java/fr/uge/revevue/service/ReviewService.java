package fr.uge.revevue.service;

import fr.uge.revevue.entity.Review;
import fr.uge.revevue.entity.ReviewContent;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.information.review.ReviewInformation;
import fr.uge.revevue.information.review.ReviewPageInformation;
import fr.uge.revevue.information.user.UserInformation;
import fr.uge.revevue.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ReviewService {
    private static final int LIMIT_REVIEW_PAGE = 4;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReviewContentRepository reviewContentRepository;

    public ReviewService(ReviewRepository reviewRepository, PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, ReviewContentRepository reviewContentRepository) {
        this.reviewRepository = reviewRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.reviewContentRepository = reviewContentRepository;
    }
    @Transactional
    public void create(long userId, long postId, String title, List<CommentForm> commentForms){
        if (commentForms == null){
            throw new IllegalArgumentException("Comment Forms is null");
        }
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
        List<ReviewContent> contents = new ArrayList<>();
        for (var contentForm : commentForms) {
           var content = reviewContentRepository.findByUserUsernameAndContentAndCodeSelection(user.getUsername(), contentForm.getContent(), contentForm.getCodeSelection());
           if (content.isPresent()){
               contents.add(content.get());
           }else{
               var comment = new ReviewContent(contentForm.getContent(), contentForm.getCodeSelection(), user);
               contents.add(comment);
               reviewContentRepository.save(comment);
           }
        }
        var review = new Review(title, contents, user, post);
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
        int maxPageNumber = ((count - 1) / LIMIT_REVIEW_PAGE);
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
        int maxPageNumber = ((count - 1) / LIMIT_REVIEW_PAGE);
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
