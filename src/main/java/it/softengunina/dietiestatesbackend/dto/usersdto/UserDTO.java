package it.softengunina.dietiestatesbackend.dto.usersdto;

import it.softengunina.dietiestatesbackend.model.Role;
import it.softengunina.dietiestatesbackend.model.users.User;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;

    @NotBlank
    private String username;

    @NotNull
    private Set<Role> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles();
    }
}