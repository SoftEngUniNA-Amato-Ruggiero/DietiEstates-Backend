package it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InsertionForSaleRequestDTO extends BaseInsertionRequestDTO {
    private double price;
}
