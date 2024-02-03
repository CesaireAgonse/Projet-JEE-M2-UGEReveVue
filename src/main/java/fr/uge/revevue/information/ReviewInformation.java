package fr.uge.revevue.information;

import fr.uge.revevue.entity.Review;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record ReviewInformation(
        long id,
        UserInformation userInformation,
        String content,
        int score,
        Date date,
        Set<CommentInformation> comments,
        Set<ReviewInformation> reviews
) {

    public static ReviewInformation from(Review review){
        Objects.requireNonNull(review, "[ReviewInformation] review is null");
        return new ReviewInformation(
                review.getId(),
                UserInformation.from(review.getUser()),
                review.getContent(),
                review.getScoreVote(),
                review.getDate(),
                review.getComments().stream().map(CommentInformation::from).collect(Collectors.toSet()),
                review.getReviews().stream().map(ReviewInformation::from).collect(Collectors.toSet())
        );
    }
}
