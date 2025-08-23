package it.softengunina.userservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "agency")
    @JsonManagedReference
    @Setter(AccessLevel.PROTECTED)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<RealEstateManager> managers = new HashSet<>();

    @OneToMany(mappedBy = "agency")
    @JsonManagedReference
    @Setter(AccessLevel.PROTECTED)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<RealEstateAgent> agents = new HashSet<>();

    public RealEstateAgency(@NonNull String iban,
                            @NonNull String name) {
        this.iban = iban;
        this.name = name;
    }

    public Set<RealEstateManager> getManagers() {
        return Collections.unmodifiableSet(managers);
    }

    public Set<RealEstateAgent> getAgents() {
        return Collections.unmodifiableSet(agents);
    }

    public void addManager(@NonNull RealEstateManager manager) {
        this.agents.add(manager);
        this.managers.add(manager);
    }

    public void addAgent(@NonNull RealEstateAgent agent) {
        this.agents.add(agent);
    }

    public void removeManager(RealEstateManager manager) {
        this.managers.remove(manager);
    }

    public void removeAgent(RealEstateAgent agent) {
        this.agents.remove(agent);
    }
}

