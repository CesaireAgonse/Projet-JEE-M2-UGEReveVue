package fr.uge.revevue.service;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.repository.CommentRepository;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    private PostRepository postRepository;
    private UserRepository userRepository;
    public CommentService(CommentRepository commentRepository, PostRepository postRepository,UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void codeCommented(long userId,long postId,String content){
        var findUser = userRepository.findById(userId);
        if (findUser.isEmpty()){
            throw new IllegalStateException("User not found");
        }
        var findCode = postRepository.findById(postId);
        if (findCode.isEmpty()){
            throw new IllegalStateException("Code not found");
        }
        var user = findUser.get();
        var post = findCode.get();
        commentRepository.save(new Comment(content,user,post));
    }
}
