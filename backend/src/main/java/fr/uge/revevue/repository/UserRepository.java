package fr.uge.revevue.repository;

import fr.uge.revevue.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Modifying
    @Query("UPDATE User SET password = :password WHERE username = :username")
    void update(@Param("username") String username,@Param("password") String password);

    @Modifying
    @Query("UPDATE User SET profilePhoto = :photo WHERE username = :username")
    void changePhoto(String username, byte[] photo);

    boolean existsByUsername(String username);

    int countUserFollowedByUsername(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role <> '1'")
    int countNonAdminUsers();

    Optional<User> findByUsername(String username);

    List<User> findUserByFollowedId(long followedId);

    @Query("SELECT u FROM User u WHERE u.role <> '1'")
    List<User> findAllNonAdminUsers(Pageable page);

    @Query("SELECT u.followed FROM User u WHERE u.id= :userId ")
    List<User> findFollowedById(long userId);

    @Query("SELECT u.followed FROM User u WHERE u.id= :userId AND u NOT IN :users")
    List<User> findFollowedByIdFilterUsers(long userId, List<User> users);
    
    @Query("SELECT u FROM User u WHERE u NOT IN :users")
    List<User> findUserFilterUsers(List<User> users);

    @Query("SELECT u.followed FROM User u WHERE u.username= :username ")
    List<User> findUserFollowedByUsername(String username, Pageable page);
}
