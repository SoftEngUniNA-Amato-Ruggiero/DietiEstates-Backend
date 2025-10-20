package it.softengunina.dietiestatesbackend.factory.insertionfactory;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.InsertionForSaleRequestDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.insertions.Tag;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import lombok.NonNull;
import org.geojson.Feature;
import org.springframework.stereotype.Component;

@Component
public class InsertionForSaleFactory implements InsertionFactory<InsertionForSaleRequestDTO> {
    public InsertionForSale createInsertion(@NonNull InsertionForSaleRequestDTO req,
                                            @NonNull UserWithAgency uploader) {
        Feature feature = req.getAddress().getFeatures().getFirst();
        Address address = Address.fromProperties(feature.getProperties());

        InsertionDetails details = InsertionDetailsFactory.create(req);

        return InsertionForSale.builder()
                .description(req.getDescription())
                .tags(Tag.fromNames(req.getTags()))
                .address(address)
                .uploader(uploader.getUser())
                .agency(uploader.getAgency())
                .details(details)
                .price(req.getPrice())
                .build();
    }
}
