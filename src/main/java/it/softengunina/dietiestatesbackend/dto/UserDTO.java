package it.softengunina.dietiestatesbackend.dto;

import it.softengunina.dietiestatesbackend.model.users.User;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotBlank
    private String username;

    public UserDTO(User user) {
        this.username = user.getUsername();
    }
}
