package fr.uge.revevue.service;

import fr.uge.revevue.entity.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private final VoteServiceWithFailure voteServiceWithFailure;

    @Autowired
    public VoteService(VoteServiceWithFailure voteServiceWithFailure){
        this.voteServiceWithFailure = voteServiceWithFailure;
    }

    public long postVotedWithOptimisticLock(long userId, long postId, Vote.VoteType voteType){
        var retry=true;
        long vote = 0;
        while(retry) {
            retry=false;
            try {
                vote = voteServiceWithFailure.incrementScoreWrong(userId, postId, voteType);
            } catch (org.springframework.orm.ObjectOptimisticLockingFailureException e){
                retry=true;
            }
        }
        return vote;
    }
}
