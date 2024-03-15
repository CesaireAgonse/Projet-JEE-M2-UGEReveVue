package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    Set<Post> findByUserUsername(String username);
}
