package fr.uge.revevue.service;

import fr.uge.revevue.entity.Review;
import fr.uge.revevue.information.code.CodeInformation;
import fr.uge.revevue.information.code.CodePageInformation;
import fr.uge.revevue.information.review.ReviewInformation;
import fr.uge.revevue.information.review.ReviewPageInformation;
import fr.uge.revevue.information.user.UserInformation;
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
    private static int LIMIT_REVIEW_PAGE = 2;
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
        var reviews = reviewRepository.findByPostIdOrderByDateDesc(pageable, postId).stream().map(ReviewInformation::from).toList();
        return new ReviewPageInformation(reviews, page, 0);
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
    public List<ReviewInformation> getAllReviewsFromUserId(long userId){
        List<ReviewInformation> reviews = new ArrayList<>();
        var reviewsFromUser = reviewRepository.findAllByUserId(userId);
        for (var review : reviewsFromUser){
            reviews.add(ReviewInformation.from(review));
        }
        return reviews;
    }

    @Transactional
    public ReviewPageInformation getReviewPageFromUserId(long userId, int offset){
        var count = reviewRepository.countByUserId(userId);
        int maxPageNumber = (int) ((count - 1) / LIMIT_REVIEW_PAGE);
        Pageable page = PageRequest.of(offset, LIMIT_REVIEW_PAGE);
        var reviewInformations = reviewRepository.findAllByUserId(userId, page).stream().map(ReviewInformation::from).toList();
        return new ReviewPageInformation(reviewInformations, offset, maxPageNumber);
    }

    @Transactional
    public long countReviewsFromUser(UserInformation user){
        var realUser = userRepository.findByUsername(user.username());
        return reviewRepository.countByUserId(realUser.get().getId());
    }

}
