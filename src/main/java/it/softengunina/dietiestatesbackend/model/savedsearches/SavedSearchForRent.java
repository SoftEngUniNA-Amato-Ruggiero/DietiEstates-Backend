package it.softengunina.dietiestatesbackend.model.savedsearches;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.locationtech.jts.geom.Geometry;

import java.util.Set;

@Entity
@Table(name = "saved_searches_for_rent")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SavedSearchForRent extends SavedSearch {
    @Getter
    @Setter
    private Double maxRent;

    @Builder(builderMethodName = "savedSearchForRentBuilder")
    public SavedSearchForRent(BaseUser user, Geometry geometry, Double distance, Double minSize, Integer minNumberOfRooms, Integer maxFloor, Boolean hasElevator, Set<String> tags, Double maxRent) {
        super(user, geometry, distance, minSize, minNumberOfRooms, maxFloor, hasElevator, tags);
        this.maxRent = maxRent;
    }
}