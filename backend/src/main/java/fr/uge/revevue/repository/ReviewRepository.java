package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Comment;
import fr.uge.revevue.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review,Long> {

    List<Review> findByPostId(Pageable pageable, long postId);

}
