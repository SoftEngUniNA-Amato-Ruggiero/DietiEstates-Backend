package it.softengunina.dietiestatesbackend.factory.insertionfactory;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.InsertionForRentRequestDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.insertions.Tag;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import org.geojson.Feature;
import org.springframework.stereotype.Component;

@Component
public class InsertionForRentFactory implements InsertionFactory<InsertionForRentRequestDTO> {
    public InsertionForRent createInsertion(InsertionForRentRequestDTO req, UserWithAgency uploader) {
        Feature feature = req.getAddress().getFeatures().getFirst();
        Address address = Address.fromProperties(feature.getProperties());

        InsertionDetails details = InsertionDetailsFactory.create(req);

        return InsertionForRent.builder()
                .description(req.getDescription())
                .tags(Tag.fromNames(req.getTags()))
                .address(address)
                .uploader(uploader.getUser())
                .agency(uploader.getAgency())
                .details(details)
                .rent(req.getRent())
                .build();
    }
}
