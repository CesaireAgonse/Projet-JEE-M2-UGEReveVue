package fr.uge.revevue.form;

import javax.validation.constraints.NotBlank;

public record LoginForm(@NotBlank String username, @NotBlank String password) {}
