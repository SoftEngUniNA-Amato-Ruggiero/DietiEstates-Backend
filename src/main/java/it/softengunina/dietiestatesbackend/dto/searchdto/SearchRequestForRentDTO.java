package it.softengunina.dietiestatesbackend.dto.searchdto;

import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearchForRent;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class SearchRequestForRentDTO extends SearchRequestDTO {
    private Double maxRent;

    @Builder(builderMethodName = "searchRequestForRentDTOBuilder")
    public SearchRequestForRentDTO(Double maxRent,
                                    Double lat,
                                    Double lng,
                                    Double distance,
                                    Double minSize,
                                    Integer minNumberOfRooms,
                                    Integer maxFloor,
                                    Boolean hasElevator,
                                    String tags) {
        super(lat, lng, distance, minSize, minNumberOfRooms, maxFloor, hasElevator, tags);
        this.maxRent = maxRent;
    }

    @Override
    public SavedSearchForRent toSavedSearch(BaseUser user) {
        return SavedSearchForRent.savedSearchForRentBuilder()
                .user(user)
                .geometry(this.getPoint())
                .distance(this.getDistance())
                .minSize(this.getMinSize())
                .minNumberOfRooms(this.getMinNumberOfRooms())
                .maxFloor(this.getMaxFloor())
                .hasElevator(this.getHasElevator())
                .tags(this.getTagsSet())
                .maxRent(this.getMaxRent())
                .build();
    }
}
