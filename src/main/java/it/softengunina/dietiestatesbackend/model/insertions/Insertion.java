package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.User;
import lombok.NonNull;

import java.util.Set;

public interface Insertion {
    Long getId();
    String getDescription();
    Set<Tag> getTags();
    Address getAddress();
    User getUploader();
    RealEstateAgency getAgency();
    Double getSize();
    Integer getNumberOfRooms();
    Integer getFloor();
    Boolean getHasElevator();

    void setTags(@NonNull Set<Tag> tags);
    boolean hasTag(@NonNull Tag tag);
    boolean addTag(@NonNull Tag tag);
    boolean removeTag(@NonNull Tag tag);
    boolean addAllTagsFromSet(@NonNull Set<Tag> tags);
    boolean removeAllTagsFromSet(@NonNull Set<Tag> tags);
    void clearTags();
}
