package it.softengunina.dietiestatesbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.exceptions.UserIsAlreadyAffiliatedWithAgencyException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.services.UserNotAffiliatedWithAgencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AgencyController.class)
@AutoConfigureMockMvc(addFilters = false)
class AgencyControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RealEstateAgencyRepository agencyRepository;
    @MockitoBean
    RealEstateAgentRepository agentRepository;
    @MockitoBean
    RealEstateManagerRepository managerRepository;
    @MockitoBean
    UserNotAffiliatedWithAgencyService userFinderService;
    @MockitoBean
    TokenService tokenService;

    Long agencyId;
    RealEstateAgency agency;
    BaseUser user;
    RealEstateAgent agent;
    RealEstateManager managerOfDifferentAgency;

    @BeforeEach
    void setUp() {
        agencyId = 1L;
        agency = new RealEstateAgency("testIban", "testAgency");
        user = new BaseUser("testEmail", "testSub");
        agent = new RealEstateAgent(new BaseUser("agentEmail", "agentSub"), agency);
        managerOfDifferentAgency = new RealEstateManager(new BaseUser("managerEmail", "managerSub"), new RealEstateAgency("differentIban", "differentAgency"));
    }

    @Test
    void getAgencies() throws Exception {
        Mockito.when(agencyRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(agency)));

        mockMvc.perform(get("/agencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(agency.getName()))
                .andExpect(jsonPath("$.content[0].iban").value(agency.getIban()));
    }

    @Test
    void getAgencyById() throws Exception {
        Mockito.when(agencyRepository.findById(agencyId)).thenReturn(Optional.of(agency));

        mockMvc.perform(get("/agencies/" + agencyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(agency.getName()))
                .andExpect(jsonPath("$.iban").value(agency.getIban()));
    }

    @Test
    void getAgencyById_NotFound() throws Exception {
        Mockito.when(agencyRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/agencies/" + 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAgentsByAgencyId() throws Exception {
        Mockito.when(agencyRepository.findById(agencyId)).thenReturn(Optional.of(agency));
        Mockito.when(agentRepository.findByAgency(Mockito.eq(agency), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(agent)));

        mockMvc.perform(get("/agencies/" + agencyId + "/agents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].user.username").value(agent.getUsername()));
    }

    @Test
    void createAgency() throws Exception {
        RealEstateAgencyDTO req = new RealEstateAgencyDTO(null, "requestIban", "requestAgency");
        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(tokenService.getCognitoSub()).thenReturn(user.getCognitoSub());
        Mockito.when(userFinderService.findByCognitoSub(user.getCognitoSub())).thenReturn(Optional.of(user));
        Mockito.when(agencyRepository.saveAndFlush(Mockito.any(RealEstateAgency.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(managerRepository.save(Mockito.any(RealEstateManager.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/agencies")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAgency_whenUserIsAlreadyAgent() throws Exception {
        RealEstateAgencyDTO req = new RealEstateAgencyDTO(null, "requestIban", "requestAgency");
        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(tokenService.getCognitoSub()).thenReturn(managerOfDifferentAgency.getCognitoSub());
        Mockito.when(userFinderService.findByCognitoSub(managerOfDifferentAgency.getCognitoSub()))
                        .thenThrow(new UserIsAlreadyAffiliatedWithAgencyException(""));

        mockMvc.perform(post("/agencies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}