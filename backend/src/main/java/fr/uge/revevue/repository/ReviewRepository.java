package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review,Long> {

    int countByUserId(long userId);

    int countByUserUsername(String username);

    int countByPostId(long postId);

    List<Review> findAllByUserUsername(String username, Pageable page);

    List<Review> findByPostIdOrderByDateDesc(Pageable pageable, long postId);
}
