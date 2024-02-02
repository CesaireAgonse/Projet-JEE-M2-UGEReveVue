package fr.uge.revevue.information;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import fr.uge.revevue.entity.User;

public record UserInformation (long id, String username, Set<FollowedInformation> followed, boolean isAdmin){
    public static UserInformation from(User user){
        Objects.requireNonNull(user, "[UserInformation] user is null");
        return new UserInformation(
                user.getId(),
                user.getUsername(),
                user.getFollowed().stream().map(FollowedInformation::from).collect(Collectors.toSet()),
                user.isAdmin()
        );
    }

    public Set<String> allFollowedName(){
        return followed.stream().map(FollowedInformation::username).collect(Collectors.toSet());
    }
}