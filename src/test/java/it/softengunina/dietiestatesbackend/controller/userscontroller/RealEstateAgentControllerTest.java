package it.softengunina.dietiestatesbackend.controller.userscontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RealEstateAgentController.class)
@AutoConfigureMockMvc(addFilters = false)
class RealEstateAgentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BaseUserRepository userRepository;
    @MockitoBean
    RealEstateAgentRepository agentRepository;
    @MockitoBean
    RealEstateManagerRepository managerRepository;
    @MockitoBean
    TokenService tokenService;

    RealEstateAgency agency;
    RealEstateManager manager;
    BaseUser user;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("agencyIban", "agencyName");
        manager = new RealEstateManager(new BusinessUser(new BaseUser("managerName", "managerSub"), agency));
        user = new BaseUser("userName", "userSub");
    }

    @Test
    void createAgent() throws Exception {
        UserDTO req = new UserDTO(user);
        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(tokenService.getCognitoSub()).thenReturn(manager.getCognitoSub());
        Mockito.when(managerRepository.findByBusinessUser_User_CognitoSub(manager.getCognitoSub())).thenReturn(Optional.of(manager));
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(agentRepository.save(Mockito.any(RealEstateAgent.class))).thenAnswer(i -> i.getArguments()[0]);

        mockMvc.perform(post("/agents")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.username").value(user.getUsername()))
                .andExpect(jsonPath("$.agency.name").value(agency.getName()))
                .andExpect(jsonPath("$.agency.iban").value(agency.getIban()))
                .andExpect(jsonPath("$.user.roles").value(hasItem("RealEstateAgent")));
    }
}