package it.softengunina.dietiestatesbackend.controller.userscontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.strategy.UserPromotionStrategyImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RealEstateManagerController.class)
@AutoConfigureMockMvc(addFilters = false)
class RealEstateManagerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BaseUserRepository<BaseUser> userRepository;
    @MockitoBean
    RealEstateManagerRepository managerRepository;
    @MockitoBean
    TokenService tokenService;
    @MockitoBean
    UserPromotionStrategyImpl promotionStrategy;

    RealEstateAgency agency;
    RealEstateManager manager;
    BaseUser user;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("agencyIban", "agencyName");
        manager = new RealEstateManager("managerName", "managerSub", agency);
        user = new BaseUser("userName", "userSub");

        Mockito.when(managerRepository.findByCognitoSub(manager.getCognitoSub())).thenReturn(Optional.of(manager));
        Mockito.when(managerRepository.findByCognitoSub("wrongSub")).thenReturn(Optional.empty());

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("wrongUsername")).thenReturn(Optional.empty());
    }

    @Test
    void createManager() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(manager.getCognitoSub());

        Mockito.when(promotionStrategy.promoteUserToAgent(Mockito.eq(user), Mockito.any(RealEstateAgency.class)))
                .thenAnswer(invocation -> new RealEstateAgent(user.getUsername(), user.getCognitoSub(), invocation.getArgument(1)));

        Mockito.when(promotionStrategy.promoteAgentToManager(Mockito.any(RealEstateAgent.class)))
                .thenAnswer(invocation -> {
                    RealEstateAgent agent = invocation.getArgument(0);
                    return new RealEstateManager(agent.getUsername(), agent.getCognitoSub(), agent.getAgency());
                });

        UserDTO req = new UserDTO(user);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/managers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.username").value(user.getUsername()))
                .andExpect(jsonPath("$.agency.name").value(agency.getName()))
                .andExpect(jsonPath("$.agency.iban").value(agency.getIban()))
                .andExpect(jsonPath("$.user.role").value("RealEstateManager"));
    }
}