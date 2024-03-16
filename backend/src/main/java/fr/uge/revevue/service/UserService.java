package fr.uge.revevue.service;

import fr.uge.revevue.entity.Post;
import fr.uge.revevue.information.code.CodeInformation;
import fr.uge.revevue.information.code.CodePageInformation;
import fr.uge.revevue.information.code.FilterInformation;
import fr.uge.revevue.information.comment.CommentPageInformation;
import fr.uge.revevue.information.review.ReviewContentPageInformation;
import fr.uge.revevue.information.review.ReviewPageInformation;
import fr.uge.revevue.information.user.SimpleUserInformation;
import fr.uge.revevue.information.user.UserInformation;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.information.user.UserPageInformation;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService{
    private static final int LIMIT_FOLLOWED_PAGE = 10;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PostRepository postRepository;
    private final CodeService codeService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       PostRepository postRepository,
                       CodeService codeService, ReviewService reviewService, CommentService commentService){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.postRepository = postRepository;
        this.codeService = codeService;
        this.reviewService = reviewService;
        this.commentService = commentService;
    }

    @Transactional
    public SimpleUserInformation modifyPassword(String currentPassword, String newPassword){
        var user = currentUser();
        if (user == null){
            throw new IllegalStateException("User not found");
        }
        if(!matchesPassword(currentPassword, user.getPassword())){
            throw new IllegalArgumentException("Current Password are not the same");
        }
        var passwordCrypt = bCryptPasswordEncoder.encode(newPassword);
        userRepository.update(user.getUsername(), passwordCrypt);
        return SimpleUserInformation.from(user);
    }

    @Transactional
    public void follow(String followerUsername, String followedUsername){
        if (followerUsername.equals(followedUsername)){
            throw new IllegalStateException("follower and followed are the same user");
        }
        var optionalFollowedUser = userRepository.findByUsername(followedUsername);
        if (optionalFollowedUser.isEmpty()){
            throw new IllegalStateException("user follower not found");
        }
        var optionalFollowerUser = userRepository.findByUsername(followerUsername);
        if (optionalFollowerUser.isEmpty()){
            throw new IllegalStateException("user followed not found");
        }
        var followedUser = optionalFollowedUser.get();
        var followerUser = optionalFollowerUser.get();
        followerUser.getFollowed().add(followedUser);
    }

    @Transactional
    public void unfollow(String followerUsername, String followedUsername){
        if (followerUsername.equals(followedUsername)){
            throw new IllegalStateException("follower and followed are the same user");
        }
        var optionalFollowedUser = userRepository.findByUsername(followedUsername);
        if (optionalFollowedUser.isEmpty()){
            throw new IllegalStateException("user follower not found");
        }
        var optionalFollowerUser = userRepository.findByUsername(followerUsername);
        if (optionalFollowerUser.isEmpty()){
            throw new IllegalStateException("user followed not found");
        }
        var followedUser = optionalFollowedUser.get();
        var followerUser = optionalFollowerUser.get();
        followerUser.getFollowed().remove(followedUser);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("user not found");
        }
        return user.get();
    }

    public User currentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            var principal = authentication.getPrincipal();
            if (principal instanceof User){
                return (User) principal;
            }
        }
       return null;
    }

    public boolean isExisted(String username){
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findUserByName(String username){
        return userRepository.findByUsername(username);
    }

    @Transactional
    public UserInformation getInformation(String username){
        var optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(UserInformation::from).orElse(null);
    }

    @Transactional
    public List<UserInformation> getSomeUsers(int offset, int limit){
        Pageable page = PageRequest.of(offset, limit);
        return userRepository.findAll(page).stream().map(UserInformation::from).toList();
    }

    @Transactional
    public UserPageInformation getSomeUsersForAdminPage(int offset, int limit){
        var count = userRepository.count();
        int maxPageNumber = (int) ((count - 1) / limit);
        return new UserPageInformation(getSomeUsers(offset, limit),  offset, maxPageNumber);
    }
    @Transactional
    public UserPageInformation getFollowedPageFromUsername(String username, int offset){
        var count = userRepository.countUserFollowedByUsername(username);
        int maxPageNumber = ((count - 1) / LIMIT_FOLLOWED_PAGE);
        Pageable page = PageRequest.of(offset, LIMIT_FOLLOWED_PAGE);
        var followedInformations = userRepository.findUserFollowedByUsername(username, page).stream().map(UserInformation::from).toList();
        return new UserPageInformation(followedInformations, offset, maxPageNumber);
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword){
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    public UserInformation delete(String username){
        var userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }
        var user = userOptional.get();
        userRepository.delete(user);
        return UserInformation.from(user);
    }

    @Transactional
    public FilterInformation filter(String sortBy, String query, Integer pageNumber){
        var user = currentUser();
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        List<CodeInformation> codes;
        var count = codeService.countCodeWithQuery(query);
        int maxPageNumber = (count - 1) / CodeService.LIMIT;
        switch (sortBy != null ? sortBy : "") {
            // Display all codes by newest
            case "newest" -> {
                codes = codeService.findWithKeywordByNewest(query, pageNumber, CodeService.LIMIT);
            }
            // Display all codes by relevance
            case "relevance"-> {
                codes = codeService.findWithKeywordByScore(query, pageNumber, CodeService.LIMIT);
            }
            default -> {
                if(user != null) {
                    // Display codes from follows
                    codes = codeService.getCodeFromFollowed(user, query, pageNumber, CodeService.LIMIT);
                }
                else {
                    // Display all codes
                    codes = codeService.findWithKeyword(query, pageNumber, CodeService.LIMIT);
                }
            }
        }
        return new FilterInformation(codes, sortBy, query, pageNumber, maxPageNumber, count);
    }

    public CodePageInformation codes(String username, Integer pageNumber) {
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        return codeService.getCodePageFromUsername(username, pageNumber);
    }

    public ReviewPageInformation reviews(String username, Integer pageNumber) {
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        return reviewService.getReviewPageFromUsername(username,pageNumber);
    }

    public CommentPageInformation comments(String username, Integer pageNumber) {
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        return commentService.getCommentPageFromUsername(username, pageNumber);
    }

    @Transactional
    public UserPageInformation users(String username, Integer pageNumber) {
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        return getFollowedPageFromUsername(username, pageNumber);
    }

    public ReviewContentPageInformation reviewsContents(String username, Integer pageNumber) {
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        return reviewService.getReviewContentPageFromUsername(username, pageNumber);
    }

    @Transactional
    public void changePhoto(byte[] photo) {
        var user = currentUser();
        if (user == null){
            throw new IllegalStateException("User not found");
        }
        System.out.println(photo);
        userRepository.changePhoto(user.getUsername(), photo);
        System.out.println("TEST");
    }

}


