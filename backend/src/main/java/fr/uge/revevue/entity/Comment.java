package fr.uge.revevue.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @NotBlank
    @Column(columnDefinition = "VARCHAR(MAX)")
    private String content;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String codeSelection;

    private Date date;

    public Comment() {}

    public Comment(String content, User user,Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.date = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCodeSelection() {
        return codeSelection;
    }

    public void setCodeSelection(String codeSelection) {
        this.codeSelection = codeSelection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", post=" + post +
                ", content='" + content + '\'' +
                ", codeSelection='" + codeSelection + '\'' +
                ", date=" + date +
                '}';
    }
}
