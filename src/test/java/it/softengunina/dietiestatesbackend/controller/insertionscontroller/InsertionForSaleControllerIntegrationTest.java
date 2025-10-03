package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.InsertionForSaleRequestDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionResponseDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionWithPriceResponseDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestForSaleDTO;
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
class InsertionForSaleControllerIntegrationTest {
    @Autowired
    InsertionForSaleController insertionForSaleController;

    @MockitoBean
    TokenService tokenService;

    String agentSub;
    FeatureCollection addressReq;
    Set<String> tagsReq;
    InsertionForSaleRequestDTO req;

    SearchRequestForSaleDTO queryParams;

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
        req = InsertionForSaleRequestDTO.builder()
                .address(addressReq)
                .description("Description")
                .tags(tagsReq)
                .size(100.0)
                .numberOfRooms(3)
                .floor(3)
                .hasElevator(true)
                .price(250000.0)
                .build();
    }

    @Test
    void createInsertionForSale() {
        InsertionWithPriceResponseDTO res = insertionForSaleController.createInsertionForSale(req);

        assertAll(
                () -> assertNotNull(res),
                () -> assertNotNull(res.getId()),
                () -> assertEquals(req.getDescription(), res.getDescription()),
                () -> assertEquals(req.getSize(), res.getSize()),
                () -> assertEquals(req.getNumberOfRooms(), res.getNumberOfRooms()),
                () -> assertEquals(req.getFloor(), res.getFloor()),
                () -> assertEquals(req.getHasElevator(), res.getHasElevator()),
                () -> assertEquals(req.getPrice(), res.getPrice()),
                () -> assertEquals(req.getTags(), res.getTags()),
                () -> assertNotNull(res.getAddress())
        );
    }

    @Test
    void searchInsertionsForSale_WithTags() {
        queryParams = SearchRequestForSaleDTO.builder()
                .lat(20.0)
                .lng(10.0)
                .distance(10.0)
                .tags("Garden")
                .minSize(50.0)
                .minNumberOfRooms(2)
                .maxFloor(5)
                .hasElevator(true)
                .maxPrice(300000.0)
                .build();

        Page<InsertionResponseDTO> page = insertionForSaleController.searchInsertionsForSale(queryParams, null);

        assertAll(
                () -> assertNotNull(page),
                () -> assertFalse(page.isEmpty()),
                () -> assertTrue(page.stream().allMatch(i ->
                        i.getSize() >= queryParams.getMinSize() &&
                        i.getNumberOfRooms() >= queryParams.getMinNumberOfRooms() &&
                        i.getFloor() <= queryParams.getMaxFloor() &&
                        i.getHasElevator().equals(queryParams.getHasElevator()) &&
                        i.getTags().containsAll(List.of(queryParams.getTags().split(",")))
                ))
        );
    }

    @Test
    void searchInsertionsForSale_WithoutTags() {
        queryParams = SearchRequestForSaleDTO.builder()
                .lat(20.0)
                .lng(10.0)
                .distance(10.0)
                .minSize(50.0)
                .minNumberOfRooms(2)
                .maxFloor(5)
                .hasElevator(true)
                .maxPrice(300000.0)
                .build();

        Page<InsertionResponseDTO> page = insertionForSaleController.searchInsertionsForSale(queryParams, null);

        assertAll(
                () -> assertNotNull(page),
                () -> assertFalse(page.isEmpty()),
                () -> assertTrue(page.stream().allMatch(i ->
                        i.getSize() >= queryParams.getMinSize() &&
                                i.getNumberOfRooms() >= queryParams.getMinNumberOfRooms() &&
                                i.getFloor() <= queryParams.getMaxFloor() &&
                                i.getHasElevator().equals(queryParams.getHasElevator())
                ))
        );
    }
}