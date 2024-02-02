package fr.uge.revevue.entityId;

import java.io.Serializable;

public class CommentId implements Serializable {


    private Long user;
    private Long post;

    public CommentId() {
    }

    public CommentId(Long user, Long post) {
        this.user = user;
        this.post = post;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getPost() {
        return post;
    }

    public void setPost(Long post) {
        this.post = post;
    }
}
