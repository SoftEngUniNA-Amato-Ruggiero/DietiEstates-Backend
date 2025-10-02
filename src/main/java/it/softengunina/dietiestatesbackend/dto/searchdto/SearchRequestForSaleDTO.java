package it.softengunina.dietiestatesbackend.dto.searchdto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class SearchRequestForSaleDTO extends SearchRequestDTO {
    private Double maxPrice;
}
