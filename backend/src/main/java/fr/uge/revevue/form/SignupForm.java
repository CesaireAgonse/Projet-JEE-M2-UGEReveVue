package fr.uge.revevue.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class SignupForm {

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must contain only letters (upper and lower case), numbers, and underscore.")
    @NotBlank(message = "Please enter an username.")
    private String username;

    @NotBlank(message = "Please enter a password.")
    private String password;

    @NotBlank(message = "Please confirm your password.")
    private String confirmPassword;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
