package it.softengunina.dietiestatesbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.dietiestatesbackend.dto.UserDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.repository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.repository.UserRepository;
import it.softengunina.dietiestatesbackend.services.UserPromotionService;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = RealEstateAgentController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = it.softengunina.dietiestatesbackend.filter.JwtRequestFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class RealEstateAgentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserRepository<User> userRepository;
    @MockitoBean
    RealEstateManagerRepository managerRepository;
    @MockitoBean
    TokenService tokenService;
    @MockitoBean
    UserPromotionService promotionService;

    RealEstateAgency agency;
    RealEstateManager manager;
    User user;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("agencyIban", "agencyName");
        manager = new RealEstateManager("managerName", "managerSub", agency);
        user = new User("userName", "userSub");

        Mockito.when(managerRepository.findByCognitoSub(manager.getCognitoSub())).thenReturn(Optional.of(manager));
        Mockito.when(managerRepository.findByCognitoSub("wrongSub")).thenReturn(Optional.empty());

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("wrongUsername")).thenReturn(Optional.empty());
    }

    @Test
    void createAgent() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(manager.getCognitoSub());
        Mockito.when(promotionService.promoteToAgent(Mockito.eq(user), Mockito.any(RealEstateAgency.class)))
                .thenAnswer(invocation -> new RealEstateAgent(user.getUsername(), user.getCognitoSub(), invocation.getArgument(1)));


        UserDTO req = new UserDTO(user);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/agents")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.username").value(user.getUsername()))
                .andExpect(jsonPath("$.agency.name").value(agency.getName()))
                .andExpect(jsonPath("$.agency.iban").value(agency.getIban()))
                .andExpect(jsonPath("$.role").value("RealEstateAgent"));
    }
}