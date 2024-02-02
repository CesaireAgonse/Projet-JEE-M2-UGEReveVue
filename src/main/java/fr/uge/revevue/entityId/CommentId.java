package fr.uge.revevue.entityId;

import java.io.Serializable;

public class CommentId implements Serializable {


    private Long user;
    private Long content;

    public CommentId() {
    }

    public CommentId(Long user, Long content) {
        this.user = user;
        this.content = content;
    }
    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getContent() {
        return content;
    }

    public void setContent(Long content) {
        this.content = content;
    }


}
