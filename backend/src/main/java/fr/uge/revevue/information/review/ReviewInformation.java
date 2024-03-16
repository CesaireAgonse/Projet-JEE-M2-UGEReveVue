package fr.uge.revevue.information.review;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.Review;
import fr.uge.revevue.entity.ReviewContent;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.information.user.SimpleUserInformation;
import fr.uge.revevue.information.comment.CommentInformation;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public record ReviewInformation(
        long id,
        long idPost,
        boolean postIsCode,
        SimpleUserInformation userInformation,
        Vote.VoteType voteType,
        String title,
        List<ReviewContentInformation> content,
        long score,
        Date date,
        int comments,
        int reviews
) {

    public static ReviewInformation from(Review review){
        Objects.requireNonNull(review, "[ReviewInformation] review is null");
        var postIsCode = review.getPost() instanceof Code ;
        return new ReviewInformation(
                review.getId(),
                review.getPost().getId(),
                postIsCode,
                SimpleUserInformation.from(review.getUser()),
                review.getVoteUser(),
                review.getTitle(),
                review.getContent().stream().map(ReviewContentInformation::from).toList(),
                review.getScore(),
                review.getDate(),
                review.getComments().size(),
                review.getReviews().size()
        );
    }
}
