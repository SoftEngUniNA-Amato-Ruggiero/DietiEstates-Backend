package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class BaseInsertionDTO implements InsertionDTO {
    private Long id;
    @Valid
    @NotNull
    private Address address;
    private InsertionDetails details;
    private UserDTO uploader;
    private RealEstateAgencyDTO agency;

    protected BaseInsertionDTO(Insertion insertion) {
        this.id = insertion.getId();
        this.address = insertion.getAddress();
        this.details = insertion.getDetails();
        this.uploader = new UserDTO(insertion.getUploader());
        this.agency = new RealEstateAgencyDTO(insertion.getAgency());
    }
}
