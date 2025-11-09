package it.softengunina.dietiestatesbackend.dto.usersdto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.softengunina.dietiestatesbackend.dto.NotificationsPreferencesDTO;
import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyResponseDTO;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeResponseDTO implements UserResponse {
    @Delegate(types = UserResponse.class)
    @JsonIgnore
    @NotNull
    @Valid
    private UserResponseDTO user;

    @NotNull
    @Valid
    private NotificationsPreferencesDTO notificationsPreferences;

    @Valid
    private RealEstateAgencyResponseDTO agency = null;

    public MeResponseDTO(User user, NotificationsPreferences prefs) {
        this.user = new UserResponseDTO(user);
        this.notificationsPreferences = new NotificationsPreferencesDTO(prefs);
    }
}
