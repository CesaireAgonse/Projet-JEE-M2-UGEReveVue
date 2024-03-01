package fr.uge.revevue.service;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.entity.Review;
import fr.uge.revevue.information.*;
import fr.uge.revevue.repository.CommentRepository;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.ReviewRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ReviewService {
    private static int LIMIT_REVIEW_PAGE = 5;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, PostRepository postRepository,UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void create(long userId, long postId, String content){
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
        var review = new Review(post.getTitle(), content, user, post);

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
        Pageable pageable = PageRequest.of(page, LIMIT_REVIEW_PAGE);
        var reviews = reviewRepository.findByPostId(pageable, postId).stream().map(ReviewInformation::from).toList();
        return new ReviewPageInformation(reviews, page);
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
}
