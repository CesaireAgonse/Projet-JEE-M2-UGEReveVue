package fr.uge.revevue.information.review;

import fr.uge.revevue.entity.ReviewContent;
import fr.uge.revevue.information.user.SimpleUserInformation;

import java.util.Date;
import java.util.Objects;

public record ReviewContentInformation(String content, String codeSelection, SimpleUserInformation userInformation, Date date) {

    public static ReviewContentInformation from(ReviewContent reviewContent){
        Objects.requireNonNull(reviewContent, "[ReviewContentInformation] reviewContent is null");
        return new ReviewContentInformation(
                reviewContent.getContent(),
                reviewContent.getCodeSelection(),
                SimpleUserInformation.from(reviewContent.getUser()),
                reviewContent.getDate()
        );
    }
}
