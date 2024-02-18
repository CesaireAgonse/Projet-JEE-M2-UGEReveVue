package fr.uge.revevue.form;

import javax.validation.constraints.NotBlank;

public class LoginForm {

    @NotBlank(message = "Please enter an username.")
    private String username;

    @NotBlank(message = "Please enter a password.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
