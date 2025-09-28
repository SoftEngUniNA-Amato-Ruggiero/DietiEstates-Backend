package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionResponseDTO;
import it.softengunina.dietiestatesbackend.listeners.InsertionTagListener;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.Tag;
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
@EntityListeners(InsertionTagListener.class)
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

    protected BaseInsertion(@NonNull String description,
                            @NonNull Set<Tag> tags,
                            @NonNull Address address,
                            @NonNull BaseUser uploader,
                            @NonNull RealEstateAgency agency) {
        this.description = description;
        this.tags.addAll(tags);
        this.address = address;
        this.uploader = uploader;
        this.agency = agency;
    }

    public abstract InsertionResponseDTO accept(InsertionDTOVisitor visitor);

    @Override
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public void setTags(@NonNull Set<Tag> tags) {
        this.clearTags();
        this.addAllTagsFromSet(tags);
    }

    @Override
    public boolean hasTag(@NonNull Tag tag) {
        return this.tags.contains(tag);
    }

    @Override
    public boolean addTag(@NonNull Tag tag) {
        return this.tags.add(tag);
    }

    @Override
    public boolean addAllTagsFromSet(@NonNull Set<Tag> tags) {
        return this.tags.addAll(tags);
    }

    @Override
    public boolean removeTag(@NonNull Tag tag) {
        return this.tags.remove(tag);
    }

    @Override
    public boolean removeAllTagsFromSet(@NonNull Set<Tag> tags) {
        return this.tags.removeAll(tags);
    }

    @Override
    public void clearTags() {
        this.tags.clear();
    }
}
