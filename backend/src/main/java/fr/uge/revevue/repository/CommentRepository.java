package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment,Long> {

    int countByUserId(long userId);

    int countByUserUsername(String username);

    int countByPostId(long postId);

    List<Comment> findByPostIdOrderByDateDesc(Pageable pageable, long postId);

    List<Comment> findAllByUserUsername(String username, Pageable page);
}
