package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.factory.insertiondtofactory.InsertionDTOFactory;


/**
 * This interface represents a real estate insertion.
 * It provides methods to access the insertion's ID, address, details,
 * uploader (real estate agent), associated agency of the uploader.
 */
public interface Insertion {
    /**
     * Get the unique identifier of the insertion.
     * @return the ID of the insertion.
     */
    Long getId();
    Address getAddress();
    InsertionDetails getDetails();
    RealEstateAgent getUploader();
    RealEstateAgency getAgency();
    InsertionDTOFactory getDTOFactory();
}
