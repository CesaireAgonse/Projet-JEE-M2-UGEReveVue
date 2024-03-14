package fr.uge.revevue.information.user;

import fr.uge.revevue.entity.Role;
import fr.uge.revevue.entity.User;

import java.util.Objects;

public record SimpleUserInformation(long id, String username, boolean isAdmin, byte[] profilePhoto){

    public static SimpleUserInformation from(User user){
        Objects.requireNonNull(user, "[FollowedInformation] user is null");
        return new SimpleUserInformation(
                user.getId(),
                user.getUsername(),
                user.getRole().getTypeRole().equals(Role.TypeRole.ADMIN),
                user.getProfilePhoto()
        );
    }

}
