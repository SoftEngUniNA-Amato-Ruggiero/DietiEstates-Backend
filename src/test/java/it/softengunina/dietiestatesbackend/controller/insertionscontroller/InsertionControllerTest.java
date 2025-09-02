package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithRentDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.insertions.*;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
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
    @MockitoBean
    InsertionDTOVisitorImpl visitor;

    BaseInsertion insertion;
    UserWithAgency uploader;
    RealEstateAgency agency;
    Long insertionId;

    @BeforeEach
    void setUp() {
        insertionId = 1L;
        agency = new RealEstateAgency("iban", "agencyName");
        uploader = new RealEstateAgent(new BaseUser("username", "sub"), agency);
        insertion = new InsertionForRent(new Address(), new InsertionDetails(), uploader.getUser(), uploader.getAgency(), 900.0);
    }

    @Test
    void getInsertions() throws Exception {
        Mockito.when(repository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(insertion)));

        Mockito.when(visitor.visit(Mockito.any(InsertionForRent.class)))
                        .thenAnswer(i -> new InsertionWithRentDTO(i.getArgument(0, InsertionForRent.class)));

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