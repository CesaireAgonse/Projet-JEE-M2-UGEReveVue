package fr.uge.revevue.service;

import fr.uge.revevue.entity.Post;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import fr.uge.revevue.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteServiceWithFailure {
  private final VoteRepository voteRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  
  public VoteServiceWithFailure(VoteRepository voteRepository, UserRepository userRepository, PostRepository postRepository) {
    this.userRepository = userRepository;
    this.voteRepository = voteRepository;
    this.postRepository = postRepository;
  }
  
  @Transactional
  public long incrementScoreWrong(long userId, long postId, Vote.VoteType voteType){
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
      post.setScore(post.getScore() + voteType.ordinal() - 1);
      return post.getScore();
    }
    else if (vote.getVoteType() != voteType){
      vote.setVoteType(voteType);
      post.setScore(post.getScore() + 2 * (voteType.ordinal() - 1));
      return post.getScore();
    }
    post.getVotes().remove(vote);
    voteRepository.delete(vote);
    post.setScore(post.getScore() - (voteType.ordinal() - 1));
    return post.getScore();
  }
}
