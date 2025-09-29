package it.softengunina.dietiestatesbackend.dto;

import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.AddressDetails;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AddressDTO extends AddressDetails {
    private Point location;

    public AddressDTO(Address address) {
        super(address.getDetails());
        this.location = address.getLocation();
    }
}
