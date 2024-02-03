package fr.uge.revevue.information;

import fr.uge.revevue.entity.Code;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record CodeInformation(
        long id,
        UserInformation userInformation,
        String title,
        String description,
        String javaContent,
        String unitContent,
        int score,
        Date date,
        Set<CommentInformation> comments,
        Set<ReviewInformation> reviews
) {

    public static CodeInformation from(Code code){
        Objects.requireNonNull(code, "[CodeInformation] code is null");
        return new CodeInformation(
                code.getId(),
                UserInformation.from(code.getUser()),
                code.getTitle(),
                code.getDescription(),
                code.getJavaContent(),
                code.getUnitContent(),
                code.getScoreVote(),
                code.getDate(),
                code.getComments().stream().map(CommentInformation::from).collect(Collectors.toSet()),
                code.getReviews().stream().map(ReviewInformation::from).collect(Collectors.toSet())
        );
    }
}

