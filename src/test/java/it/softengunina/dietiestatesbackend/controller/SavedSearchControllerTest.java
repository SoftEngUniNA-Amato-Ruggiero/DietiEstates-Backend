package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionSearchResultDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForRentDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForSaleDTO;
import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearch;
import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearchForRent;
import it.softengunina.dietiestatesbackend.model.savedsearches.SavedSearchForSale;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SavedSearchControllerTest {
    @Autowired
    SavedSearchController savedSearchController;
    @Autowired
    BaseUserRepository userRepository;

    BaseUser user;

    @BeforeEach
    void setUp() {
        user = userRepository.findById(10L)
                .orElse(null);
    }

    @Test
    void getAllSavedSearches() {
        Page<SavedSearch> res = savedSearchController.getAllSavedSearches(Pageable.unpaged(), user);
        assertAll (
                () -> assertNotNull(res),
                () -> assertTrue(res.hasContent())
        );
    }

    @Test
    void getSavedSearchById() {
        SavedSearch res = savedSearchController.getSavedSearchById(10L, user);
        assertAll (
                () -> assertNotNull(res),
                () -> assertEquals(10L, res.getId())
        );
    }

    @Test
    void executeSavedSearchById() {
        Page<InsertionSearchResultDTO> res = savedSearchController.executeSavedSearchById(10L, Pageable.unpaged(), user);
        assertNotNull(res);
    }

    @Test
    void createSavedSearch() {
        SearchRequestDTO req = SearchRequestDTO.builder()
                .lat(40.0)
                .lng(16.0)
                .distance(10.0)
                .build();

        Point geometry = new GeometryFactory().createPoint(new org.locationtech.jts.geom.Coordinate(16.0, 40.0));

        SavedSearch res = savedSearchController.createSavedSearch(req, user);
        assertAll (
                () -> assertNotNull(res),
                () -> assertEquals(geometry, res.getGeometry()),
                () -> assertEquals(10.0, res.getDistance())
        );
    }

    @Test
    void createSavedSearchForSale() {
        SearchRequestForSaleDTO req = SearchRequestForSaleDTO.searchRequestForSaleDTOBuilder()
                .lat(40.0)
                .lng(16.0)
                .distance(10.0)
                .maxPrice(80000.0)
                .build();

        Point geometry = new GeometryFactory().createPoint(new org.locationtech.jts.geom.Coordinate(16.0, 40.0));

        SavedSearchForSale res = savedSearchController.createSavedSearchForSale(req, user);
        assertAll (
                () -> assertNotNull(res),
                () -> assertEquals(geometry, res.getGeometry()),
                () -> assertEquals(10.0, res.getDistance()),
                () -> assertEquals(80000.0, res.getMaxPrice())
        );
    }

    @Test
    void createSavedSearchForRent() {
        SearchRequestForRentDTO req = SearchRequestForRentDTO.searchRequestForRentDTOBuilder()
                .lat(40.0)
                .lng(16.0)
                .distance(10.0)
                .maxRent(800.0)
                .build();

        Point geometry = new GeometryFactory().createPoint(new org.locationtech.jts.geom.Coordinate(16.0, 40.0));

        SavedSearchForRent res = savedSearchController.createSavedSearchForRent(req, user);
        assertAll (
                () -> assertNotNull(res),
                () -> assertEquals(geometry, res.getGeometry()),
                () -> assertEquals(10.0, res.getDistance()),
                () -> assertEquals(800.0, res.getMaxRent())
        );
    }
}