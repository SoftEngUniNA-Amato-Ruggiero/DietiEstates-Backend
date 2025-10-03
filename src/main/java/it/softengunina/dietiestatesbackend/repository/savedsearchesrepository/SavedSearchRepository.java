package it.softengunina.dietiestatesbackend.repository.savedsearchesrepository;

import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearch;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedSearchRepository<T extends SavedSearch> extends JpaRepository<T,Long> {
    Page<T> findByUser(BaseUser user, Pageable pageable);
}
