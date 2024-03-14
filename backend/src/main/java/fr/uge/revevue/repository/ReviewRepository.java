package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review,Long> {

    List<Review> findByPostIdOrderByDateDesc(Pageable pageable, long postId);

    long countByUserId(long userId);

    long countByPostId(long postId);

    long countByUserUsername(String username);

    List<Review> findAllByUserUsername(String username, Pageable page);
}
