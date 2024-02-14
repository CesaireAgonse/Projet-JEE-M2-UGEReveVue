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
    
    List<Code> findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCase(String titleKeyword, String descriptionKeyword, String userUsernameKeyword);
    
    List<Code> findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCase(Pageable pageable, String titleKeyword, String descriptionKeyword, String userUsernameKeyword);
    
    List<Code> findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCaseOrderByDateDesc(Pageable pageable, String titleKeyword, String descriptionKeyword, String userUsernameKeyword);
    
    List<Code> findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCaseOrUserIdAndUserUsernameContainingIgnoreCase(long userIdTitle, String titleKeyword, long userIdDescription, String descriptionKeyword, long userIdUsername, String userUsernameKeyword);
    
    int countByUserIdAndTitleContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCaseOrUserIdAndUserUsernameContainingIgnoreCase(long userIdTitle, String titleKeyword, long userIdDescription, String descriptionKeyword, long userIdUsername, String userUsernameKeyword);
}
