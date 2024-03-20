package fr.uge.revevue.service;

import fr.uge.revevue.entity.Role;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.information.user.SimpleUserInformation;
import fr.uge.revevue.repository.RoleRepository;
import fr.uge.revevue.repository.UserRepository;
import fr.uge.revevue.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       JwtService jwtService
    ){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
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
        SimpleUserInformation.from(user);
    }

    public Map<String, String> login(String username, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        if (authentication.isAuthenticated()){
            return this.jwtService.generate(username);
        }
        return null;
    }

    public Map<String, String> refresh(Map<String, String> refreshToken){
        return this.jwtService.refreshToken(refreshToken);
    }

    @Bean
    void initialisationDataBase() {
        Role roleAdmin = new Role(Role.TypeRole.ADMIN);
        Role roleUser = new Role(Role.TypeRole.USER);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);
        User admin = new User("admin", bCryptPasswordEncoder.encode("admin"));
        admin.setRole(roleAdmin);
        User arnaud = new User("arnaud", bCryptPasswordEncoder.encode("arnaud"));
        arnaud.setRole(roleUser);
        userRepository.save(admin);
        userRepository.save(arnaud);
    }
}
