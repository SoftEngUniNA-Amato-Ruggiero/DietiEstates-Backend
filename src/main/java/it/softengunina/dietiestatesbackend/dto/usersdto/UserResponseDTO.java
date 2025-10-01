package it.softengunina.dietiestatesbackend.dto.usersdto;

import it.softengunina.dietiestatesbackend.model.users.Role;
import it.softengunina.dietiestatesbackend.model.users.User;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    @NotNull
    private Long id;

    @NotBlank
    private String username;

    @NotNull
    private Set<Role> roles;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles();
    }
}