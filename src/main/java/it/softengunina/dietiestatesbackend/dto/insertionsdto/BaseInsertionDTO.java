package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyRequestDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserResponseDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseInsertionDTO implements InsertionDTO {
    private Long id;
    @Valid
    @NotNull
    private Address address;
    private InsertionDetails details;
    private UserResponseDTO uploader;
    private RealEstateAgencyRequestDTO agency;

    protected BaseInsertionDTO(Insertion insertion) {
        this.id = insertion.getId();
        this.address = insertion.getAddress();
        this.details = insertion.getDetails();
        this.uploader = new UserResponseDTO(insertion.getUploader());
        this.agency = new RealEstateAgencyRequestDTO(insertion.getAgency());
    }
}
