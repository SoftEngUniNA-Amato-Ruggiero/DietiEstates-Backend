package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionSearchResultDTO;
import it.softengunina.dietiestatesbackend.dto.searchdto.SearchRequestDTO;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InsertionControllerIntegrationTest {
    @Autowired
    InsertionController insertionController;

    @MockitoBean
    TokenService tokenService;

    SearchRequestDTO queryParams;
    String userSub;

    @BeforeEach
    void setUp() {
        userSub = "baseUserSub";
        Mockito.when(tokenService.getCognitoSub()).thenReturn(userSub);
    }

    @Test
    void searchInsertionsForSale_WithTags() {
        queryParams = SearchRequestDTO.builder()
                .lat(40.0)
                .lng(55.0)
                .distance(50.0)
                .tags("Garage")
                .minSize(50.0)
                .minNumberOfRooms(2)
                .build();

        Page<InsertionSearchResultDTO> page = insertionController.searchInsertions(queryParams, null);

        assertAll(
                () -> assertNotNull(page),
                () -> assertFalse(page.isEmpty())
        );
    }

    @Test
    void searchInsertionsForSale_WithoutTags() {
        queryParams = SearchRequestDTO.builder()
                .lat(40.0)
                .lng(55.0)
                .distance(50.0)
                .minSize(50.0)
                .minNumberOfRooms(2)
                .build();

        Page<InsertionSearchResultDTO> page = insertionController.searchInsertions(queryParams, null);

        assertAll(
                () -> assertNotNull(page),
                () -> assertFalse(page.isEmpty())
        );
    }
}