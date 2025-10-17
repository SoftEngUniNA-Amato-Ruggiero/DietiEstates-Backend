package it.softengunina.dietiestatesbackend.interceptors;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
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

class UserInterceptorTest {
    @Mock
    private TokenService tokenService;
    @Mock
    private BaseUserRepository repository;

    @Mock
    private HttpServletResponse response;
    @Mock
    private Object handler;

    AutoCloseable mocks;
    BaseUser user;
    UserInterceptor interceptor;

    @BeforeEach
    void setUp() {
        mocks = org.mockito.MockitoAnnotations.openMocks(this);
        user = new BaseUser("name", "sub");
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void preHandle() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/me");
        interceptor = new UserInterceptor(tokenService, repository);

        Mockito.when(tokenService.getCognitoSub()).thenReturn(user.getCognitoSub());
        Mockito.when(repository.findByCognitoSub(user.getCognitoSub())).thenReturn(Optional.of(user));

        boolean res = interceptor.preHandle(request, response, handler);

        assertAll(
                () -> assertTrue(res),
                () -> assertEquals(user, request.getAttribute("user"))
        );
    }
}