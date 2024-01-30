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
public interface CodeRepository  extends CrudRepository<Code, Integer> {

    List<Code> findAll(Pageable pageable);
    
    List<Code> findByTitleContaining(String keyword);
}
