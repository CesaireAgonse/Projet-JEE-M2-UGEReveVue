package fr.uge.revevue.repository;

import fr.uge.revevue.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Iterable<User> findAll();

    @Query("SELECT u.followed FROM User u WHERE u.id= :userId ")
    List<User> findFollowedById(long userId);

    @Query("SELECT u.followed FROM User u WHERE u.id= :userId AND u NOT IN :users")
    List<User> findFollowedByIdFilterUsers(long userId, List<User> users);

    boolean existsByUsername(String username);

    List<User> findAll();

    @Modifying
    @Query("UPDATE User SET password = :password WHERE username = :username")
    void update(@Param("username") String username,@Param("password") String password);
}
