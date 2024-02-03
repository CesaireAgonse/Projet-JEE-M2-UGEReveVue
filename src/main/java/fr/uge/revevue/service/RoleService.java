package fr.uge.revevue.service;


import fr.uge.revevue.entity.Role;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.repository.CodeRepository;
import fr.uge.revevue.repository.RoleRepository;
import fr.uge.revevue.repository.UserRepository;
import fr.uge.revevue.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @PersistenceUnit
    private final EntityManagerFactory emf;

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public RoleService(RoleRepository roleRepository,
                       EntityManagerFactory emf,
                       EntityManager em){
        this.roleRepository = roleRepository;
        this.emf = emf;
        this.em = em;
    }

    @Bean
    public void initRole(){
        var adminRole = Role.admin();
        var userRole = Role.user();
        roleRepository.save(adminRole);
        roleRepository.save(userRole);
    }

    public Role getUser(){
        var userRole = roleRepository.findByName("USER");
        if (userRole.isEmpty()){
            throw new IllegalStateException("User role not found");
        }
        return userRole.get();
    }

    public Role getAdmin(){
        var adminRole = roleRepository.findByName("ADMIN");
        if (adminRole.isEmpty()){
            throw new IllegalStateException("Admin role not found");
        }
        return adminRole.get();
    }
}
