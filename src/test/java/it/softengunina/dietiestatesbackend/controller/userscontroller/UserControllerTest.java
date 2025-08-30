package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
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
    BaseUserRepository userRepository;

    BaseUser user;
    RealEstateAgent agent;
    RealEstateAgency agency;
    Long userId = 1L;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("TestIban", "TestAgencyName");
        user = new BaseUser("testUsername", "testCognitoSub");
        agent = new RealEstateAgent(new BaseUser("agentUsername", "agentCognitoSub"), agency);
    }

    @Test
    void getUserByUsername() throws Exception {
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        mockMvc.perform(get("/users")
        .param("username", user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void getUserById() throws Exception {
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }
}