package it.softengunina.dietiestatesbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

/**
 * Class for a Real Estate Agency that has been registered to the system.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "real_estate_agencies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class RealEstateAgency{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(unique = true, nullable = false)
    @Getter
    @Setter
    private String iban;

    @Column(nullable = false)
    @Getter
    @Setter
    private String name;

    public RealEstateAgency(@NonNull String iban,
                            @NonNull String name) {
        this.iban = iban;
        this.name = name;
    }
}

