package fr.uge.revevue.service;


import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.Post;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.repository.CodeRepository;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import fr.uge.revevue.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

@Service
public class VoteService {
    private VoteRepository voteRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private VoteServiceWithFailure voteServiceWithFailure;

    @Autowired
    public VoteService(VoteRepository voteRepository,
                       PostRepository postRepository,
                       UserRepository userRepository,
                       VoteServiceWithFailure voteServiceWithFailure){
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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
