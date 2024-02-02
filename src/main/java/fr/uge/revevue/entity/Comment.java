package fr.uge.revevue.entity;

import fr.uge.revevue.entityId.CommentId;
import fr.uge.revevue.entityId.VoteId;

import javax.persistence.*;

@Entity
@Table(name = "Comments")
@IdClass(CommentId.class)
public class Comment {

    private String content;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    public Comment() {
    }

    public Comment(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
