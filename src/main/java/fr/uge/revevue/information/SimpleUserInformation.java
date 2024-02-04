package fr.uge.revevue.information;

import fr.uge.revevue.entity.User;

import java.util.Objects;

public record SimpleUserInformation(long id, String username){

    public static SimpleUserInformation from(User user){
        Objects.requireNonNull(user, "[FollowedInformation] user is null");
        return new SimpleUserInformation(
                user.getId(),
                user.getUsername()
        );
    }

}
