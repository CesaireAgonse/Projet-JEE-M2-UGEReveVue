package fr.uge.revevue.service;

import fr.uge.revevue.entity.Review;
import fr.uge.revevue.entity.ReviewContent;
import fr.uge.revevue.form.ReviewContentForm;
import fr.uge.revevue.information.review.*;
import fr.uge.revevue.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewService {
    private static final int LIMIT_REVIEW_PAGE = 4;
    private static final int LIMIT_REVIEW_CONTENT_PAGE = 5;

    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final ReviewContentRepository reviewContentRepository;
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepository, PostRepository postRepository, ReviewContentRepository reviewContentRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.postRepository = postRepository;
        this.reviewContentRepository = reviewContentRepository;
        this.userService = userService;
    }

    public boolean isExisted(long id){
        return reviewRepository.existsById(id);
    }

    @Transactional
    public void create(long postId, String title, List<ReviewContentForm> commentForms){
        var user = userService.currentUser();
        if (user == null){
            throw new IllegalStateException("user not logged");
        }

        var findPost = postRepository.findById(postId);
        if (findPost.isEmpty()){
            throw new IllegalStateException("post not found");
        }
        var post = findPost.get();
        List<ReviewContent> contents = new ArrayList<>();
        for (var contentForm : commentForms) {
           var content = reviewContentRepository.findByUserUsernameAndContentAndCodeSelection(user.getUsername(), contentForm.getContent(), contentForm.getCodeSelection());
           if (content.isPresent()){
               content.get().setDate(new Date());
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
        return ReviewInformation.from(review.get(), userService.currentUser());
    }

    @Transactional
    public ReviewPageInformation getReviews(long postId, String sortBy, Integer pageNumber){
        if (pageNumber == null || pageNumber < 0){
            pageNumber = 0;
        }
        var count = reviewRepository.countByPostId(postId);
        int maxPageNumber = ((count - 1) / LIMIT_REVIEW_PAGE);
        Pageable pageable = PageRequest.of(pageNumber, LIMIT_REVIEW_PAGE);
        if (sortBy != null && sortBy.contains("relevance")){
            var reviews = reviewRepository.findByPostIdOrderByScoreDesc(pageable, postId).stream().map(review -> ReviewInformation.from(review, userService.currentUser())).toList();
            return new ReviewPageInformation(reviews, sortBy, pageNumber, maxPageNumber, count);
        }
        var reviews = reviewRepository.findByPostIdOrderByDateDesc(pageable, postId).stream().map(review -> ReviewInformation.from(review, userService.currentUser())).toList();
        return new ReviewPageInformation(reviews, sortBy, pageNumber, maxPageNumber, count);
    }

    @Transactional
    public ReviewPageInformation reviews(String username, Integer pageNumber) {
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        var count = reviewRepository.countByUserUsername(username);
        int maxPageNumber = ((count - 1) / LIMIT_REVIEW_PAGE);
        Pageable page = PageRequest.of(pageNumber, LIMIT_REVIEW_PAGE);
        var reviewInformations = reviewRepository.findAllByUserUsername(username, page).stream().map(review -> ReviewInformation.from(review, userService.currentUser())).toList();
        return new ReviewPageInformation(reviewInformations, "newest", pageNumber, maxPageNumber, count);
    }

    @Transactional
    public ReviewContentPageInformation reviewsContents(String username, Integer pageNumber) {
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        var count = reviewContentRepository.countByUserUsername(username);
        int maxPageNumber = ((count - 1) / LIMIT_REVIEW_CONTENT_PAGE);
        Pageable page = PageRequest.of(pageNumber, LIMIT_REVIEW_CONTENT_PAGE);
        var reviewContentInformations = reviewContentRepository.findAllByUserUsernameOrderByDateDesc(username, page).stream().map(ReviewContentInformation::from).toList();
        return new ReviewContentPageInformation(reviewContentInformations, pageNumber, maxPageNumber);
    }

    @Transactional
    public ReviewInformation delete(long reviewId){
        var review = reviewRepository.findById(reviewId);
        if(review.isEmpty()){
            throw new IllegalArgumentException("Review not found");
        }
        reviewRepository.delete(review.get());
        return ReviewInformation.from(review.get(), userService.currentUser());
    }
}
