package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionWithRentResponseDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

/**
 * Class for insertions about real estates that are available for rent.
 */
@Entity
@Table(name = "insertions_for_rent")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InsertionForRent extends BaseInsertion implements InsertionWithRent {
    @Column(nullable = false)
    @Getter
    @Setter
    private Double rent;

    /**
     * Constructor for InsertionForRent.
     * @param address it is the address of the insertion
     * @param description it contains the description of the insertion
     * @param tags it is a set of tags associated with the insertion
     * @param uploader it is the real estate agent who uploaded the insertion
     * @param agency it is the real estate agency the uploader may be affiliated with
     * @param details it is the object that contains all the details of the insertion
     * @param rent it is the monthly rent of the real estate
     */
    @Builder
    public InsertionForRent(String description,
                            Set<Tag> tags,
                            @NonNull Address address,
                            @NonNull BaseUser uploader,
                            @NonNull RealEstateAgency agency,
                            InsertionDetails details,
                            @NonNull Double rent) {
        super(description, tags, address, uploader, agency, details);
        this.rent = rent;
    }

    @Override
    public InsertionWithRentResponseDTO accept(InsertionDTOVisitor visitor) {
        return visitor.visit(this);
    }
}
