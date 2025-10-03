package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.AddressDetails;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.insertions.Tag;
import it.softengunina.dietiestatesbackend.model.insertions.*;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.BaseInsertionRepository;
import it.softengunina.dietiestatesbackend.services.SaveSearchService;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InsertionController.class)
@AutoConfigureMockMvc(addFilters = false)
class InsertionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BaseInsertionRepository<BaseInsertion> repository;

    @MockitoBean
    SaveSearchService saveSearchService;

    @MockitoBean
    InsertionDTOVisitorImpl visitor;

    BaseInsertion insertion;
    BusinessUser uploader;
    RealEstateAgency agency;
    Long insertionId;
    SearchRequestDTO searchReq;
    Pageable pageable;

    @BeforeEach
    void setUp() {
        double lat = 44.40565;
        double lng = 8.946256;
        Point point = new GeometryFactory().createPoint(new Coordinate(lng, lat));
        AddressDetails addressDetails = mock(AddressDetails.class);
        Address address = new Address(addressDetails, point);

        double distance = 2.0;

        insertionId = 1L;
        agency = new RealEstateAgency("iban", "agencyName");
        uploader = new BusinessUser(new BaseUser("username", "sub"), agency);
        InsertionDetails details = InsertionDetails.builder().size(80.0).numberOfRooms(3).floor(2).hasElevator(true).build();

        insertion = InsertionForRent.builder()
                .description("description")
                .tags(Tag.fromNames(Set.of("tag1, tag2")))
                .address(address)
                .rent(90000.0)
                .details(details)
                .uploader(uploader.getUser())
                .agency(uploader.getAgency())
                .build();

        Double minSize = null;
        Integer minNumberOfRooms = null;
        Integer maxFloor = null;
        Boolean hasElevator = null;
        searchReq = SearchRequestDTO.builder()
                .lat(lat)
                .lng(lng)
                .distance(distance)
                .minSize(minSize)
                .minNumberOfRooms(minNumberOfRooms)
                .maxFloor(maxFloor)
                .hasElevator(hasElevator)
                .tags("")
                .build();

        int pageNumber = 0;
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);
    }

    @Test
    void getInsertionById() throws Exception {
        Mockito.when(repository.findById(insertionId))
                .thenReturn(Optional.of(insertion));

        mockMvc.perform(get("/insertions/" + insertionId))
                .andExpect(status().isOk());
    }
}