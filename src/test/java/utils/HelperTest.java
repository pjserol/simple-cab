package utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelperTest {

    @Test
    public void tryParseInt() {
        assertEquals(10, Helper.tryParseInt("10", 20));
        assertEquals(20, Helper.tryParseInt(null, 20));
        assertEquals(20, Helper.tryParseInt("", 20));
    }

    @Test
    public void tryParseString() {
        assertEquals("val", Helper.tryParseString("val", "default"));
        assertEquals("default", Helper.tryParseString(null, "default"));
        assertEquals("default", Helper.tryParseString("", "default"));
    }

    @Test
    public void tryParseBoolean() {
        assertEquals(true, Helper.tryParseBoolean("true", false));
        assertEquals(false, Helper.tryParseBoolean( null, false));
        assertEquals(false, Helper.tryParseBoolean("", false));
    }
}