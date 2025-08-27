package it.softengunina.dietiestatesbackend.dto.usersdto;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;

    @NotBlank
    private String username;

    public UserDTO(BaseUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}