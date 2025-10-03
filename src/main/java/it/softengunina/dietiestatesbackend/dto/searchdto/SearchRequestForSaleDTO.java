package it.softengunina.dietiestatesbackend.dto.searchdto;

import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearchForSale;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class SearchRequestForSaleDTO extends SearchRequestDTO {
    private Double maxPrice;

    @Override
    public SavedSearchForSale toSavedSearch(BaseUser user) {
        return SavedSearchForSale.builder()
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
