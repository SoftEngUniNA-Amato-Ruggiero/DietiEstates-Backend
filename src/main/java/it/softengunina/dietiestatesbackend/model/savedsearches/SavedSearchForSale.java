package it.softengunina.dietiestatesbackend.model.savedsearches;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Geometry;

import java.util.Set;

@Entity
@Table(name = "saved_searches_for_sale")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SavedSearchForSale extends SavedSearch {
    @Getter
    @Setter
    private Double maxPrice;

    public SavedSearchForSale(BaseUser user, Geometry geometry, Double distance, Double minSize, Integer minNumberOfRooms, Integer maxFloor, Boolean hasElevator, Set<String> tags, Double maxPrice) {
        super(user, geometry, distance, minSize, minNumberOfRooms, maxFloor, hasElevator, tags);
        this.maxPrice = maxPrice;
    }
}
