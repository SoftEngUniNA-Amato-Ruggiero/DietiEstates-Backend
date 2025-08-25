package it.softengunina.userservice.dto;

import it.softengunina.userservice.model.RealEstateAgency;
import it.softengunina.userservice.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserAgencyRoleDTO {
    @NotNull
    @Valid
    private User user;

    @Valid
    private RealEstateAgency agency;

    @NotNull
    private String role;
}
