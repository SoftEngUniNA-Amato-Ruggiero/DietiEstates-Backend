package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.insertions.*;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForRentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Rollback
@AutoConfigureMockMvc(addFilters = false)
class InsertionControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    InsertionForRentRepository repository;
    @Autowired
    RealEstateAgentRepository agentRepository;
    @Autowired
    RealEstateAgencyRepository agencyRepository;

    @Mock
    Address address;
    @Mock
    InsertionDetails details;

    AutoCloseable mocks;

    RealEstateAgency agency;
    RealEstateAgent uploader;
    InsertionForRent insertion;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        agency = agencyRepository.save(new RealEstateAgency("testIban", "testAgency"));
        uploader = agentRepository.save(new RealEstateAgent(new BaseUser("agent", "sub"), agency));
        insertion = repository.save(new InsertionForRent(address, details, uploader, 800.0));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void getInsertions() throws Exception {
        mockMvc.perform(get("/insertions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0]").exists());
    }

    @Test
    void getInsertionById() throws Exception {
        mockMvc.perform(get("/insertions/" + insertion.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(insertion.getId()));
    }
}