package it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InsertionForRentRequestDTO extends BaseInsertionRequestDTO {
    private double rent;
}
