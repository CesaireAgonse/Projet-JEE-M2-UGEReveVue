package fr.uge.revevue.entity;

import fr.uge.revevue.entityId.VoteId;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.*;

@Entity
@Table(name = "Votes")
@IdClass(VoteId.class)
public class Vote {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public enum VoteType{
        DownVote,
        NotVoted,
        UpVote
    }

    private VoteType voteType;

    public Vote(){}

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

    public Vote(User user, Post post, VoteType voteType) {
        this.user = user;
        this.post = post;
        this.voteType = voteType;
    }

    public VoteType getVoteType() {return voteType;}

    public void setVoteType(VoteType voteType) {this.voteType = voteType;}
}