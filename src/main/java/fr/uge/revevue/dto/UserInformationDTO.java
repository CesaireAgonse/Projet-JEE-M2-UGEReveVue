package fr.uge.revevue.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class UserInformationDTO {

    private long id;

    @NotBlank
    private String username;

    private List<String> followed;
    public UserInformationDTO(){}

    public UserInformationDTO(long id, String username, List<String> followed){
        this.id = id;
        this.username = username;
        this.followed = followed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getFollowed() {
        return followed;
    }

    public void setFollowed(List<String> followed) {
        this.followed = followed;
    }
}
