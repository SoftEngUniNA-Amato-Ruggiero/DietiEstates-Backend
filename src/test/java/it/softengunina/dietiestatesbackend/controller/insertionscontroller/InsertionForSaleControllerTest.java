package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithPriceDTO;
import it.softengunina.dietiestatesbackend.model.insertions.Address;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForSaleRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InsertionForSaleController.class)
@AutoConfigureMockMvc(addFilters = false)
class InsertionForSaleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    InsertionForSaleRepository repository;
    @MockitoBean
    RealEstateAgentRepository agentRepository;
    @MockitoBean
    TokenService tokenService;

    @Mock
    Address address;
    @Mock
    InsertionDetails details;
    @Mock
    RealEstateAgent uploader;

    AutoCloseable mocks;

    InsertionForSale insertion;
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

        mockMvc.perform(get("/insertions/for-sale"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0]").exists());
    }

    @Test
    void createInsertion() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        InsertionWithPriceDTO testReq = new InsertionWithPriceDTO(insertion);

        Mockito.when(tokenService.getCognitoSub()).thenReturn("uploaderSub");
        Mockito.when(agentRepository.findByUser_CognitoSub("uploaderSub")).thenReturn(Optional.of(uploader));
        Mockito.when(repository.save(Mockito.any(InsertionForSale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/insertions/for-sale")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(testReq)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.address").exists())
            .andExpect(jsonPath("$.details").exists())
            .andExpect(jsonPath("$.uploader").exists())
            .andExpect(jsonPath("$.price").exists());
    }
}