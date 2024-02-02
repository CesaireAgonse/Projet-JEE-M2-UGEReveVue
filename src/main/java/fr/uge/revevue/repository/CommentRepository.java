package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment,Long> {

}
