package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review,Long> {

    List<Review> findByPostIdOrderByDateDesc(Pageable pageable, long postId);

    List<Review> findAllByUserId(long userId);

    long countByUserId(long userId);
}
