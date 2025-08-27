package it.softengunina.dietiestatesbackend.model.insertions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "insertions")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class BaseInsertion implements Insertion {
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
