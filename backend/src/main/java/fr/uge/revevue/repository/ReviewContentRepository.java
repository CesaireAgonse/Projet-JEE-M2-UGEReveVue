package fr.uge.revevue.repository;

import fr.uge.revevue.entity.ReviewContent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ReviewContentRepository extends CrudRepository<ReviewContent, Long> {

    int countByUserUsername(String username);

    Optional<ReviewContent> findByUserUsernameAndContentAndCodeSelection(String username, String content, String codeSelection);

    List<ReviewContent> findAllByUserUsernameOrderByDateDesc(String username, Pageable page);
}

