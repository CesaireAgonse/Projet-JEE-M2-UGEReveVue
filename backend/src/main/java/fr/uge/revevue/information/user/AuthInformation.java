package fr.uge.revevue.information.user;

import fr.uge.revevue.entity.Role;
import fr.uge.revevue.entity.User;

import java.util.Objects;

public record AuthInformation(String username, boolean isAdmin){

    public static AuthInformation from(User user){
        Objects.requireNonNull(user, "[AuthInformation] user is null");
        return new AuthInformation(
                user.getUsername(),
                user.getRole().getTypeRole().equals(Role.TypeRole.ADMIN)
        );
    }
}
