package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithPriceDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.insertions.*;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForSaleRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    BusinessUserRepository businessUserRepository;
    @MockitoBean
    InsertionDTOVisitorImpl visitor;
    @MockitoBean
    TokenService tokenService;

    InsertionForSale insertion;
    BusinessUser uploader;
    RealEstateAgency agency;
    Long insertionId;

    @BeforeEach
    void setUp() {
        insertionId = 1L;
        agency = new RealEstateAgency("iban", "agencyName");
        uploader = new BusinessUser(new BaseUser("username", "sub"), agency);
        insertion = new InsertionForSale(new Address(), new InsertionDetails(), uploader.getUser(), uploader.getAgency(), 90000.0);
    }

    @Test
    void getInsertions() throws Exception {
        Mockito.when(repository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(insertion)));

        Mockito.when(visitor.visit(Mockito.any(InsertionForSale.class)))
                .thenAnswer(i -> new InsertionWithPriceDTO(i.getArgument(0, InsertionForSale.class)));

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
        Mockito.when(businessUserRepository.findByUser_CognitoSub("uploaderSub")).thenReturn(Optional.of(uploader));
        Mockito.when(repository.save(Mockito.any(InsertionForSale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mockito.when(visitor.visit(Mockito.any(InsertionForSale.class)))
                .thenAnswer(i -> new InsertionWithPriceDTO(i.getArgument(0, InsertionForSale.class)));

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