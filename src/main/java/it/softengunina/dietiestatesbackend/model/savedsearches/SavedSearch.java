package it.softengunina.dietiestatesbackend.model.savedsearches;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Geometry;

import java.util.Set;

@Entity
@Table(name = "saved_searches")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SavedSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    @Setter
    private BaseUser user;

    @Column(columnDefinition = "geometry(Geometry, 4326)", nullable = false)
    @Getter
    @Setter
    private Geometry geometry;

    @Column(nullable = false)
    @Getter
    @Setter
    private Double distance;

    @Getter
    @Setter
    private Double minSize;

    @Getter
    @Setter
    private Integer minNumberOfRooms;

    @Getter
    @Setter
    private Integer maxFloor;

    @Getter
    @Setter
    private Boolean hasElevator;

    @ElementCollection
    @CollectionTable(name = "search_tags", joinColumns = @JoinColumn(name = "search_id"))
    @Column(name = "search_tag")
    @Getter
    @Setter
    private Set<String> tags;

    @Builder(builderMethodName = "savedSearchBuilder")
    public SavedSearch(BaseUser user, Geometry geometry, Double distance, Double minSize, Integer minNumberOfRooms, Integer maxFloor, Boolean hasElevator, Set<String> tags) {
        this.user = user;
        this.geometry = geometry;
        this.distance = distance;
        this.minSize = minSize;
        this.minNumberOfRooms = minNumberOfRooms;
        this.maxFloor = maxFloor;
        this.hasElevator = hasElevator;
        this.tags = tags;
    }
}
