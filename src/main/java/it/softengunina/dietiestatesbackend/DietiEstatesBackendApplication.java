package it.softengunina.dietiestatesbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;


/**
 * Dieti Estates Backend application provides REST APIs for browsing and uploading insertions of Real Estates properties.
 */
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class DietiEstatesBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DietiEstatesBackendApplication.class, args);
    }

}
