package fr.uge.revevue.entity;

import org.hibernate.annotations.Entity;
import org.springframework.data.annotation.Id;

import javax.persistence.FetchType;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
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
}