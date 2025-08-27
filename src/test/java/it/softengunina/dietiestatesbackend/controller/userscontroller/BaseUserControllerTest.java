package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = it.softengunina.dietiestatesbackend.filter.JwtRequestFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class BaseUserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserRepository<BaseUser> userRepository;
    @MockitoBean
    TokenService tokenService;

    BaseUser user;
    Long userId = 1L;

    @BeforeEach
    void setUp() {
        user = new BaseUser("testUsername", "testCognitoSub");

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByCognitoSub(user.getCognitoSub())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Mockito.when(userRepository.findByUsername("wrongUsername")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByCognitoSub("wrongCognitoSub")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());
    }

    @Test
    void getUserByUsername() throws Exception {
        mockMvc.perform(get("/users")
        .param("username", user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void getUserById() throws Exception {
        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void getRole() throws Exception {
        Mockito.when(tokenService.getCognitoSub()).thenReturn(user.getCognitoSub());

        mockMvc.perform(get("/users/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value(user.getUsername()))
                .andExpect(jsonPath("$.agency").isEmpty())
                .andExpect(jsonPath("$.role").value(user.getRole()));
    }
}