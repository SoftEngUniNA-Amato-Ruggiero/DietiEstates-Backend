package it.softengunina.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.userservice.dto.UserDTO;
import it.softengunina.userservice.model.RealEstateAgency;
import it.softengunina.userservice.model.RealEstateAgent;
import it.softengunina.userservice.model.RealEstateManager;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.RealEstateManagerRepository;
import it.softengunina.userservice.repository.UserRepository;
import it.softengunina.userservice.services.PromotionService;
import it.softengunina.userservice.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = RealEstateAgentController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = it.softengunina.userservice.filter.JwtRequestFilter.class
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
    PromotionService promotionService;

    RealEstateAgency agency;
    RealEstateManager manager;
    User user;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("agencyIban", "agencyName");
        agency.setId(1L);
        manager = new RealEstateManager("managerName", "managerSub", agency);
        manager.setId(1L);
        user = new User("userName", "userSub");
        user.setId(2L);

        Mockito.when(managerRepository.findByCognitoSub(manager.getCognitoSub())).thenReturn(Optional.of(manager));
        Mockito.when(managerRepository.findByCognitoSub("wrongSub")).thenReturn(Optional.empty());

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("wrongUsername")).thenReturn(Optional.empty());

        Mockito.doNothing()
                .when(promotionService)
                .verifyUserIsNotAnAgent(user);

        Mockito.doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "User is already an agent"))
                .when(promotionService)
                .verifyUserIsNotAnAgent(manager);

        Mockito.when(promotionService.promoteUserToAgent(Mockito.eq(user), Mockito.any(RealEstateAgency.class)))
                .thenAnswer(invocation -> new RealEstateAgent(user.getUsername(), user.getCognitoSub(), invocation.getArgument(1)));
    }

    @Test
    void createAgent() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(manager.getCognitoSub());
        UserDTO req = new UserDTO(user.getUsername());
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/agents")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.cognitoSub").value(user.getCognitoSub()));
    }
}