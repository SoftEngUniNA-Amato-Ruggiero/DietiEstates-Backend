package it.softengunina.dietiestatesbackend.dto.usersdto;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserWithAgencyDTO {
    @NotNull
    @Valid
    private UserDTO user;

    @Valid
    private RealEstateAgencyDTO agency;

    public UserWithAgencyDTO(UserWithAgency user) {
        this.user = new UserDTO(user);
        this.agency = new RealEstateAgencyDTO(user.getAgency());
    }
}
