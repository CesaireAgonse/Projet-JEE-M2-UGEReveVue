package fr.uge.revevue.information;

import fr.uge.revevue.entity.Comment;

import java.util.Objects;

public record CommentInformation(UserInformation userInformation, String content) {

    public static CommentInformation from(Comment comment){
        Objects.requireNonNull(comment, "[CommentInformation] code is null");
        return new CommentInformation(UserInformation.from(comment.getUser()), comment.getContent());
    }
}
