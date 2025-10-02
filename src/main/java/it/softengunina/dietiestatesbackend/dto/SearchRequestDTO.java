package it.softengunina.dietiestatesbackend.dto;

import lombok.Data;

@Data
public class SearchRequestDTO {
    private Double lat;
    private Double lng;
    private Double distance;
    private Integer minSize;
    private Integer minNumberOfRooms;
    private Integer maxFloor;
    private Boolean hasElevator;
    private String tags;
}
