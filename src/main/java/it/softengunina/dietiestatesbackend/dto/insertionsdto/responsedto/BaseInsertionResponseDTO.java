package it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyRequestDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserResponseDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BaseInsertionResponseDTO implements InsertionResponseDTO {
    @NotNull
    private Long id;
    @NotNull
    private String description;
    @NotNull
    private Set<String> tags;
    @NotNull
    @Valid
    private Address address;
    @NotNull
    @Valid
    private UserResponseDTO uploader;
    @NotNull
    @Valid
    private RealEstateAgencyRequestDTO agency;

    public BaseInsertionResponseDTO (@NonNull Insertion insertion) {
        this.id = insertion.getId();
        this.description = insertion.getDescription();
        this.tags = insertion.getTags();
        this.address = insertion.getAddress();
        this.uploader = new UserResponseDTO(insertion.getUploader());
        this.agency = new RealEstateAgencyRequestDTO(insertion.getAgency());
    }
}
