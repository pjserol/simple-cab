package utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidationTest {

    @Test
    public void verifyInputDate() {
        assertEquals(true, Validation.verifyInputDate("2013-12-01"));
        assertEquals(true, Validation.verifyInputDate("2013-2-1"));
        assertEquals(false, Validation.verifyInputDate("2013-12"));
        assertEquals(false, Validation.verifyInputDate("13-12"));
        assertEquals(false, Validation.verifyInputDate(""));
    }
}