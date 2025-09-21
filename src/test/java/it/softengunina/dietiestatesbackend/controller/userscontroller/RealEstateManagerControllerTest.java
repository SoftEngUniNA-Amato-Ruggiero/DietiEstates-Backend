package it.softengunina.dietiestatesbackend.controller.userscontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RealEstateManagerController.class)
@AutoConfigureMockMvc(addFilters = false)
class RealEstateManagerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RealEstateAgentRepository agentRepository;
    @MockitoBean
    RealEstateManagerRepository managerRepository;
    @MockitoBean
    TokenService tokenService;

    RealEstateAgency agency;
    RealEstateManager manager;
    RealEstateAgent agent;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("agencyIban", "agencyName");
        manager = new RealEstateManager(new BusinessUser(new BaseUser("managerName", "managerSub"), agency));
        agent = new RealEstateAgent(new BusinessUser(new BaseUser("userName", "userSub"), agency));
    }

    @Test
    void createManager() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO req = new UserDTO(agent);

        Mockito.when(tokenService.getCognitoSub()).thenReturn(manager.getCognitoSub());
        Mockito.when(managerRepository.findByBusinessUser_User_CognitoSub(manager.getCognitoSub())).thenReturn(Optional.of(manager));
        Mockito.when(agentRepository.findByBusinessUser_AgencyAndBusinessUser_User_Username(manager.getAgency(), agent.getUsername())).thenReturn(Optional.of(agent));
        Mockito.when(managerRepository.save(Mockito.any(RealEstateManager.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(post("/managers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.username").value(agent.getUsername()))
                .andExpect(jsonPath("$.agency.name").value(agency.getName()))
                .andExpect(jsonPath("$.agency.iban").value(agency.getIban()));
    }
}