package fr.uge.revevue.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Reviews")
public class Review extends Post{

    private String content;
    @ManyToOne(fetch= FetchType.LAZY)
    private Post post;


    @OneToMany(fetch= FetchType.EAGER,mappedBy = "post")
    private Set<Review> reviews;

    public Review( String content,User user, Post post) {
        super(user);
        this.content = content;
        this.post = post;
    }
    public Review(){

    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
