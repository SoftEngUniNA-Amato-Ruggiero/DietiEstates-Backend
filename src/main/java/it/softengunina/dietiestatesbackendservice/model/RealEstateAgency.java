package it.softengunina.dietiestatesbackendservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

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
    @Setter
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    @Getter
    @Setter
    private String iban;

    @NotBlank
    @Getter
    @Setter
    private String name;

    public RealEstateAgency(@NonNull String iban,
                            @NonNull String name) {
        this.iban = iban;
        this.name = name;
    }
}

