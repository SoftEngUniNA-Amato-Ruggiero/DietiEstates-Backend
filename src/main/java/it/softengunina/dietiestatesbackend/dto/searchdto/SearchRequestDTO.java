package it.softengunina.dietiestatesbackend.dto.searchdto;

import lombok.Data;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.Set;

@Data
public class SearchRequestDTO {
    private Double lat;
    private Double lng;
    private Double distance;
    private Double minSize;
    private Integer minNumberOfRooms;
    private Integer maxFloor;
    private Boolean hasElevator;
    private String tags;

    public Point getPoint() {
        Point point = new GeometryFactory().createPoint(new Coordinate(this.getLng(), this.getLat()));
        point.setSRID(4326);
        return point;
    }

    public Set<String> getTagsSet() {
        return (this.getTags() == null || this.getTags().isEmpty()) ? Set.of() : Set.of(this.getTags().split(","));
    }
}
