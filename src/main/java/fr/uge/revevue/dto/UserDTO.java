package fr.uge.revevue.dto;

import javax.validation.constraints.NotBlank;

public record UserDTO(@NotBlank String username, @NotBlank String password) {}
