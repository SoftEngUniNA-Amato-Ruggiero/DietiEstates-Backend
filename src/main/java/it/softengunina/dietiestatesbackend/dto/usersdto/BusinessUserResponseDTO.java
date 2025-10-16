package it.softengunina.dietiestatesbackend.dto.usersdto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyResponseDTO;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.Delegate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessUserResponseDTO implements UserResponse {
    @Delegate(types = UserResponse.class)
    @JsonIgnore
    @NotNull
    @Valid
    private UserResponseDTO user;

    @Valid
    private RealEstateAgencyResponseDTO agency;

    public BusinessUserResponseDTO(UserWithAgency user) {
        this.user = new UserResponseDTO(user);
        this.agency = new RealEstateAgencyResponseDTO(user.getAgency());
    }
}
