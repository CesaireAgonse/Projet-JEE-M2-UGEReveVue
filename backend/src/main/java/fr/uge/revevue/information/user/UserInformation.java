package fr.uge.revevue.information.user;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import fr.uge.revevue.entity.Role;
import fr.uge.revevue.entity.User;

public record UserInformation (long id,
                               String username,
                               int nbFollowed,
                               boolean isAdmin,
                               List<SimpleUserInformation> followed,
                               byte[] profilePhoto){

    public static UserInformation from(User user){
        Objects.requireNonNull(user, "[UserInformation] user is null");
        return new UserInformation(
                user.getId(),
                user.getUsername(),
                user.getFollowed().size(),
                user.getRole().getTypeRole().equals(Role.TypeRole.ADMIN),
                user.getFollowed().stream().map(SimpleUserInformation::from).toList(),
                user.getProfilePhoto()
        );
    }
}