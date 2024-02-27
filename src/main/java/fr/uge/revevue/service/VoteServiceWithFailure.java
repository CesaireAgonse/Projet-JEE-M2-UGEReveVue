package fr.uge.revevue.service;

import fr.uge.revevue.entity.Post;
import fr.uge.revevue.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteServiceWithFailure {
  private VoteRepository voteRepository;
  
  public VoteServiceWithFailure(VoteRepository voteRepository) {
    this.voteRepository = voteRepository;
  }
  
  @Transactional
  public void incrementScoreWrong(Post post, int value){
    post.setScore(post.getScore() + value);
  }
}
