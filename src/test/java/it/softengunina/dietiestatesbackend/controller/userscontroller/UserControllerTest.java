package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
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

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserRepository<BaseUser> userRepository;
    @MockitoBean
    TokenService tokenService;

    BaseUser user;
    RealEstateAgent agent;
    RealEstateAgency agency;
    Long userId = 1L;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("TestIban", "TestAgencyName");
        user = new BaseUser("testUsername", "testCognitoSub");
        agent = new RealEstateAgent("agentUsername", "agentCognitoSub", agency);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByCognitoSub(user.getCognitoSub())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Mockito.when(userRepository.findByUsername("wrongUsername")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByCognitoSub("wrongCognitoSub")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Mockito.when(userRepository.findByCognitoSub(agent.getCognitoSub())).thenReturn(Optional.of(agent));
    }

    @Test
    void getUserByUsername() throws Exception {
        mockMvc.perform(get("/users")
        .param("username", user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void getUserById() throws Exception {
        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void getRole_User() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(user.getCognitoSub());

        mockMvc.perform(get("/users/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.role").value(user.getRole()));
    }

    @Test
    void getRole_Agent() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(agent.getCognitoSub());

        mockMvc.perform(get("/users/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(agent.getUsername()))
                .andExpect(jsonPath("$.role").value(agent.getRole()));
    }

    @Test
    void getRole_NotFound() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn("wrongCognitoSub");

        mockMvc.perform(get("/users/role"))
                .andExpect(status().isNotFound());
    }
}