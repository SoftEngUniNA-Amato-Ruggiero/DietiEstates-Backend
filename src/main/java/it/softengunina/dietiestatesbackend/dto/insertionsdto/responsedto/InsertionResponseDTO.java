package it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto;

import it.softengunina.dietiestatesbackend.dto.AddressDTO;
import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyRequestDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserResponseDTO;

import java.util.Set;

public interface InsertionResponseDTO {
    Long getId();
    String getDescription();
    Set<String> getTags();
    AddressDTO getAddress();
    UserResponseDTO getUploader();
    RealEstateAgencyRequestDTO getAgency();
    Double getSize();
    Integer getNumberOfRooms();
    Integer getFloor();
    Boolean getHasElevator();
}
