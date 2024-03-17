package fr.uge.revevue.repository;

import fr.uge.revevue.entity.Post;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.entityId.VoteId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends CrudRepository<Vote, VoteId> {

    @Query("SELECT v FROM Vote v WHERE v.user = :user AND v.post = :post")
    Vote findByPostAndUser(User user, Post post);
}
