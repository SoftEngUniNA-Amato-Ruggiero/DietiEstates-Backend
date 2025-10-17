package it.softengunina.dietiestatesbackend.interceptors;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ManagerInterceptorTest {
    @Mock
    private TokenService tokenService;
    @Mock
    private RealEstateManagerRepository repository;

    @Mock
    private HttpServletResponse response;
    @Mock
    private Object handler;

    AutoCloseable mocks;
    BaseUser user;
    RealEstateAgency agency;
    RealEstateManager manager;
    ManagerInterceptor interceptor;

    @BeforeEach
    void setUp() {
        mocks = org.mockito.MockitoAnnotations.openMocks(this);
        user = new BaseUser("name", "sub");
        agency = new RealEstateAgency("AgencyName", "1234567890");
        manager = new RealEstateManager(new BusinessUser(user, agency));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void preHandle() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/agents");
        interceptor = new ManagerInterceptor(repository, tokenService);

        Mockito.when(tokenService.getCognitoSub()).thenReturn(user.getCognitoSub());
        Mockito.when(repository.findByBusinessUser_User_CognitoSub(user.getCognitoSub())).thenReturn(Optional.of(manager));

        boolean res = interceptor.preHandle(request, response, handler);

        assertAll(
                () -> assertTrue(res),
                () -> assertEquals(manager, request.getAttribute("manager"))
        );
    }
}