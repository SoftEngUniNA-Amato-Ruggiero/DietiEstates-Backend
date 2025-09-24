package it.softengunina.dietiestatesbackend.dto.usersdto;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyResponseDTO;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessUserResponseDTO {
    @NotNull
    @Valid
    private UserResponseDTO user;

    @NotNull
    @Valid
    private RealEstateAgencyResponseDTO agency;

    public BusinessUserResponseDTO(UserWithAgency user) {
        this.user = new UserResponseDTO(user);
        this.agency = new RealEstateAgencyResponseDTO(user.getAgency());
    }
}
