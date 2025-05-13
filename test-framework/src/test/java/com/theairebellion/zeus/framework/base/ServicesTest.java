package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.framework.base.mock.MockClassLevelHook;
import com.theairebellion.zeus.framework.base.mock.MockService;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Services Tests")
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
      // Setup for regular tests
      lenient().when(applicationContext.getBeansOfType(ClassLevelHook.class))
            .thenReturn(Collections.singletonMap("mockHook", mockHook));

      reflectionUtilMock = mockStatic(ReflectionUtil.class);
      reflectionUtilMock.when(() -> ReflectionUtil.getFieldValues(mockHook, MockService.class))
            .thenReturn(List.of(mockService));
   }

   @AfterEach
   void tearDown() {
      if (reflectionUtilMock != null) {
         reflectionUtilMock.close();
      }
   }

   @Test
   @DisplayName("Should retrieve service using fluent service class")
   void testServiceRetrieval() {
      // When
      MockService serviceInstance = services.service(MockClassLevelHook.class, MockService.class);

      // Then
      assertNotNull(serviceInstance);
      assertEquals(mockService, serviceInstance);
      verify(applicationContext).getBeansOfType(ClassLevelHook.class);
      reflectionUtilMock.verify(() -> ReflectionUtil.getFieldValues(mockHook, MockService.class));
   }

   @Test
   @DisplayName("Should use cached service instance on subsequent calls")
   void testServiceRetrieval_CachingMechanism() {
      // When
      MockService firstInstance = services.service(MockClassLevelHook.class, MockService.class);
      MockService secondInstance = services.service(MockClassLevelHook.class, MockService.class);

      // Then
      assertSame(firstInstance, secondInstance);
      verify(applicationContext, times(1)).getBeansOfType(ClassLevelHook.class);
      reflectionUtilMock.verify(() -> ReflectionUtil.getFieldValues(mockHook, MockService.class), times(1));
   }

   @Test
   @DisplayName("Should throw exception when no matching bean is found")
   void testServiceRetrieval_NoBeanFound() {
      // Given
      when(applicationContext.getBeansOfType(ClassLevelHook.class)).thenReturn(Collections.emptyMap());

      // When/Then
      IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> services.service(MockClassLevelHook.class, MockService.class));

      assertTrue(exception.getMessage().contains("No bean found"));
      assertTrue(exception.getMessage().contains(MockClassLevelHook.class.getName()));
   }

   @Test
   @DisplayName("Should filter beans by exact class match")
   void testServiceRetrieval_FiltersByExactClass() {
      // Create real instances instead of mocks to test the class filtering
      MockClassLevelHook realMockHook = new MockClassLevelHook();
      ClassLevelHook otherHook = new ClassLevelHook() {
      };  // Anonymous implementation

      Map<String, ClassLevelHook> beans = new HashMap<>();
      beans.put("otherHook", otherHook);
      beans.put("mockHook", realMockHook);

      when(applicationContext.getBeansOfType(ClassLevelHook.class)).thenReturn(beans);

      // Create a new Services instance with real hook instances
      Services testServices = new Services(applicationContext);

      // Setup the reflectionUtilMock to return a mock service for our real hook
      reflectionUtilMock.when(() -> ReflectionUtil.getFieldValues(realMockHook, MockService.class))
            .thenReturn(List.of(mockService));

      // When
      MockService serviceInstance = testServices.service(MockClassLevelHook.class, MockService.class);

      // Then
      assertNotNull(serviceInstance);
      assertEquals(mockService, serviceInstance);
      reflectionUtilMock.verify(() -> ReflectionUtil.getFieldValues(realMockHook, MockService.class));
   }

   @Test
   @DisplayName("Should have @Component and @Lazy annotations")
   void testComponentAnnotations() {
      // When
      Component component = Services.class.getAnnotation(Component.class);
      Lazy lazy = Services.class.getAnnotation(Lazy.class);

      // Then
      assertNotNull(component, "Should have @Component annotation");
      assertNotNull(lazy, "Should have @Lazy annotation");
   }

   @Test
   @DisplayName("Constructor should have @Autowired annotation")
   void testConstructorAnnotation() throws NoSuchMethodException {
      // When
      Autowired autowired = Services.class.getConstructor(ApplicationContext.class)
            .getAnnotation(Autowired.class);

      // Then
      assertNotNull(autowired, "Constructor should have @Autowired annotation");
   }
}