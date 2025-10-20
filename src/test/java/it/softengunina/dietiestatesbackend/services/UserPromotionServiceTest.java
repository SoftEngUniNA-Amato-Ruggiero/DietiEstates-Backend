package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserPromotionServiceTest {
    @Mock
    BusinessUserRepository businessUserRepository;
    AutoCloseable mocks;

    UserPromotionService userPromotionService;

    BaseUser user;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);
        userPromotionService = new UserPromotionService(businessUserRepository);
        user = new BaseUser("testUser", "testSub");
        agency = new RealEstateAgency("00000000", "Test Agency");
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    /* ------------------ WHITE BOX TEST SUITE ------------------ */
    /* ------------------ promoteToBusinessUser ------------------ */

    @Test
    void promoteToBusinessUser_WhenBusinessUserIsEmpty() {
        Mockito.when(businessUserRepository.findById(user.getId()))
                .thenReturn(Optional.empty());
        Mockito.when(businessUserRepository.save(Mockito.any(BusinessUser.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BusinessUser businessUser = userPromotionService.promoteToBusinessUser(user, agency);

        assertAll(
                () -> assertEquals(user.getUsername(), businessUser.getUsername()),
                () -> assertEquals(user.getCognitoSub(), businessUser.getCognitoSub()),
                () -> assertEquals(agency, businessUser.getAgency())
        );
    }

    @Test
    void promoteToBusinessUser_WhenBusinessUserIsFoundAndAgencyMatches() {
        Mockito.when(businessUserRepository.findById(user.getId()))
                .thenReturn(Optional.of(new BusinessUser(user, agency)));

        BusinessUser businessUser = userPromotionService.promoteToBusinessUser(user, agency);

        assertAll(
                () -> assertEquals(user.getUsername(), businessUser.getUsername()),
                () -> assertEquals(user.getCognitoSub(), businessUser.getCognitoSub()),
                () -> assertEquals(agency, businessUser.getAgency())
        );
    }

    @Test
    void promoteToBusinessUser_WhenBusinessUserIsFoundAndAgencyDoesNotMatch() {
        RealEstateAgency differentAgency = new RealEstateAgency("11111111", "Different Agency");
        Mockito.when(businessUserRepository.findById(user.getId()))
                .thenReturn(Optional.of(new BusinessUser(user, differentAgency)));

        assertThrows(ResponseStatusException.class,
                () -> userPromotionService.promoteToBusinessUser(user, agency)
        );
    }
}