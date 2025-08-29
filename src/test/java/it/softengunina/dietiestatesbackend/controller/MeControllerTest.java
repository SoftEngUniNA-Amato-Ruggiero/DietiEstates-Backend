package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MeController.class)
@AutoConfigureMockMvc(addFilters = false)
class MeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BaseUserRepository<BaseUser> userRepository;

    @MockitoBean
    RealEstateAgentRepository<RealEstateAgent> agentRepository;

    @MockitoBean
    TokenService tokenService;

    BaseUser user;
    RealEstateAgent agent;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("TestIban", "TestAgencyName");
        user = new BaseUser("testUsername", "testCognitoSub");
        agent = new RealEstateAgent("agentUsername", "agentCognitoSub", agency);

        Mockito.when(userRepository.findByCognitoSub(user.getCognitoSub())).thenReturn(Optional.of(user));

        Mockito.when(userRepository.findByCognitoSub("wrongCognitoSub")).thenReturn(Optional.empty());

        Mockito.when(userRepository.findByCognitoSub(agent.getCognitoSub())).thenReturn(Optional.of(agent));

        Mockito.when(agentRepository.findByCognitoSub(agent.getCognitoSub())).thenReturn(Optional.of(agent));
    }

    @Test
    void getRole_User() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(user.getCognitoSub());

        mockMvc.perform(get("/me/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.role").value(user.getRole()));
    }

    @Test
    void getRole_Agent() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(agent.getCognitoSub());

        mockMvc.perform(get("/me/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(agent.getUsername()))
                .andExpect(jsonPath("$.role").value(agent.getRole()));
    }

    @Test
    void getRole_UserNotFound() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn("wrongCognitoSub");

        mockMvc.perform(get("/me/role"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAgency_User() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(user.getCognitoSub());

        mockMvc.perform(get("/me/agency"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAgency_Agent() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(agent.getCognitoSub());

        mockMvc.perform(get("/me/agency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban").value(agency.getIban()))
                .andExpect(jsonPath("$.name").value(agency.getName()));
    }

    @Test
    void getAgency_UserNotFound() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn("wrongCognitoSub");

        mockMvc.perform(get("/me/agency"))
                .andExpect(status().isNotFound());
    }
}