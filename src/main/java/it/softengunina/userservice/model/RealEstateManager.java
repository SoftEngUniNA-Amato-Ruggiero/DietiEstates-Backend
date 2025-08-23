package it.softengunina.userservice.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "real_estate_managers")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString
public class RealEstateManager extends RealEstateAgent {
    public RealEstateManager(@NonNull String email, @NonNull String cognitoSub, @NonNull RealEstateAgency agency) {
        super(email, cognitoSub, agency);
    }
}