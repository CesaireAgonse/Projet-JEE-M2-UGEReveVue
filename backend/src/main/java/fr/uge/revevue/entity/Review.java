package fr.uge.revevue.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Reviews")
public class Review extends Post{

    @ManyToOne(fetch= FetchType.LAZY)
    private Post post;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewContent> contents;

    public Review(String title, List<ReviewContent> contents, User user, Post post) {
        super(title, user);
        this.post = post;
        this.contents = contents;
    }

    public Review(){}

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<ReviewContent> getContent() {
        return contents;
    }

    public void setContent(List<ReviewContent> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "Review{" +
                "post=" + post +
                ", contents=" + contents +
                ", votes=" + votes +
                ", comments=" + comments +
                ", reviews=" + reviews +
                '}';
    }
}
