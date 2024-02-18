package fr.uge.revevue.service;


import fr.uge.revevue.entity.Code;
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
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository,
                       PostRepository postRepository,
                       UserRepository userRepository){
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public int postVoted(long userId, long postId, Vote.VoteType voteType){
        var findUser = userRepository.findById(userId);
        if (findUser.isEmpty()){
            throw new IllegalStateException("User not found");
        }
        var findPost = postRepository.findById(postId);
        if (findPost.isEmpty()){
            throw new IllegalStateException("Post not found");
        }
        var user = findUser.get();
        var post = findPost.get();
        var vote = voteRepository.findByPostAndUser(user, post);
        if (vote == null){
            var newVote = new Vote(user, post, voteType);
            post.getVotes().add(newVote);
            voteRepository.save(newVote);
            return post.getScoreVote();
        }
        else if (vote.getVoteType() != voteType){
            vote.setVoteType(voteType);
            return post.getScoreVote();
        }
        post.getVotes().remove(vote);
        voteRepository.delete(vote);
        return post.getScoreVote();
    }
}
