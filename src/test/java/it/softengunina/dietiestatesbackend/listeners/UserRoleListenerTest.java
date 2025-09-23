package it.softengunina.dietiestatesbackend.listeners;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.Role;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleListenerTest {
    @Mock
    RoleRepository roleRepository;

    AutoCloseable mocks;

    UserRoleListener listener;
    RealEstateAgency agency;
    BaseUser baseAgent;
    RealEstateAgent agent;


    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        listener = new UserRoleListener(roleRepository);
        baseAgent = new BaseUser("baseUserName", "baseUserCognitoSub");
        agency = new RealEstateAgency("agencyIban", "agencyName");
        agent = new RealEstateAgent(new BusinessUser(baseAgent, agency));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void prePersist_IfRoleExists() {
        Role expectedRole = new Role(agent.getClass().getSimpleName());
        Mockito.when(roleRepository.findByName(Mockito.any(String.class))).thenAnswer(i -> Optional.of(new Role( (String)i.getArguments()[0]) ));

        listener.prePersist(agent);

        assertTrue(agent.getRoles().contains(expectedRole));
    }

    @Test
    void prePersist_IfRoleDoesNotExist() {
        Role expectedRole = new Role(agent.getClass().getSimpleName());
        Mockito.when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenAnswer(i -> i.getArguments()[0]);

        listener.prePersist(agent);

        assertTrue(agent.getRoles().contains(expectedRole));
    }
}