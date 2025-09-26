package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.User;
import lombok.NonNull;

import java.util.Set;

public interface Insertion {
    Long getId();
    String getDescription();
    Set<String> getTags();
    Address getAddress();
    User getUploader();
    RealEstateAgency getAgency();

    boolean hasTag(@NonNull String tag);
    boolean addTag(@NonNull String tag);
    boolean removeTag(@NonNull String tag);
    boolean addAllTagsFromSet(@NonNull Set<String> tags);
    boolean removeAllTagsFromSet(@NonNull Set<String> tags);
    void clearTags();
}
