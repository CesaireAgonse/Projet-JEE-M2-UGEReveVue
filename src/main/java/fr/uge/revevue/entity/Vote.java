package fr.uge.revevue.entity;

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
    private Code code;

    public enum VoteType{
        DownVote,
        NotVoted,
        UpVote
    }

    private VoteType voteType;

    public Vote(){}

    public Vote(User user, Code code, VoteType voteType) {
        this.user = user;
        this.code = code;
        this.voteType = voteType;
    }

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public Code getCode() {return code;}

    public void setCode(Code code) {this.code = code;}

    public VoteType getVoteType() {return voteType;}

    public void setVoteType(VoteType voteType) {this.voteType = voteType;}

    public int getScore(){
        // retournera -1 ou 1 car l'enum de vote commence à partir de 0
        return this.getVoteType().ordinal() - 1;
    }
}