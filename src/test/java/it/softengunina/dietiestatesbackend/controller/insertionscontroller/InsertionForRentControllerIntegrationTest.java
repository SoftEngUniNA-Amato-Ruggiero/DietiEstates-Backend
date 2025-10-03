package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.InsertionForRentRequestDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionResponseDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionWithRentResponseDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForRentDTO;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InsertionForRentControllerIntegrationTest {
    @Autowired
    InsertionForRentController insertionForRentController;

    @MockitoBean
    TokenService tokenService;

    String agentSub;
    FeatureCollection addressReq;
    Set<String> tagsReq;
    InsertionForRentRequestDTO req;

    SearchRequestForRentDTO queryParams;

    @BeforeEach
    void setUp() {
        agentSub = "agent1Sub";
        Mockito.when(tokenService.getCognitoSub()).thenReturn(agentSub);

        Feature feature = Mockito.mock(Feature.class);
        Mockito.when(feature.getProperties()).thenReturn(Map.of(
                "street", "Main Street",
                "city", "Springfield",
                "state", "IL",
                "zipCode", "62701",
                "country", "USA",
                "lat", 39.7817,
                "lon", -89.6501,
                "location", new Point(39.7817, -89.6501)
        ));

        addressReq = Mockito.mock(FeatureCollection.class);
        Mockito.when(addressReq.getFeatures()).thenReturn(List.of(feature));

        tagsReq = Set.of("tag1", "tag2", "tag3");
        req = InsertionForRentRequestDTO.builder()
                .address(addressReq)
                .description("Description")
                .tags(tagsReq)
                .size(100.0)
                .numberOfRooms(3)
                .floor(3)
                .hasElevator(true)
                .rent(250000.0)
                .build();
    }

    @Test
    void createInsertionForSale() {
        InsertionWithRentResponseDTO res = insertionForRentController.createInsertionForRent(req);

        assertAll(
                () -> assertNotNull(res),
                () -> assertNotNull(res.getId()),
                () -> assertEquals(req.getDescription(), res.getDescription()),
                () -> assertEquals(req.getSize(), res.getSize()),
                () -> assertEquals(req.getNumberOfRooms(), res.getNumberOfRooms()),
                () -> assertEquals(req.getFloor(), res.getFloor()),
                () -> assertEquals(req.getHasElevator(), res.getHasElevator()),
                () -> assertEquals(req.getRent(), res.getRent()),
                () -> assertEquals(req.getTags(), res.getTags()),
                () -> assertNotNull(res.getAddress())
        );
    }

    @Test
    void searchInsertionsForSale_WithTags() {
        queryParams = SearchRequestForRentDTO.searchRequestForRentDTOBuilder()
                .lat(40.0)
                .lng(55.0)
                .distance(50.0)
                .tags("Garage")
                .minSize(50.0)
                .minNumberOfRooms(2)
                .maxRent(300000.0)
                .build();

        Page<InsertionResponseDTO> page = insertionForRentController.searchInsertionsForRent(queryParams, null);

        assertAll(
                () -> assertNotNull(page),
                () -> assertFalse(page.isEmpty()),
                () -> assertTrue(page.stream().allMatch(i ->
                        i.getSize() >= queryParams.getMinSize() &&
                                i.getNumberOfRooms() >= queryParams.getMinNumberOfRooms() &&
                                i.getTags().containsAll(List.of(queryParams.getTags().split(",")))
                ))
        );
    }

    @Test
    void searchInsertionsForSale_WithoutTags() {
        queryParams = SearchRequestForRentDTO.searchRequestForRentDTOBuilder()
                .lat(40.0)
                .lng(55.0)
                .distance(50.0)
                .minSize(50.0)
                .minNumberOfRooms(2)
                .maxRent(300000.0)
                .build();

        Page<InsertionResponseDTO> page = insertionForRentController.searchInsertionsForRent(queryParams, null);

        assertAll(
                () -> assertNotNull(page),
                () -> assertFalse(page.isEmpty()),
                () -> assertTrue(page.stream().allMatch(i ->
                        i.getSize() >= queryParams.getMinSize() &&
                        i.getNumberOfRooms() >= queryParams.getMinNumberOfRooms()
                ))
        );
    }
}