package it.softengunina.userservice.dto;

import it.softengunina.userservice.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserAndRoleDTO {
    @NotNull
    @Valid
    private User user;

    @NotNull
    private String role;
}