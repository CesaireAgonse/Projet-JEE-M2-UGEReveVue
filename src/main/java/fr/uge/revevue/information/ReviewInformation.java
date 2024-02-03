package fr.uge.revevue.information;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.entity.Review;
import fr.uge.revevue.entity.Vote;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record ReviewInformation(long id, UserInformation userInformation, String content, Set<CommentInformation> comments,int votes,Set<ReviewInformation> reviews) {


    public static ReviewInformation from(Review review){
        Objects.requireNonNull(review, "[ReviewInformation] code is null");
        return new ReviewInformation(review.getId(),UserInformation.from(review.getUser()), review.getContent(),
                review.getComments().stream().map(CommentInformation::from).collect(Collectors.toSet()),review.getScoreVote(),
                review.getReviews().stream().map(ReviewInformation::from).collect(Collectors.toSet()));
    }
}
