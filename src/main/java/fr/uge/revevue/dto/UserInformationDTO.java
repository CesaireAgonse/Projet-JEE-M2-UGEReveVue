package fr.uge.revevue.dto;

import javax.validation.constraints.NotBlank;

public class UserInformationDTO {
    @NotBlank
    private String username;
    public UserInformationDTO(){}

    public UserInformationDTO(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
