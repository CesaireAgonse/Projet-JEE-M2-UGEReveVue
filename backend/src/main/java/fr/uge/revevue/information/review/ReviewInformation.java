package fr.uge.revevue.information.review;

import fr.uge.revevue.entity.Review;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.information.user.SimpleUserInformation;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public record ReviewInformation(
        long id,
        long idPost,
        String typePost,
        SimpleUserInformation userInformation,
        Vote.VoteType voteType,
        String title,
        List<ReviewContentInformation> content,
        long score,
        Date date,
        int comments,
        int reviews
) {

    private static Vote.VoteType getVoteOfAuth(User auth, Review review){
        if (auth != null){
            return review.getVoteUser(auth.getUsername());
        }
        return Vote.VoteType.NotVoted;
    }

    public static ReviewInformation from(Review review, User auth){
        Objects.requireNonNull(review, "[ReviewInformation] review is null");
        return new ReviewInformation(
                review.getId(),
                review.getPost().getId(),
                review.getPost().getDtype(),
                SimpleUserInformation.from(review.getUser()),
                getVoteOfAuth(auth, review),
                review.getTitle(),
                review.getContent().stream().map(ReviewContentInformation::from).toList(),
                review.getScore(),
                review.getDate(),
                review.getComments().size(),
                review.getReviews().size()
        );
    }
}
