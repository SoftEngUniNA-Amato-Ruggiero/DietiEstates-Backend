package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitor;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Abstract class for a generic type of insertion about real estates, uploaded by a Real Estate Agent.
 */
@Entity
@Table(name = "insertions")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class BaseInsertion implements Insertion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Embedded
    @Getter
    @Setter
    private InsertionDetails details;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NotNull
    @Getter
    @Setter
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploader_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @NotNull
    @Getter
    @Setter
    private BaseUser uploader;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agency_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @NotNull
    @Getter
    @Setter
    private RealEstateAgency agency;

    protected BaseInsertion(@NonNull Address address,
                            InsertionDetails details,
                            @NonNull BaseUser uploader,
                            RealEstateAgency agency) {
        this.address = address;
        this.details = details;
        this.uploader = uploader;
        this.agency = agency;
    }

    /**
     * Gets the correct DTO for the specific type of insertion,
     * allowing polymorphic creation of the correct DTOs for the concrete insertion type.
     *
     * @return InsertionDTO specific for the type of insertion.
     */
    public abstract InsertionDTO accept(InsertionDTOVisitor visitor);
}
