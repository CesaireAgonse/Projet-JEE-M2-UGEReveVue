package fr.uge.revevue.information.user;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import fr.uge.revevue.entity.Role;
import fr.uge.revevue.entity.User;

public record UserInformation (long id,
                               String username,
                               Set<SimpleUserInformation> followed,
                               boolean isAdmin,
                               byte[] profilePhoto){

    public static UserInformation from(User user){
        Objects.requireNonNull(user, "[UserInformation] user is null");
        return new UserInformation(
                user.getId(),
                user.getUsername(),
                user.getFollowed().stream().map(SimpleUserInformation::from).collect(Collectors.toSet()),
                user.getRole().getTypeRole().equals(Role.TypeRole.ADMIN),
                user.getProfilePhoto()
        );
    }

    public Set<String> allFollowedName(){
        return followed.stream().map(SimpleUserInformation::username).collect(Collectors.toSet());
    }
}