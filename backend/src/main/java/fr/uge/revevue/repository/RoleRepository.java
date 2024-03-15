package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByTypeRole(Role.TypeRole typeRole);
}
