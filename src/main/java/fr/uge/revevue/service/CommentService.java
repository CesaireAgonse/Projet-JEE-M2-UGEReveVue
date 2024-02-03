package fr.uge.revevue.service;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.repository.CommentRepository;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public CommentService(CommentRepository commentRepository, PostRepository postRepository,UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void postCommented(long userId, long postId, String content){
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
        var comment = new Comment(content, user, post);
        commentRepository.save(comment);
    }
}
