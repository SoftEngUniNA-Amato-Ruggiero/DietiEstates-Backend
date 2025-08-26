package it.softengunina.dietiestatesbackend.model.insertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsertionForSaleTest {
    InsertionForSale insertion;

    @BeforeEach
    void setUp() {
        insertion = new InsertionForSale();
    }

    @Test
    void getRent() {
        assertNull(insertion.getRent());
    }
}