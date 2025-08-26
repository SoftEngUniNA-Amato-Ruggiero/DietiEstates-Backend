package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.insertions.Address;
import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InsertionDTO {
    private Long id;
    private Address address;
    private InsertionDetails details;
    private UserDTO uploader;

    protected InsertionDTO(Insertion insertion) {
        this.id = insertion.getId();
        this.address = insertion.getAddress();
        this.details = insertion.getDetails();
        this.uploader = new UserDTO(insertion.getUploader());
    }
}
