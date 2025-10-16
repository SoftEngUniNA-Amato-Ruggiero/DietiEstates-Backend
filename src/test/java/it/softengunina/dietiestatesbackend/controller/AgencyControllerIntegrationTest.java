package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyRequestDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.BusinessUserResponseDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserResponseDTO;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AgencyControllerIntegrationTest {
    @Autowired
    AgencyController agencyController;
    @Autowired
    BaseUserRepository userRepository;

    @Test
    void getAgentsByAgencyId() {
        Page<UserResponseDTO> res = agencyController.getAgentsByAgencyId(10L, null);

        assertNotNull(res);
        assertTrue(res.hasContent());
        assertEquals(1, res.getTotalElements());
    }

    @Test
    void createAgency() {
        BaseUser user = userRepository.findById(10L).orElseThrow();
        RealEstateAgencyRequestDTO req = new RealEstateAgencyRequestDTO("1234567890", "New Agency");

        BusinessUserResponseDTO res = agencyController.createAgency(req, user);

        assertNotNull(res);
        assertEquals("New Agency", res.getAgency().getName());
        assertEquals("1234567890", res.getAgency().getIban());
    }
}