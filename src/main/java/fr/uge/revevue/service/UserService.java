package fr.uge.revevue.service;

import fr.uge.revevue.entity.User;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Insert an employee in the database
     */
    public void insert(User user) {
        userRepository.save(user);
    }

    public Optional<User> find(User user){
        return userRepository.findByLoginAndPassword(user.getUsername(), user.getPassword());
    }


}
