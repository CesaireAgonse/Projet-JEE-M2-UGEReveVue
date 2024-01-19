package fr.uge.revevue.form;

import javax.validation.constraints.NotBlank;

public record SignupForm(@NotBlank String username, @NotBlank String password, @NotBlank String confirmPassword) {}
