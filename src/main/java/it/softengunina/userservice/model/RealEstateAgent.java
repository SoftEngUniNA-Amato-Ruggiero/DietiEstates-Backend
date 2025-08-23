package it.softengunina.userservice.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "real_estate_agents")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString
public class RealEstateAgent extends User {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    @JsonBackReference
    @NotNull
    @Getter
    @Setter
    protected RealEstateAgency agency;

    public RealEstateAgent(@NonNull String email, @NonNull String cognitoSub, @NonNull RealEstateAgency agency) {
        super(email, cognitoSub);
        this.agency = agency;
    }
}
