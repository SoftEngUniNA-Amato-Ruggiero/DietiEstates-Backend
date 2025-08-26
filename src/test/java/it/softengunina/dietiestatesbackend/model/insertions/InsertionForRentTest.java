package it.softengunina.dietiestatesbackend.model.insertions;

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
    void getPrice() {
        assertNull(insertion.getPrice());
    }
}