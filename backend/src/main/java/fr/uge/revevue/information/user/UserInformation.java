package fr.uge.revevue.information.user;

import java.util.Objects;

import fr.uge.revevue.entity.User;

public record UserInformation (
        String username,
        byte[] profilePhoto,
        int nbFollowed,
        int nbCode,
        int nbReview,
        int nbComments,
        boolean isFollowed
){

    public static UserInformation from(User user, boolean isFollowed){
        Objects.requireNonNull(user, "[UserInformation] user is null");
        return new UserInformation(
                user.getUsername(),
                user.getProfilePhoto(),
                user.getFollowed().size(),
                user.getPosts().stream().filter(post -> post.getDtype().equals("Code")).toList().size(),
                user.getPosts().stream().filter(post -> post.getDtype().equals("Review")).toList().size(),
                user.getComments().size(),
                isFollowed
        );
    }
}