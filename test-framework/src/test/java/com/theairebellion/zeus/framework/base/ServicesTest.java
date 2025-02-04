package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.framework.base.mock.DummyFluentService;
import com.theairebellion.zeus.framework.base.mock.DummyService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicesTest {


    @Test
    void testServiceRetrieval() {
        // Create an instance of DummyFluentService (which implements ClassLevelHook).
        DummyFluentService dummy = new DummyFluentService();
        dummy.dummyService = new DummyService();

        // Build a map of beans using the fully qualified type for ClassLevelHook.
        Map<String, ClassLevelHook> beans =
                Collections.singletonMap("dummy", dummy);

        // Create a mock ApplicationContext.
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBeansOfType(ClassLevelHook.class))
                .thenReturn(beans);

        // Instantiate the Services class using the mocked ApplicationContext.
        Services services = new Services(context);

        // Retrieve the MyService instance via the Services API.
        DummyService service = services.service(DummyFluentService.class, DummyService.class);

        // Verify that the retrieved service is not null and returns the expected value.
        assertNotNull(service, "Service should not be null");
        assertEquals("hello", service.getValue(), "Service should return 'hello'");
    }
}