package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
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
    BusinessUserRepository businessUserRepository;

    @MockitoBean
    TokenService tokenService;

    BaseUser user;
    RealEstateAgency agency;
    RealEstateAgent agent;
    RealEstateManager manager;

    @BeforeEach
    void setUp() {
        user = new BaseUser("testUsername", "testCognitoSub");
        agency = new RealEstateAgency("TestIban", "TestAgencyName");
        agent = new RealEstateAgent(new BusinessUser(new BaseUser("agentUsername", "agentCognitoSub"), agency));
        manager = new RealEstateManager(new BusinessUser(new BaseUser("managerUsername", "managerCognitoSub"), agency));
    }

    @Test
    void getAgency_BaseUser() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(user.getCognitoSub());
        Mockito.when(businessUserRepository.findByUser_CognitoSub(user.getCognitoSub())).thenReturn(Optional.empty());

        mockMvc.perform(get("/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAgency_Agent() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(agent.getCognitoSub());
        Mockito.when(businessUserRepository.findByUser_CognitoSub(agent.getCognitoSub())).thenReturn(Optional.of(agent.getBusinessUser()));

        mockMvc.perform(get("/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agency.iban").value(agency.getIban()))
                .andExpect(jsonPath("$.agency.name").value(agency.getName()))
                .andExpect(jsonPath("$.user.username").value(agent.getUsername()))
                .andExpect(jsonPath("$.user.id").value(agent.getId()));
    }

    @Test
    void getAgency_Manager() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(manager.getCognitoSub());
        Mockito.when(businessUserRepository.findByUser_CognitoSub(manager.getCognitoSub())).thenReturn(Optional.of(manager.getBusinessUser()));

        mockMvc.perform(get("/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agency.iban").value(agency.getIban()))
                .andExpect(jsonPath("$.agency.name").value(agency.getName()))
                .andExpect(jsonPath("$.user.username").value(manager.getUsername()))
                .andExpect(jsonPath("$.user.id").value(manager.getId()));
    }

    @Test
    void getAgency_UserNotFound() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn("wrongCognitoSub");
        Mockito.when(businessUserRepository.findByUser_CognitoSub("wrongCognitoSub")).thenReturn(Optional.empty());

        mockMvc.perform(get("/me"))
                .andExpect(status().isNotFound());
    }
}