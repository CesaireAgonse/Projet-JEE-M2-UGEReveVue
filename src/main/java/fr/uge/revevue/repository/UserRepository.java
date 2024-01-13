package fr.uge.revevue.repository;

import fr.uge.revevue.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :login AND u.password = :password")
    Optional<User> findByLoginAndPassword(String login, String password);

}
