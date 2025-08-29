package it.softengunina.dietiestatesbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a real estate agency.
 * Includes fields for ID, IBAN, and name.
 * It can be managed by multiple real estate managers.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "real_estate_agencies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class RealEstateAgency{
    /**
     * Primary key for the real estate agency.
     * Auto-generated value.
     */
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

