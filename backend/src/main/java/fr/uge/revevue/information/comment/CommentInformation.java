package fr.uge.revevue.information.comment;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.information.user.SimpleUserInformation;

import java.util.Date;
import java.util.Objects;

public record CommentInformation(long id, long idPost, boolean postIsCode, SimpleUserInformation userInformation, String content, String codeSelection, Date date) {

    public static CommentInformation from(Comment comment){
        Objects.requireNonNull(comment, "[CommentInformation] comment is null");
        var postIsCode = comment.getPost() instanceof Code;
        if (comment.getPost() == null){
            return new CommentInformation(
                    comment.getId(),
                    -1,
                    postIsCode,
                    SimpleUserInformation.from(comment.getUser()),
                    comment.getContent(),
                    comment.getCodeSelection(),
                    comment.getDate()
            );
        }
        return new CommentInformation(
                comment.getId(),
                comment.getPost().getId(),
                postIsCode,
                SimpleUserInformation.from(comment.getUser()),
                comment.getContent(),
                comment.getCodeSelection(),
                comment.getDate()
        );
    }
}
