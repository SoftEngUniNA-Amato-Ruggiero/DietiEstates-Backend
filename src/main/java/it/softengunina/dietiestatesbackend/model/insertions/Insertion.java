package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;

public interface Insertion {
    public Long getId();
    public Address getAddress();
    public InsertionDetails getDetails();
    public RealEstateAgent getUploader();
    public RealEstateAgency getAgency();
}
