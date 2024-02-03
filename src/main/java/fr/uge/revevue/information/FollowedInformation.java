package fr.uge.revevue.information;

import fr.uge.revevue.entity.User;

import java.util.Objects;

public record FollowedInformation(long id, String username){

    public static FollowedInformation from(User user){
        Objects.requireNonNull(user, "[FollowedInformation] user is null");
        return new FollowedInformation(
                user.getId(),
                user.getUsername()
        );
    }

}
