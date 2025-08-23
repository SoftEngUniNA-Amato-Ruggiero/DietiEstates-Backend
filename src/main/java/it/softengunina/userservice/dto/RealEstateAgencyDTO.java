package it.softengunina.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class RealEstateAgencyDTO {
    @NotBlank
    private String iban;

    @NotBlank
    private String name;
}
