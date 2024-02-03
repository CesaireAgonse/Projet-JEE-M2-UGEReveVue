package fr.uge.revevue.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "Reviews")
public class Review extends Post{

    @ManyToOne(fetch= FetchType.LAZY)
    private Post post;

    @NotBlank
    @Column(columnDefinition = "VARCHAR(MAX)")
    private String content;

    public Review( String content,User user, Post post) {
        super(user);
        this.post = post;
        this.content = content;
    }

    public Review(){}

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
