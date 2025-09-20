package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.UserIsAlreadyAffiliatedWithAgencyException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserNotAffiliatedWithAgencyServiceTest {
    @Mock
    BaseUserRepository userRepository;
    @Mock
    BusinessUserRepository agentRepository;

    AutoCloseable mocks;

    UserNotAffiliatedWithAgencyService service;
    BaseUser user;
    RealEstateAgent agent;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        service = new UserNotAffiliatedWithAgencyService(userRepository, agentRepository);
        user = new BaseUser("baseUserName", "baseUserCognitoSub");
        agency = new RealEstateAgency("agencyIban", "agencyName");
        agent = new RealEstateAgent(new BusinessUser(new BaseUser("agentUserName", "agentUserCognitoSub"), agency));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void findByCognitoSub() {
        Mockito.when(agentRepository.findByUser_CognitoSub(user.getCognitoSub())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByCognitoSub(user.getCognitoSub())).thenReturn(Optional.of(user));

        Optional<BaseUser> foundUser = service.findByCognitoSub(user.getCognitoSub());
        assertTrue(foundUser.isPresent());
    }

    @Test
    void findByCognitoSub_WhenAgent() {
        Mockito.when(agentRepository.findByUser_CognitoSub(agent.getCognitoSub())).thenReturn(Optional.of(agent.getBusinessUser()));
        Mockito.when(userRepository.findByCognitoSub(agent.getCognitoSub())).thenReturn(Optional.of(agent.getUser()));

        String cognitoSub = agent.getCognitoSub();
        assertThrows(UserIsAlreadyAffiliatedWithAgencyException.class, () -> service.findByCognitoSub(cognitoSub));
    }

    @Test
    void findByUsername() {
        Mockito.when(agentRepository.findByUser_Username(user.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(user.getCognitoSub())).thenReturn(Optional.of(user));

        Optional<BaseUser> foundUser = service.findByUsername(user.getCognitoSub());
        assertTrue(foundUser.isPresent());
    }

    @Test
    void findByUsername_WhenAgent() {
        Mockito.when(agentRepository.findByUser_Username(agent.getUsername())).thenReturn(Optional.of(agent.getBusinessUser()));
        Mockito.when(userRepository.findByUsername(agent.getUsername())).thenReturn(Optional.of(agent.getUser()));

        String username = agent.getUsername();
        assertThrows(UserIsAlreadyAffiliatedWithAgencyException.class, () -> service.findByUsername(username));
    }
}