package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment,Long> {

    List<Comment> findByPostIdOrderByDateDesc(Pageable pageable, long postId);

    long countByUserId(long userId);

    List<Comment> findAllByUserId(long usserId);

    List<Comment> findAllByUserId(long usserId, Pageable page);
}
