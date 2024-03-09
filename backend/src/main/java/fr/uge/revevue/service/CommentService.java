package fr.uge.revevue.service;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.information.CommentInformation;
import fr.uge.revevue.information.CommentPageInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.repository.CommentRepository;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CommentService {
    private static int LIMIT_COMMENT_PAGE = 3;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository,UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void postCommented(long userId, long postId, String content, String codeSelection){
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
        comment.setCodeSelection(codeSelection);
        commentRepository.save(comment);
    }
    @Transactional
    public CommentPageInformation getComments(long postId, int page){
        if (page < 0){
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_COMMENT_PAGE);
        var comments = commentRepository.findByPostId(pageable, postId).stream().map(CommentInformation::from).toList();
        return new CommentPageInformation(comments, page);
    }

    @Transactional
    public long countCommentsFromUser(UserInformation user){
        var realUser = userRepository.findByUsername(user.username());
        return commentRepository.countByUserId(realUser.get().getId());
    }
}
