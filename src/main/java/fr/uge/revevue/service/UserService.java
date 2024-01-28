package fr.uge.revevue.service;

import com.sun.jdi.request.InvalidRequestStateException;
import fr.uge.revevue.dto.UserInformationDTO;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(){}

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserInformationDTO signup(String username, String password) {
        var find = userRepository.findByUsername(username);
        if (find.isPresent()){
            throw new IllegalArgumentException("username already used");
        }
        var passwordCrypt = bCryptPasswordEncoder.encode(password);
        var user = new User(username, passwordCrypt);
        userRepository.save(user);
        return new UserInformationDTO(username);
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

    public UserInformationDTO getInformations(String username){
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()){
            return null;
        }
        return new UserInformationDTO(user.get().getUsername());
    }

    @Transactional
    public void modifPassword(String username,String newPassword,String currentPassword){
        var find = userRepository.findByUsername(username);
        if (find.isEmpty()){
            throw new IllegalStateException("User not found");
        }
        if(!bCryptPasswordEncoder.matches(currentPassword,find.get().getPassword())){
            throw new IllegalArgumentException("Current PassWord are not the same");
        }
        else {
            var passwordCrypt = bCryptPasswordEncoder.encode(newPassword);
            userRepository.update(username,passwordCrypt);

        }


    }


}
