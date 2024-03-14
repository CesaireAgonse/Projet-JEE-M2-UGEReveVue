package fr.uge.revevue.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "Reviews")
public class Review extends Post{

    @ManyToOne(fetch= FetchType.EAGER)
    private Post post;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> contents;

    public Review(String title, List<Comment> contents, User user, Post post) {
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

    public List<Comment> getContent() {
        return contents;
    }

    public void setContent(List<Comment> contents) {
        this.contents = contents;
    }
}
