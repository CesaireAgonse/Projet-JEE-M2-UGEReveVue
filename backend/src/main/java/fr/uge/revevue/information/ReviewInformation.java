package fr.uge.revevue.information;

import fr.uge.revevue.entity.Review;
import fr.uge.revevue.entity.Vote;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record ReviewInformation(
        long id,
        SimpleUserInformation userInformation,
        Vote.VoteType voteType,
        String title,
        String content,
        long score,
        Date date,
        List<CommentInformation> comments,
        List<ReviewInformation> reviews
) {

    public static ReviewInformation from(Review review){
        Objects.requireNonNull(review, "[ReviewInformation] review is null");
        return new ReviewInformation(
                review.getId(),
                SimpleUserInformation.from(review.getUser()),
                review.getVoteUser(),
                review.getTitle(),
                review.getContent(),
                review.getScore(),
                review.getDate(),
                review.getComments().stream().map(CommentInformation::from).sorted(Comparator.comparing(CommentInformation::date).reversed()).toList(),
                review.getReviews().stream().map(ReviewInformation::from)
                        .sorted(Comparator.comparing(ReviewInformation::date).reversed())
                        .sorted(Comparator.comparing(ReviewInformation::score).reversed()).toList()
        );
    }
}
