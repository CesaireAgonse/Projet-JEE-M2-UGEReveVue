package fr.uge.revevue.service;

import fr.uge.revevue.entity.Post;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PostRepository postRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       PostRepository postRepository){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.postRepository = postRepository;
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
    public List<UserInformation> getAllUser(){
        return userRepository.findAll().stream().map(UserInformation::from).toList();
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword){
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    public UserInformation delete(long codeId){
        var userOptional = userRepository.findById(codeId);
        if(userOptional.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }
        var user = userOptional.get();

        //supression de tout les posts de l'User
        Set<Post> userPosts = postRepository.findByUserId(codeId);
        for (Post post : userPosts) {
            postRepository.delete(post);
        }

        userRepository.delete(user);
        return UserInformation.from(user);
    }
}


