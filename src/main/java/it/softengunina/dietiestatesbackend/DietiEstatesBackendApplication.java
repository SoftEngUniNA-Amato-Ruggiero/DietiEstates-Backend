package it.softengunina.dietiestatesbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;


/**
 * The main class for the Dieti Estates Backend application.
 */
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class DietiEstatesBackendApplication {

    /**
     * Starting point of the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DietiEstatesBackendApplication.class, args);
    }

}
