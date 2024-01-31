package fr.uge.revevue.information;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import fr.uge.revevue.entity.User;

public record UserInformation (long id, String username, Set<UserInformation> followed){
    public static UserInformation from(User user){
        Objects.requireNonNull(user, "[UserInformation] user is null");
        return new UserInformation(
                user.getId(),
                user.getUsername(),
                user.getFollowed().stream().map(UserInformation::from).collect(Collectors.toSet())
        );
    }
}