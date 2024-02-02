package fr.uge.revevue.entity;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.entity.Vote;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public abstract class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post")
    protected Set<Vote> votes = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post")
    protected Set<Comment> comments = new HashSet<>();

    @ManyToOne
    private User user;
    public Post(User user) {
        this.user = user;
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

    public int getScoreVote(){
        var score = 0;
        for (var vote : votes) {
            score += vote.getScore();
        }
        return score;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
