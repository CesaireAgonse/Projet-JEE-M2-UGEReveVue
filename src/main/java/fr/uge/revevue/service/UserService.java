package fr.uge.revevue.service;

import fr.uge.revevue.entity.Role;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.repository.RoleRepository;
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
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PersistenceUnit
    private final EntityManagerFactory emf;

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       EntityManagerFactory emf,
                       EntityManager em){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emf = emf;
        this.em = em;
    }

    @Transactional
    public void signup(String username, String password) {
        var find = userRepository.findByUsername(username);
        if (find.isPresent()){
            throw new IllegalArgumentException("username already used");
        }
        var passwordCrypt = bCryptPasswordEncoder.encode(password);
        var user = new User(username, passwordCrypt);
        var optionalRole = roleRepository.findByTypeRole(Role.TypeRole.USER);
        if (optionalRole.isEmpty()){
            throw new IllegalStateException("USER role not found");
        }
        user.setRole(optionalRole.get());
        userRepository.save(user);
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


    public boolean matchesPassword(String currentPassword){
        return bCryptPasswordEncoder.matches(currentPassword, currentUser().getPassword());
    }

    @Transactional
    public UserInformation modifyPassword(String currentPassword, String newPassword){
        var user = currentUser();
        if (user == null){
            throw new IllegalStateException("User not found");
        }
        if(!matchesPassword(currentPassword)){
            throw new IllegalArgumentException("Current Password are not the same");
        }
        var passwordCrypt = bCryptPasswordEncoder.encode(newPassword);
        userRepository.update(user.getUsername(), passwordCrypt);
        return UserInformation.from(user);
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

    @Transactional
    public List<UserInformation> getAllUser(){
        List<UserInformation> list = new ArrayList<>();
        userRepository.findAll().forEach( user -> { list.add( getInformation(user.getUsername()));});
        return list;
    }

    @Transactional
    public UserInformation delete(long codeId){
        var user = userRepository.findById(codeId);
        if(user.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }
        userRepository.delete(user.get());
        return UserInformation.from(user.get());
    }
}


