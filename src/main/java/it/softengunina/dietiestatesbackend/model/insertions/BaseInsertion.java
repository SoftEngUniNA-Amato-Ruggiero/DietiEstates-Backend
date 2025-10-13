package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionResponseDTO;
import it.softengunina.dietiestatesbackend.listeners.InsertionsListener;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class for a generic type of insertion about real estates, uploaded by a Real Estate Agent.
 */
@Entity
@Table(name = "insertions")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(InsertionsListener.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class BaseInsertion implements Insertion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(name = "description", columnDefinition = "TEXT")
    @Lob
    @Getter
    @Setter
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "insertion_tags",
            joinColumns = @JoinColumn(name = "insertion_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private final Set<Tag> tags = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NotNull
    @Getter
    @Setter
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploader_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @NotNull
    @Getter
    @Setter
    private BaseUser uploader;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agency_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @NotNull
    @Getter
    @Setter
    private RealEstateAgency agency;

    @Embedded
    @Getter
    @Setter
    private InsertionDetails details;

    protected BaseInsertion(String description,
                            Set<Tag> tags,
                            @NonNull Address address,
                            @NonNull BaseUser uploader,
                            @NonNull RealEstateAgency agency,
                            InsertionDetails details) {
        this.description = description;
        this.tags.addAll(tags);
        this.address = address;
        this.uploader = uploader;
        this.agency = agency;
        this.details = details;
    }

    public Double getSize() {
        return details != null ? details.getSize() : null;
    }

    public Integer getNumberOfRooms() {
        return details != null ? details.getNumberOfRooms() : null;
    }

    public Integer getFloor() {
        return details != null ? details.getFloor() : null;
    }

    public Boolean getHasElevator() {
        return details != null ? details.getHasElevator() : null;
    }

    /**
     * Accept method for the producing a response DTO through the InsertionDTOVisitor.
     * @param visitor the InsertionDTOVisitor to accept
     * @return the DTO produced by the visitor
     */
    public abstract InsertionResponseDTO accept(InsertionDTOVisitor visitor);

    /**
     * Returns an unmodifiable view of the tags associated with the insertion.
     */
    @Override
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Replaces the current set of tags with the provided set.
     * @param tags the new set of tags to associate with the insertion
     */
    @Override
    public void setTags(@NonNull Set<Tag> tags) {
        this.clearTags();
        this.addAllTagsFromSet(tags);
    }

    /**
     * Checks if a specific tag is associated with the insertion.
     * @param tag the tag to check for association
     * @return true if the tag is associated with the insertion, false otherwise
     */
    @Override
    public boolean hasTag(@NonNull Tag tag) {
        return this.tags.contains(tag);
    }

    /**
     * Adds a tag to the insertion's set of tags.
     * @param tag the tag to be added
     * @return true if the tag was added successfully, false if it was already present
     */
    @Override
    public boolean addTag(@NonNull Tag tag) {
        return this.tags.add(tag);
    }

    /**
     * Adds multiple tags to the insertion's set of tags.
     * @param tags the set of tags to be added
     * @return true if at least one tag was added successfully, false if all were already present
     */
    @Override
    public boolean addAllTagsFromSet(@NonNull Set<Tag> tags) {
        return this.tags.addAll(tags);
    }

    /**
     * Removes a tag from the insertion's set of tags.
     * @param tag the tag to be removed
     * @return true if the tag was removed successfully, false if it was not present
     */
    @Override
    public boolean removeTag(@NonNull Tag tag) {
        return this.tags.remove(tag);
    }

    /**
     * Removes multiple tags from the insertion's set of tags.
     * @param tags the set of tags to be removed
     * @return true if at least one tag was removed successfully, false if none were present
     */
    @Override
    public boolean removeAllTagsFromSet(@NonNull Set<Tag> tags) {
        return this.tags.removeAll(tags);
    }

    /**
     * Clears all tags associated with the insertion.
     */
    @Override
    public void clearTags() {
        this.tags.clear();
    }
}
