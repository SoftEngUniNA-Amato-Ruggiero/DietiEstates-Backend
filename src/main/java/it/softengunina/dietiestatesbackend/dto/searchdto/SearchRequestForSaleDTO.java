package it.softengunina.dietiestatesbackend.dto.searchdto;

import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearchForSale;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class SearchRequestForSaleDTO extends SearchRequestDTO {
    private Double maxPrice;

    @Builder(builderMethodName = "searchRequestForSaleDTOBuilder")
    public SearchRequestForSaleDTO(Double maxPrice,
                                   Double lat,
                                   Double lng,
                                   Double distance,
                                   Double minSize,
                                   Integer minNumberOfRooms,
                                   Integer maxFloor,
                                   Boolean hasElevator,
                                   String tags) {
        super(lat, lng, distance, minSize, minNumberOfRooms, maxFloor, hasElevator, tags);
        this.maxPrice = maxPrice;
    }

    @Override
    public SavedSearchForSale toSavedSearch(BaseUser user) {
        return SavedSearchForSale.savedSearchForSaleBuilder()
                .user(user)
                .geometry(this.getPoint())
                .distance(this.getDistance())
                .minSize(this.getMinSize())
                .minNumberOfRooms(this.getMinNumberOfRooms())
                .maxFloor(this.getMaxFloor())
                .hasElevator(this.getHasElevator())
                .tags(this.getTagsSet())
                .maxPrice(this.getMaxPrice())
                .build();
    }
}
