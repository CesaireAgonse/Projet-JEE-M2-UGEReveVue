package fr.uge.revevue.service;

import fr.uge.revevue.entity.Role;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.repository.RoleRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder
    ){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public SimpleUserInformation signup(String username, String password) {
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
        return SimpleUserInformation.from(user);
    }

    @Transactional
    public SimpleUserInformation login(String username, String password){
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()){
            throw new IllegalArgumentException("this username not existed");
        }
        var user = userOptional.get();
        if(!matchesPassword(password, user.getPassword())){
            throw new IllegalArgumentException("password incorrect");
        }
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return SimpleUserInformation.from(user);
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
        userRepository.delete(user);
        return UserInformation.from(user);
    }
}


