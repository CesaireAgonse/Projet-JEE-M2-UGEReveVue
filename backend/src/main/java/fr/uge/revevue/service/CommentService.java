package fr.uge.revevue.service;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.information.comment.CommentInformation;
import fr.uge.revevue.information.comment.CommentPageInformation;
import fr.uge.revevue.repository.CommentRepository;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CommentService {
    private static final int LIMIT_COMMENT_PAGE = 3;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Transactional
    public void postCommented(long postId, String content, String codeSelection){
       var user = userService.currentUser();
        if (user == null){
            throw new IllegalStateException("user not logged");
        }
        var findPost = postRepository.findById(postId);
        if (findPost.isEmpty()){
            throw new IllegalStateException("post not found");
        }
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
        return new CommentPageInformation(comments, pageNumber, maxPageNumber, count);
    }

    public boolean isExisted(long id){
        return commentRepository.existsById(id);
    }

    @Transactional
    public CommentPageInformation comments(String username, Integer pageNumber) {
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        var count = commentRepository.countByUserUsername(username);
        int maxPageNumber = ((count - 1) / LIMIT_COMMENT_PAGE);
        Pageable page = PageRequest.of(pageNumber, LIMIT_COMMENT_PAGE);
        var commentInformations = commentRepository.findAllByUserUsername(username, page).stream().map(CommentInformation::from).toList();
        return new CommentPageInformation(commentInformations, pageNumber, maxPageNumber, count);
    }

    @Transactional
    public CommentInformation delete(long reviewId){
        var comment = commentRepository.findById(reviewId);
        if(comment.isEmpty()){
            throw new IllegalArgumentException("Comment not found");
        }
        commentRepository.delete(comment.get());
        return CommentInformation.from(comment.get());
    }
}
