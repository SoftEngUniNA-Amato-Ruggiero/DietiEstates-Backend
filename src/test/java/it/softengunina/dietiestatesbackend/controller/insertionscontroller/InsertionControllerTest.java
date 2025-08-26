package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionRepository;
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
        controllers = InsertionController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = it.softengunina.dietiestatesbackend.filter.JwtRequestFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class InsertionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    InsertionRepository<Insertion> insertionRepository;

    Long insertionId = 1L;
    Insertion insertion;

    Long uploaderId = 2L;
    RealEstateAgent uploader;

    @BeforeEach
    void setUp() {
        insertion = Mockito.mock(InsertionForSale.class);
        uploader = Mockito.mock(RealEstateAgent.class);

        Mockito.when(insertion.getId()).thenReturn(insertionId);
        Mockito.when(insertion.getPrice()).thenReturn(100000.0);
        Mockito.when(insertion.getUploader()).thenReturn(uploader);
        Mockito.when(uploader.getId()).thenReturn(uploaderId);

        Mockito.when(insertionRepository.findById(insertionId)).thenReturn(Optional.of(insertion));
        Mockito.when(insertionRepository.findById(2L)).thenReturn(Optional.empty());

        Mockito.when(insertionRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(insertion)));
    }

    @Test
    void getInsertions() throws Exception {
        mockMvc.perform(get("/insertions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0]").exists());
    }

    @Test
    void getInsertionById() throws Exception {
        mockMvc.perform(get("/insertions/" + insertionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(insertion.getId()));
    }
}