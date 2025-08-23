package it.softengunina.userservice.controller;

import it.softengunina.userservice.model.RealEstateAgency;
import it.softengunina.userservice.model.RealEstateAgent;
import it.softengunina.userservice.model.RealEstateManager;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.RealEstateAgencyRepository;
import it.softengunina.userservice.repository.RealEstateAgentRepository;
import it.softengunina.userservice.repository.RealEstateManagerRepository;
import it.softengunina.userservice.repository.UserRepository;
import it.softengunina.userservice.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AgencyController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = it.softengunina.userservice.filter.JwtRequestFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AgencyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    RealEstateAgencyRepository agencyRepository;
    @MockitoBean
    RealEstateManagerRepository managerRepository;
    @MockitoBean
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    @MockitoBean
    UserRepository<User> userRepository;
    @MockitoBean
    TokenService tokenService;

    RealEstateAgency agency;
    RealEstateManager manager;
    User user;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("testIban", "testAgency");
        agency.setId(1L);
        manager = new RealEstateManager("managerEmail", "managerSub", new RealEstateAgency("differentIban", "differentAgency"));
        manager.setId(1L);
        user = new User("testEmail", "testSub");
        user.setId(2L);

        Mockito.when(agencyRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(agency)));

        Mockito.when(agencyRepository.findById(agency.getId())).thenReturn(Optional.of(agency));
        Mockito.when(agencyRepository.findById(9L)).thenReturn(Optional.empty());
    }

    @Test
    void getAgencies() throws Exception {
        mockMvc.perform(get("/agencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(agency.getName()))
                .andExpect(jsonPath("$.content[0].iban").value(agency.getIban()));
    }

    @Test
    void getAgencyById() throws Exception {
        mockMvc.perform(get("/agencies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(agency.getName()))
                .andExpect(jsonPath("$.iban").value(agency.getIban()));
    }

    @Test
    void getAgencyById_NotFound() throws Exception {
        mockMvc.perform(get("/agencies/2"))
                .andExpect(status().isNotFound());
    }
}