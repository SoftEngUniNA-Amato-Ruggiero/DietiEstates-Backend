package it.softengunina.dietiestatesbackendservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotBlank
    private String username;
}
