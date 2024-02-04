package fr.uge.revevue.information;

import fr.uge.revevue.entity.Comment;

import java.util.Date;
import java.util.Objects;

public record CommentInformation(long id, UserInformation userInformation, String content, String codeSelection, Date date) {

    public static CommentInformation from(Comment comment){
        Objects.requireNonNull(comment, "[CommentInformation] comment is null");
        return new CommentInformation(
                comment.getId(),
                UserInformation.from(comment.getUser()),
                comment.getContent(),
                comment.getCodeSelection(),
                comment.getDate()
        );
    }
}
