package com.theairebellion.zeus.framework.parameters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LateTest {

    public static final String TEST_STRING = "testString";

    @Test
    void testJoinWithString() {
        Late<String> late = () -> TEST_STRING;
        String result = late.join();
        assertEquals(TEST_STRING, result);
    }

    @Test
    void testJoinWithInteger() {
        Late<Integer> late = () -> 100;
        Integer result = late.join();
        assertEquals(100, result);
    }
}