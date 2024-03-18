package fr.uge.revevue.service;

import fr.uge.revevue.information.comment.CommentPageInformation;
import fr.uge.revevue.information.review.ReviewContentPageInformation;
import fr.uge.revevue.information.review.ReviewPageInformation;
import fr.uge.revevue.information.user.SimpleUserInformation;
import fr.uge.revevue.information.user.UserInformation;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.information.user.UserPageInformation;
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
import java.util.Optional;

@Service
public class UserService implements UserDetailsService{
    private static final int LIMIT_FOLLOWED_PAGE = 10;
    private static final int LIMIT_USERS_PAGE = 5;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CommentService commentService;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       CommentService commentService){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.commentService = commentService;
    }

    @Transactional
    public void modifyPassword(String currentPassword, String newPassword){
        var user = currentUser();
        if (user == null){
            throw new IllegalStateException("User not found");
        }
        if(!matchesPassword(currentPassword, user.getPassword())){
            throw new IllegalArgumentException("Current Password are not the same");
        }
        var passwordCrypt = bCryptPasswordEncoder.encode(newPassword);
        userRepository.update(user.getUsername(), passwordCrypt);
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
        if (optionalUser.isEmpty()){
            return null;
        }
        boolean isFollowed;
        var auth = currentUser();
        if (auth != null){
            var optionalAuth = userRepository.findByUsername(auth.getUsername());
            isFollowed = optionalAuth.map(user -> user.getFollowed().contains(optionalUser.get())).orElse(false);
        }else{
            isFollowed = false;
        }
        return optionalUser.map(user -> UserInformation.from(user, isFollowed)).orElse(null);
    }

    @Transactional
    public UserPageInformation usersNonAdmin(Integer pageNumber){
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        var count = userRepository.countNonAdminUsers();
        int maxPageNumber = ((count - 1) / LIMIT_USERS_PAGE);
        Pageable page = PageRequest.of(pageNumber, LIMIT_USERS_PAGE);
        var users = userRepository.findAllNonAdminUsers(page).stream().map(SimpleUserInformation::from).toList();
        return new UserPageInformation(users,  pageNumber, maxPageNumber, count);
    }
    @Transactional
    public UserPageInformation getFollowedPageFromUsername(String username, int offset){
        var count = userRepository.countUserFollowedByUsername(username);
        int maxPageNumber = ((count - 1) / LIMIT_FOLLOWED_PAGE);
        Pageable page = PageRequest.of(offset, LIMIT_FOLLOWED_PAGE);
        var followedInformations = userRepository.findUserFollowedByUsername(username, page).stream().map(SimpleUserInformation::from).toList();
        return new UserPageInformation(followedInformations, offset, maxPageNumber, count);
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword){
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    public void delete(String username){
        var userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }
        var user = userOptional.get();
        userRepository.delete(user);
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

    @Transactional
    public void changePhoto(byte[] photo) {
        var user = currentUser();
        if (user == null){
            throw new IllegalStateException("User not found");
        }
        userRepository.changePhoto(user.getUsername(), photo);
    }
}


