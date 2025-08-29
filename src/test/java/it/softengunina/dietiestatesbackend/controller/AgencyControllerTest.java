package it.softengunina.dietiestatesbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.strategy.UserPromotionStrategyImpl;
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
    BaseUserRepository<BaseUser> userRepository;
    @MockitoBean
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    @MockitoBean
    TokenService tokenService;
    @MockitoBean
    UserPromotionStrategyImpl promotionStrategy;

    Long agencyId = 1L;
    RealEstateAgency agency;
    RealEstateManager manager;
    RealEstateAgent agent;
    BaseUser user;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("testIban", "testAgency");
        manager = new RealEstateManager("managerEmail", "managerSub", new RealEstateAgency("differentIban", "differentAgency"));
        agent = new RealEstateAgent("agentEmail", "agentSub", agency);
        user = new BaseUser("testEmail", "testSub");

        Mockito.when(agencyRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(agency)));

        Mockito.when(agencyRepository.findById(agencyId)).thenReturn(Optional.of(agency));
        Mockito.when(agencyRepository.findById(9L)).thenReturn(Optional.empty());

        Mockito.when(agentRepository.findByAgency(Mockito.eq(agency), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(agent)));

        Mockito.when(userRepository.findByCognitoSub(user.getCognitoSub())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByCognitoSub(manager.getCognitoSub())).thenReturn(Optional.of(manager));
        Mockito.when(userRepository.findByCognitoSub("wrongSub")).thenReturn(Optional.empty());

        Mockito.when(agencyRepository.saveAndFlush(Mockito.any(RealEstateAgency.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mockito.when(promotionStrategy.promoteUserToAgent(Mockito.eq(user), Mockito.any(RealEstateAgency.class)))
                .thenAnswer(invocation -> new RealEstateAgent(user.getUsername(), user.getCognitoSub(), invocation.getArgument(1)));

        Mockito.when(promotionStrategy.promoteAgentToManager(Mockito.any(RealEstateAgent.class)))
                .thenAnswer(invocation -> {
                    RealEstateAgent passedAgent = invocation.getArgument(0);
                    return new RealEstateManager(passedAgent.getUsername(), passedAgent.getCognitoSub(), passedAgent.getAgency());
                });
    }

    @Test
    void getAgencies() throws Exception {
        mockMvc.perform(get("/agencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(agency.getName()))
                .andExpect(jsonPath("$.content[0].iban").value(agency.getIban()));
    }

    @Test
    void getAgencyById() throws Exception {
        mockMvc.perform(get("/agencies/" + agencyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(agency.getName()))
                .andExpect(jsonPath("$.iban").value(agency.getIban()));
    }

    @Test
    void getAgencyById_NotFound() throws Exception {
        mockMvc.perform(get("/agencies/" + 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAgentsByAgencyId() throws Exception {
        mockMvc.perform(get("/agencies/" + agencyId + "/agents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value(agent.getUsername()));
    }

    @Test
    void createAgency() throws Exception {
        RealEstateAgencyDTO req = new RealEstateAgencyDTO(null, "requestIban", "requestAgency");
        Mockito.when(tokenService.getCognitoSub()).thenReturn(user.getCognitoSub());
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/agencies")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAgency_whenUserIsAlreadyAgent() throws Exception {
        RealEstateAgencyDTO req = new RealEstateAgencyDTO(null, "requestIban", "requestAgency");
        Mockito.when(tokenService.getCognitoSub()).thenReturn(manager.getCognitoSub());
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/agencies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }
}