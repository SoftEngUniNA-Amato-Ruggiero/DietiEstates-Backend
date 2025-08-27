package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "insertions_for_sale")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InsertionForSale extends BaseInsertion implements InsertionWithPrice {
    @Getter
    @Setter
    private Double price;

    public InsertionForSale(Address address, InsertionDetails details, RealEstateAgent uploader, double price) {
        super(address, details, uploader);
        this.price = price;
    }
}
