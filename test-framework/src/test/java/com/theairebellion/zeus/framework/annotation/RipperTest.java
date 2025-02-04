package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.RipperTestDummy;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

public class RipperTest {

    @Test
    void testRipperAnnotation() throws Exception {
        Method m = RipperTestDummy.class.getMethod("dummyRipperMethod");
        Ripper ripper = m.getAnnotation(Ripper.class);
        assertNotNull(ripper, "Method should be annotated with @Ripper");
        String[] targets = ripper.targets();
        assertArrayEquals(new String[]{"target1", "target2"}, targets);
    }
}