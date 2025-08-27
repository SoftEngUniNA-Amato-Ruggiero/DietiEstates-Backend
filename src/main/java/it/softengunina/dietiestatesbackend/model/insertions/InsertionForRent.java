package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithRentDTO;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "insertions_for_rent")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InsertionForRent extends BaseInsertion implements InsertionWithRent {
    @Getter
    @Setter
    private Double rent;

    public InsertionForRent(Address address, InsertionDetails details, RealEstateAgent uploader, double rent) {
        super(address, details, uploader);
        this.rent = rent;
    }

    @Override
    public InsertionDTO toDTO() {
        return new InsertionWithRentDTO(this);
    }
}
