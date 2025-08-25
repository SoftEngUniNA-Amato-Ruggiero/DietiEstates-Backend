package it.softengunina.userservice.model.insertions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.softengunina.userservice.model.users.RealEstateAgent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class Insertion {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", nullable = false)
    @JsonBackReference
    @NotNull
    @Getter
    @Setter
    private RealEstateAgent uploader;

    protected Insertion(@NonNull Address address, InsertionDetails details, @NonNull RealEstateAgent uploader) {
        this.address = address;
        this.details = details;
        this.uploader = uploader;
    }
}
