package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;

public interface Insertion {
    Long getId();
    Address getAddress();
    InsertionDetails getDetails();
    RealEstateAgent getUploader();
    RealEstateAgency getAgency();
}
