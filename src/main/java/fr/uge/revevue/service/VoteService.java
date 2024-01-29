package fr.uge.revevue.service;


import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.repository.CodeRepository;
import fr.uge.revevue.repository.UserRepository;
import fr.uge.revevue.repository.VoteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class VoteService {
    private VoteRepository voteRepository;
    private CodeRepository codeRepository;
    private UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, CodeRepository codeRepository, UserRepository userRepository){
        this.voteRepository = voteRepository;
        this.codeRepository = codeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public int codeVoted(long userId, long codeId, Vote.VoteType voteType){
        var findUser = userRepository.findById(userId);
        if (findUser.isEmpty()){
            throw new IllegalStateException("User not found");
        }
        var findCode = codeRepository.findById(codeId);
        if (findCode.isEmpty()){
            throw new IllegalStateException("Code not found");
        }
        var user = findUser.get();
        var code = findCode.get();
        var vote = voteRepository.findByCodeAndUser(user, code);
        if (vote == null){
            var newVote = new Vote(user, code, voteType);
            code.getVotes().add(newVote);
            voteRepository.save(newVote);
            return code.getScoreVote();
        }
        else if (vote.getVoteType() != voteType){
            vote.setVoteType(voteType);
        }
        else {
            voteRepository.delete(vote);
        }
        return code.getScoreVote();
    }


}
