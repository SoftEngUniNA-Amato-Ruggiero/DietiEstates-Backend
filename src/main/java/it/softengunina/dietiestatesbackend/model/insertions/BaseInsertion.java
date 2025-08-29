package it.softengunina.dietiestatesbackend.model.insertions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * This abstract class represents a base real estate insertion.
 * It implements the Insertion interface and includes common attributes
 * such as id, address, details, uploader (real estate agent), and associated agency of the uploader.
 */
@Entity
@Table(name = "insertions")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class BaseInsertion implements Insertion {
    /**
     * The unique identifier for the insertion.
     * It is automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Embedded
    @Getter
    @Setter
    private Address address;

    @Embedded
    @Getter
    @Setter
    private InsertionDetails details;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploader_id", nullable = false)
    @JsonBackReference
    @NotNull
    @Getter
    @Setter
    private RealEstateAgent uploader;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agency_id", nullable = false)
    @JsonBackReference
    @NotNull
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private RealEstateAgency agency;

    protected BaseInsertion(@NonNull Address address, InsertionDetails details, @NonNull RealEstateAgent uploader) {
        this.address = address;
        this.details = details;
        this.uploader = uploader;
        this.agency = uploader.getAgency();
    }

    public Double getPrice() {
        return null;
    }

    public Double getRent() {
        return null;
    }
}
