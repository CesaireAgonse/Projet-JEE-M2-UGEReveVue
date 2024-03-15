package fr.uge.revevue.entityId;

import java.io.Serializable;
import java.util.Objects;

public class VoteId implements Serializable {
    private Long user;
    private Long post;

    public VoteId() {}

    public VoteId(Long user, Long post) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteId voteId = (VoteId) o;
        return Objects.equals(user, voteId.user) && Objects.equals(post, voteId.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, post);
    }
}