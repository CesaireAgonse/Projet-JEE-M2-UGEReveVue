package fr.uge.revevue.information.user;

import fr.uge.revevue.entity.User;

import java.util.Objects;

public record SimpleUserInformation(
        String username,
        byte[] profilePhoto,
        int nbFollowed,
        int nbCode,
        int nbReview,
        int nbComments
){

    public static SimpleUserInformation from(User user){
        Objects.requireNonNull(user, "[FollowedInformation] user is null");
        return new SimpleUserInformation(
                user.getUsername(),
                user.getProfilePhoto(),
                user.getFollowed().size(),
                user.getPosts().stream().filter(post -> post.getDtype().equals("Code")).toList().size(),
                user.getPosts().stream().filter(post -> post.getDtype().equals("Review")).toList().size(),
                user.getComments().size()
        );
    }
}
