package fr.uge.revevue.dto;

import javax.validation.constraints.NotBlank;

public record UserAuthenticationDTO(@NotBlank String username, @NotBlank String password) {}
