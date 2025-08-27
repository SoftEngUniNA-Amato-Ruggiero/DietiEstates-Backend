package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.factory.insertiondtofactory.InsertionDTOFactory;

public interface Insertion {
    Long getId();
    Address getAddress();
    InsertionDetails getDetails();
    RealEstateAgent getUploader();
    RealEstateAgency getAgency();
    InsertionDTOFactory getDTOFactory();
}
