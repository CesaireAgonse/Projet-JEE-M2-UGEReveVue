package fr.uge.revevue.entity;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.entity.Vote;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public abstract class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected Set<Vote> votes = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected Set<Comment> comments = new HashSet<>();

    @ManyToOne
    private User user;
    
    private long score;

    private Date date;
    public Post(User user) {
        this.user = user;
        this.date = new Date();
    }

    public Post() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public long getScore() {
        return score;
    }
    
    public void setScore(long score) {
        this.score = score;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
