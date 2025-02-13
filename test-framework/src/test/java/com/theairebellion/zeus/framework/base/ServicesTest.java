package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.framework.base.mock.MockClassLevelHook;
import com.theairebellion.zeus.framework.base.mock.MockService;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicesTest {

    @InjectMocks
    private Services services;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private MockClassLevelHook mockHook;

    @Mock
    private MockService mockService;

    private MockedStatic<ReflectionUtil> reflectionUtilMock;

    @BeforeEach
    void setUp() {
        lenient().when(applicationContext.getBeansOfType(ClassLevelHook.class))
                .thenReturn(Collections.singletonMap("mockHook", mockHook));

        lenient().when(applicationContext.getBean(MockClassLevelHook.class)).thenReturn(mockHook);

        reflectionUtilMock = mockStatic(ReflectionUtil.class);
        reflectionUtilMock.when(() -> ReflectionUtil.getFieldValue(mockHook, MockService.class))
                .thenReturn(mockService);
    }

    @AfterEach
    void tearDown() {
        if (reflectionUtilMock != null) {
            reflectionUtilMock.close();
        }
    }

    @Test
    void testServiceRetrieval() {
        MockService serviceInstance = services.service(MockClassLevelHook.class, MockService.class);
        assertNotNull(serviceInstance);
        assertEquals(mockService, serviceInstance);
    }

    @Test
    void testServiceRetrieval_CachingMechanism() {
        MockService firstInstance = services.service(MockClassLevelHook.class, MockService.class);
        MockService secondInstance = services.service(MockClassLevelHook.class, MockService.class);
        assertSame(firstInstance, secondInstance);
        verify(applicationContext, times(1)).getBeansOfType(ClassLevelHook.class);
    }

    @Test
    void testServiceRetrieval_NoBeanFound() {
        when(applicationContext.getBeansOfType(ClassLevelHook.class)).thenReturn(Collections.emptyMap());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> services.service(MockClassLevelHook.class, MockService.class));

        assertTrue(exception.getMessage().contains("No bean found"));
    }
}