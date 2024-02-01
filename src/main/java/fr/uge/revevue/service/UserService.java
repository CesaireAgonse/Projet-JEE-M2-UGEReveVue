package fr.uge.revevue.service;

import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @PersistenceUnit
    private final EntityManagerFactory emf;

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       EntityManagerFactory emf,
                       EntityManager em){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emf = emf;
        this.em = em;
    }

    public void signup(String username, String password) {
        var find = userRepository.findByUsername(username);
        if (find.isPresent()){
            throw new IllegalArgumentException("username already used");
        }
        var passwordCrypt = bCryptPasswordEncoder.encode(password);
        var user = new User(username, passwordCrypt);
        userRepository.save(user);
        UserInformation.from(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
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

    @Transactional
    public UserInformation getInformation(String username){
        var optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(UserInformation::from).orElse(null);
    }

    @Transactional
    public void modifyPassword(String username, String newPassword, String currentPassword){
        var find = userRepository.findByUsername(username);
        if (find.isEmpty()){
            throw new IllegalStateException("User not found");
        }
        if(!bCryptPasswordEncoder.matches(currentPassword,find.get().getPassword())){
            throw new IllegalArgumentException("Current Password are not the same");
        }
        var passwordCrypt = bCryptPasswordEncoder.encode(newPassword);
        userRepository.update(username,passwordCrypt);
    }

    @Transactional
    public void follow(String followerUsername, String followedUsername){
        var optionalFollowedUser = userRepository.findByUsername(followedUsername);
        if (optionalFollowedUser.isEmpty()){
            throw new IllegalStateException("User follower not found");
        }
        var optionalFollowerUser = userRepository.findByUsername(followerUsername);
        if (optionalFollowerUser.isEmpty()){
            throw new IllegalStateException("User followed not found");
        }
        var followedUser = optionalFollowedUser.get();
        var followerUser = optionalFollowerUser.get();
        followerUser.getFollowed().add(followedUser);
        em.persist(followerUser);
    }

    @Transactional
    public void unfollow(String followerUsername, String followedUsername){
        var optionalFollowedUser = userRepository.findByUsername(followedUsername);
        if (optionalFollowedUser.isEmpty()){
            throw new IllegalStateException("User follower not found");
        }
        var optionalFollowerUser = userRepository.findByUsername(followerUsername);
        if (optionalFollowerUser.isEmpty()){
            throw new IllegalStateException("User followed not found");
        }
        var followedUser = optionalFollowedUser.get();
        var followerUser = optionalFollowerUser.get();
        followerUser.getFollowed().remove(followedUser);
        em.persist(followerUser);
    }

}


