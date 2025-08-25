package it.softengunina.dietiestatesbackendservice.dto;

import it.softengunina.dietiestatesbackendservice.model.RealEstateAgency;
import it.softengunina.dietiestatesbackendservice.model.users.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAgencyRoleDTO {
    @NotNull
    @Valid
    private UserDTO user;

    @Valid
    private RealEstateAgencyDTO agency;

    @NotNull
    private String role;

    public UserAgencyRoleDTO(User user) {
        this.user = new UserDTO(user);

        RealEstateAgency userAgency = user.getAgency();
        if (userAgency != null) {
            this.agency = new RealEstateAgencyDTO(userAgency);
        } else {
            this.agency = null;
        }

        this.role = user.getRole();
    }
}
