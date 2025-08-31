package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.model.insertions.*;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InsertionController.class)
@AutoConfigureMockMvc(addFilters = false)
class InsertionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    InsertionRepository<BaseInsertion> repository;
    @MockitoBean
    RealEstateAgentRepository agentRepository;
    @MockitoBean
    RealEstateAgencyRepository agencyRepository;

    @Mock
    Address address;
    @Mock
    InsertionDetails details;
    @Mock
    RealEstateAgent uploader;

    AutoCloseable mocks;

    BaseInsertion insertion;
    Long insertionId;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        insertionId = 1L;
        insertion = new InsertionForSale(address, details, uploader, 100000.0);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void getInsertions() throws Exception {
        Mockito.when(repository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(insertion)));

        mockMvc.perform(get("/insertions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0]").exists());
    }

    @Test
    void getInsertionById() throws Exception {
        Mockito.when(repository.findById(insertionId))
                .thenReturn(Optional.of(insertion));

        mockMvc.perform(get("/insertions/" + insertionId))
                .andExpect(status().isOk());
    }
}