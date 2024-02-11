package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository  extends CrudRepository<Code, Long> {

    List<Code> findAll(Pageable pageable);
    
    List<Code> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(String titleKeyword, String descriptionKeyword, String userUsernameKeyword);
    
    List<Code> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(Pageable pageable, String titleKeyword, String descriptionKeyword, String userUsernameKeyword);
    
    List<Code> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCaseOrderByDateDesc(Pageable pageable, String titleKeyword, String descriptionKeyword, String userUsernameKeyword);
    
    List<Code> findByUserIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(Pageable pageable, long userId, String titleKeyword, String descriptionKeyword, String userUsernameKeyword);
}
