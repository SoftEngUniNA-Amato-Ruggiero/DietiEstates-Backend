package it.softengunina.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotBlank
//    @Email
    private String username;
}
