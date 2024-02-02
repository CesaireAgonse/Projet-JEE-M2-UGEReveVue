package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}
