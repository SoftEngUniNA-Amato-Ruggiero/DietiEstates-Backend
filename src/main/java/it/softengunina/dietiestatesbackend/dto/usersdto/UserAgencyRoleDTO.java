package it.softengunina.dietiestatesbackend.dto.usersdto;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
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

    public UserAgencyRoleDTO(UserWithAgency user) {
        this.user = new UserDTO(user.getId(), user.getUsername());
        this.agency = new RealEstateAgencyDTO(user.getAgency());
        this.role = user.getRole();
    }

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
