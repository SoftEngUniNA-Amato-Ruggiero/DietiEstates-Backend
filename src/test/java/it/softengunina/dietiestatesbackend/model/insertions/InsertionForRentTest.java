package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.factory.insertiondtofactory.InsertionWithRentDTOFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsertionForRentTest {
    InsertionForRent insertion;

    @BeforeEach
    void setUp() {
        insertion = new InsertionForRent();
    }

    @Test
    void getDTOFactory() {
        assertEquals(InsertionWithRentDTOFactory.class, insertion.getDTOFactory().getClass());
    }
}