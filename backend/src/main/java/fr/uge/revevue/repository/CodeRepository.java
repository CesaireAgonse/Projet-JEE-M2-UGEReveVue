package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Code;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository  extends CrudRepository<Code, Long> {

    int countByUserUsername(String username);

    int countByUserIdAndTitleContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCaseOrUserIdAndUserUsernameContainingIgnoreCase(long userIdTitle, String titleKeyword, long userIdDescription, String descriptionKeyword, long userIdUsername, String userUsernameKeyword);

    int countByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(String titleKeyword, String descriptionKeyword, String userUsernameKeyword);

    List<Code> findAllByUserUsername(String username, Pageable page);

    List<Code> findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCase(Pageable pageable, String titleKeyword, String descriptionKeyword, String userUsernameKeyword);
    
    List<Code> findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCaseOrderByDateDesc(Pageable pageable, String titleKeyword, String descriptionKeyword, String userUsernameKeyword);
    
    List<Code> findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCaseOrderByScoreDesc(Pageable pageable, String titleKeyword, String descriptionKeyword, String userUsernameKeyword);
}
