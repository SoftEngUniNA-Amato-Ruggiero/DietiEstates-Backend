package it.softengunina.dietiestatesbackend.dto.insertionsdto;

public interface InsertionDTO {
    Long getId();

    it.softengunina.dietiestatesbackend.model.insertions.Address getAddress();

    it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails getDetails();

    it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO getUploader();

    it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO getAgency();
}
