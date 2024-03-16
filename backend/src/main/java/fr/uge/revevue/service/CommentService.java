package fr.uge.revevue.service;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.information.comment.CommentInformation;
import fr.uge.revevue.information.comment.CommentPageInformation;
import fr.uge.revevue.information.review.ReviewInformation;
import fr.uge.revevue.information.review.ReviewPageInformation;
import fr.uge.revevue.information.user.UserInformation;
import fr.uge.revevue.repository.CommentRepository;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private static int LIMIT_COMMENT_PAGE = 4;
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
    public CommentPageInformation getComments(long postId, Integer pageNumber){
        if (pageNumber == null || pageNumber < 0){
            pageNumber = 0;
        }
        var count = commentRepository.countByPostId(postId);
        int maxPageNumber = ((count - 1) / LIMIT_COMMENT_PAGE);
        Pageable pageable = PageRequest.of(pageNumber, LIMIT_COMMENT_PAGE);
        var comments = commentRepository.findByPostIdOrderByDateDesc(pageable, postId).stream().map(CommentInformation::from).toList();
        return new CommentPageInformation(comments, pageNumber, maxPageNumber);
    }

    @Transactional
    public long countCommentsFromUser(UserInformation user){
        var realUser = userRepository.findByUsername(user.username());
        return commentRepository.countByUserId(realUser.get().getId());
    }

    public boolean isExisted(long id){
        return commentRepository.existsById(id);
    }

    @Transactional
    public CommentInformation delete (long reviewId){
        var comment = commentRepository.findById(reviewId);
        if(comment.isEmpty()){
            throw new IllegalArgumentException("Comment not found");
        }
        commentRepository.delete(comment.get());
        return CommentInformation.from(comment.get());
    }

    @Transactional
    public CommentPageInformation getCommentPageFromUsername(String username, int offset){
        var count = commentRepository.countByUserUsername(username);
        int maxPageNumber = ((count - 1) / LIMIT_COMMENT_PAGE);
        Pageable page = PageRequest.of(offset, LIMIT_COMMENT_PAGE);
        var commentInformations = commentRepository.findAllByUserUsername(username, page).stream().map(CommentInformation::from).toList();
        return new CommentPageInformation(commentInformations, offset, maxPageNumber);
    }
}
