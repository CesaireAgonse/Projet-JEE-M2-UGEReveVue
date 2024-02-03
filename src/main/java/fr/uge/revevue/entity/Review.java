package fr.uge.revevue.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Reviews")
public class Review extends Post{

    private String content;
    @ManyToOne(fetch= FetchType.LAZY)
    private Post post;

    public Review(User user, String content, Post post) {
        super(user);
        this.content = content;
        this.post = post;
    }
    public Review(){

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
