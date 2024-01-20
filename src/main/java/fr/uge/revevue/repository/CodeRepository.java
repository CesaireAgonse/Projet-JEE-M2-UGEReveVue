package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Code;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository  extends CrudRepository<Code, Integer> {
}
