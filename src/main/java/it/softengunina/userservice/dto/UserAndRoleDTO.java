package it.softengunina.userservice.dto;

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
    private UserDTO user;

    @NotNull
    private String role;
}