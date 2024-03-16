package fr.uge.revevue.repository;

import fr.uge.revevue.entity.ReviewContent;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReviewContentRepository extends CrudRepository<ReviewContent, Long> {

    Optional<ReviewContent> findByUserUsernameAndContentAndCodeSelection(String username, String content, String codeSelection);
}

