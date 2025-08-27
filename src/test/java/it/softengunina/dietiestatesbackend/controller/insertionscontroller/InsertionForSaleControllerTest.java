package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithPriceDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.insertions.Address;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForSaleRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
    InsertionForSaleRepository insertionForSaleRepository;
    @MockitoBean
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    @MockitoBean
    TokenService tokenService;

    Long insertionId;
    Address address;
    InsertionDetails details;
    Double price;

    RealEstateAgency testAgency;
    RealEstateAgent testAgent;
    InsertionWithPriceDTO testReq;

    @BeforeEach
    void setUp() {
        insertionId = 1L;
        address = Mockito.mock(Address.class);
        details = Mockito.mock(InsertionDetails.class);
        price = 100000.0;

        testAgency = new RealEstateAgency("testIban", "testAgency");
        testAgent = new RealEstateAgent("testUsername", "testSub", testAgency);
        testReq = new InsertionWithPriceDTO();
        testReq.setAddress(address);
        testReq.setDetails(details);
        testReq.setPrice(price);
        testReq.setUploader(new UserDTO(testAgent));

        Mockito.when(agentRepository.findByCognitoSub(testAgent.getCognitoSub())).thenReturn(Optional.of(testAgent));
        Mockito.when(insertionForSaleRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createInsertion() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(testAgent.getCognitoSub());
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/insertions/for-sale")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(testReq)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.address").exists())
            .andExpect(jsonPath("$.details").exists())
            .andExpect(jsonPath("$.price").value(price))
            .andExpect(jsonPath("$.uploader.username").value(testAgent.getUsername()));
    }
}