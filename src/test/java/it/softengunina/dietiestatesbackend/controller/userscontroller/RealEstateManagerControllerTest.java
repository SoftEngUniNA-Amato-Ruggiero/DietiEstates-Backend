package it.softengunina.dietiestatesbackend.controller.userscontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
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
    BaseUserRepository userRepository;
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
        manager = new RealEstateManager(new BaseUser("managerName", "managerSub"), agency);
        user = new BaseUser("userName", "userSub");

        Mockito.when(managerRepository.save(Mockito.any(RealEstateManager.class))).thenAnswer(i -> i.getArgument(0));


        Mockito.when(managerRepository.findByUser_CognitoSub("wrongSub")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername("wrongUsername")).thenReturn(Optional.empty());

    }

    @Test
    void createManager() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO req = new UserDTO(user);

        Mockito.when(tokenService.getCognitoSub()).thenReturn(manager.getCognitoSub());
        Mockito.when(managerRepository.findByUser_CognitoSub(manager.getCognitoSub())).thenReturn(Optional.of(manager));
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/managers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.username").value(user.getUsername()))
                .andExpect(jsonPath("$.agency.name").value(agency.getName()))
                .andExpect(jsonPath("$.agency.iban").value(agency.getIban()))
                .andExpect(jsonPath("$.role").value("RealEstateManager"));
    }
}