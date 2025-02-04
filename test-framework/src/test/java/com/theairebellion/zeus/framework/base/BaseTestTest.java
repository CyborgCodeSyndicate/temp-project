package com.theairebellion.zeus.framework.base;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BaseTestTest {

    @Test
    void testStaticInitializerCompletes() {
        // Simply loading the class will run the static initializer.
        // If no exception is thrown, we consider it a pass.
        assertDoesNotThrow(() -> Class.forName("com.theairebellion.zeus.framework.base.BaseTest"));
    }

    @Test
    void testAddSystemPropertiesWhenResourceNotExists() throws Exception {
        // Use reflection to invoke the private static addSystemProperties() method.
        // (If there is no system.properties file in the test classpath, this should do nothing.)
        Method m = BaseTest.class.getDeclaredMethod("addSystemProperties");
        m.setAccessible(true);
        // Before invoking, check that a dummy property is not already set.
        String propName = "dummy.property";
        System.clearProperty(propName);
        // Create a dummy Properties object and simulate that resource does not exist
        // (Since ClassPathResource("system.properties").exists() will be false if file is missing.)
        // So simply invoking should not throw.
        assertDoesNotThrow(() -> m.invoke(null));
        // And system property should remain unset.
        assertNull(System.getProperty(propName));
    }
}