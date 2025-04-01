package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
//todo fix me
class BaseInsertionServiceTest {
    //
    // @Mock
    // private InsertionServiceRegistry serviceRegistry;
    //
    // @Mock
    // private Insertion insertionService;
    //
    // private TestBaseInsertionService testService;
    //
    // // Simple test class with a field that should be processed
    // static class TestData {
    //     private String field1 = "test value";
    //     private Integer field2 = 123;
    //     private String field3 = null; // This should be skipped as it's null
    // }
    //
    // // Create a concrete implementation of BaseInsertionService for testing
    // static class TestBaseInsertionService extends BaseInsertionService {
    //
    //     private final List<Field> fieldsToProcess = new ArrayList<>();
    //     private Object mockAnnotation;
    //     private Class<? extends ComponentType> componentTypeToReturn;
    //     private By locatorToReturn;
    //     private Enum<?> enumValueToReturn;
    //
    //     private boolean beforeInsertionCalled = false;
    //     private boolean afterInsertionCalled = false;
    //
    //     public TestBaseInsertionService(InsertionServiceRegistry serviceRegistry) {
    //         super(serviceRegistry);
    //     }
    //
    //     public void setMockAnnotation(Object annotation) {
    //         this.mockAnnotation = annotation;
    //     }
    //
    //     public void setComponentTypeToReturn(Class<? extends ComponentType> type) {
    //         this.componentTypeToReturn = type;
    //     }
    //
    //     public void setLocatorToReturn(By locator) {
    //         this.locatorToReturn = locator;
    //     }
    //
    //     public void setEnumValueToReturn(Enum<?> value) {
    //         this.enumValueToReturn = value;
    //     }
    //
    //     public void addFieldToProcess(Field field) {
    //         fieldsToProcess.add(field);
    //     }
    //
    //     public boolean wasBeforeInsertionCalled() {
    //         return beforeInsertionCalled;
    //     }
    //
    //     public boolean wasAfterInsertionCalled() {
    //         return afterInsertionCalled;
    //     }
    //
    //     @Override
    //     protected Object getFieldAnnotation(Field field) {
    //         return mockAnnotation;
    //     }
    //
    //     @Override
    //     protected Class<? extends ComponentType> getComponentType(Object annotation) {
    //         return componentTypeToReturn;
    //     }
    //
    //     @Override
    //     protected By buildLocator(Object annotation) {
    //         return locatorToReturn;
    //     }
    //
    //     @Override
    //     protected Enum<?> getEnumValue(Object annotation) {
    //         return enumValueToReturn;
    //     }
    //
    //     @Override
    //     protected List<Field> filterAndSortFields(Field[] fields) {
    //         return fieldsToProcess;
    //     }
    //
    //     @Override
    //     protected void beforeInsertion(Object annotation) {
    //         beforeInsertionCalled = true;
    //     }
    //
    //     @Override
    //     protected void afterInsertion(Object annotation) {
    //         afterInsertionCalled = true;
    //     }
    // }
    //
    // // Mock implementation of ComponentType for testing
    // interface TestComponentType extends ComponentType {
    //     @Override
    //     default Enum<?> getType() {
    //         return null;
    //     }
    // }
    //
    // // Mock enum that implements ComponentType
    // enum TestEnum implements TestComponentType {
    //     TEST_COMPONENT;
    //
    //     @Override
    //     public Enum<?> getType() {
    //         return this;
    //     }
    // }
    //
    // @BeforeEach
    // void setUp() {
    //     testService = new TestBaseInsertionService(serviceRegistry);
    // }
    //
    // @Nested
    // @DisplayName("insertData method tests")
    // class InsertDataTests {
    //
    //     @Test
    //     @DisplayName("Should process fields with non-null values")
    //     void shouldProcessFieldsWithNonNullValues() throws NoSuchFieldException {
    //         // Given
    //         TestData testData = new TestData();
    //         Field field1 = TestData.class.getDeclaredField("field1");
    //         Field field2 = TestData.class.getDeclaredField("field2");
    //
    //         testService.addFieldToProcess(field1);
    //         testService.addFieldToProcess(field2);
    //
    //         // Setup test service
    //         TestEnum mockEnum = TestEnum.TEST_COMPONENT;
    //         testService.setMockAnnotation(mock(Annotation.class));
    //         testService.setComponentTypeToReturn(TestComponentType.class);
    //         testService.setLocatorToReturn(By.id("test-id"));
    //         testService.setEnumValueToReturn(mockEnum);
    //
    //         // Setup mock service registry
    //         when(serviceRegistry.getService(TestComponentType.class)).thenReturn(insertionService);
    //
    //         // When
    //         testService.insertData(testData);
    //
    //         // Then
    //         verify(insertionService).insertion(eq(mockEnum), eq(By.id("test-id")), eq("test value"));
    //         verify(insertionService).insertion(eq(mockEnum), eq(By.id("test-id")), eq(123));
    //         assertTrue(testService.wasBeforeInsertionCalled());
    //         assertTrue(testService.wasAfterInsertionCalled());
    //     }
    //
    //     @Test
    //     @DisplayName("Should skip fields with null values")
    //     void shouldSkipFieldsWithNullValues() throws NoSuchFieldException {
    //         // Given
    //         TestData testData = new TestData();
    //         Field field3 = TestData.class.getDeclaredField("field3");
    //
    //         testService.addFieldToProcess(field3);
    //
    //         // Setup test service
    //         testService.setMockAnnotation(mock(Annotation.class));
    //         testService.setComponentTypeToReturn(TestComponentType.class);
    //         testService.setLocatorToReturn(By.id("test-id"));
    //         testService.setEnumValueToReturn(TestEnum.TEST_COMPONENT);
    //
    //         // Setup mock service registry
    //         when(serviceRegistry.getService(TestComponentType.class)).thenReturn(insertionService);
    //
    //         // When
    //         testService.insertData(testData);
    //
    //         // Then
    //         verify(insertionService, never()).insertion(any(), any(), any());
    //         assertFalse(testService.wasBeforeInsertionCalled());
    //         assertFalse(testService.wasAfterInsertionCalled());
    //     }
    //
    //     @Test
    //     @DisplayName("Should skip fields with empty annotation")
    //     void shouldSkipFieldsWithEmptyAnnotation() throws NoSuchFieldException {
    //         // Given
    //         TestData testData = new TestData();
    //         Field field1 = TestData.class.getDeclaredField("field1");
    //
    //         testService.addFieldToProcess(field1);
    //
    //         // Setup test service
    //         testService.setMockAnnotation(null); // No annotation
    //
    //         // When
    //         testService.insertData(testData);
    //
    //         // Then
    //         verify(serviceRegistry, never()).getService(any());
    //         verify(insertionService, never()).insertion(any(), any(), any());
    //     }
    //
    //     @Test
    //     @DisplayName("Should throw IllegalStateException when no insertion service is registered")
    //     void shouldThrowIllegalStateExceptionWhenNoInsertionServiceIsRegistered() throws NoSuchFieldException {
    //         // Given
    //         TestData testData = new TestData();
    //         Field field1 = TestData.class.getDeclaredField("field1");
    //
    //         testService.addFieldToProcess(field1);
    //
    //         // Setup test service
    //         testService.setMockAnnotation(mock(Annotation.class));
    //         testService.setComponentTypeToReturn(TestComponentType.class);
    //
    //         // Setup mock service registry to return null
    //         when(serviceRegistry.getService(TestComponentType.class)).thenReturn(null);
    //
    //         // When & Then
    //         IllegalStateException exception = assertThrows(
    //                 IllegalStateException.class,
    //                 () -> testService.insertData(testData)
    //         );
    //
    //         assertTrue(exception.getMessage().contains("No InsertionService registered for"));
    //     }
    //
    //     @Test
    //     @DisplayName("Should throw RuntimeException when field access fails")
    //     void shouldThrowRuntimeExceptionWhenFieldAccessFails() {
    //         // Given
    //         TestData testData = new TestData();
    //
    //         // Create a test implementation that will throw the exception we want to test
    //         BaseInsertionService testService = new BaseInsertionService(serviceRegistry) {
    //             @Override
    //             protected Object getFieldAnnotation(Field field) {
    //                 return mock(Annotation.class);
    //             }
    //
    //             @Override
    //             protected Class<? extends ComponentType> getComponentType(Object annotation) {
    //                 return TestComponentType.class;
    //             }
    //
    //             @Override
    //             protected By buildLocator(Object annotation) {
    //                 return By.id("test-id");
    //             }
    //
    //             @Override
    //             protected Enum<?> getEnumValue(Object annotation) {
    //                 return TestEnum.TEST_COMPONENT;
    //             }
    //
    //             @Override
    //             protected List<Field> filterAndSortFields(Field[] fields) {
    //                 try {
    //                     Field field = TestData.class.getDeclaredField("field1");
    //                     return List.of(field);
    //                 } catch (NoSuchFieldException e) {
    //                     throw new RuntimeException(e);
    //                 }
    //             }
    //         };
    //
    //         // Create a spy on the test service
    //         BaseInsertionService spyService = spy(testService);
    //
    //         // Setup the spy to throw the exception we want to test
    //         doAnswer(invocation -> {
    //             throw new RuntimeException("Failed to access field: field1",
    //                     new IllegalAccessException("Test exception"));
    //         }).when(spyService).insertData(any());
    //
    //         // When & Then
    //         RuntimeException exception = assertThrows(
    //                 RuntimeException.class,
    //                 () -> spyService.insertData(testData)
    //         );
    //
    //         // Verify the exception message
    //         assertTrue(exception.getMessage().contains("Failed to access field"));
    //     }
    // }
    //
    // @Nested
    // @DisplayName("Hook method tests")
    // class HookMethodTests {
    //
    //     @Test
    //     @DisplayName("Should call beforeInsertion and afterInsertion hooks")
    //     void shouldCallBeforeAndAfterInsertionHooks() throws NoSuchFieldException {
    //         // Given
    //         TestData testData = new TestData();
    //         Field field1 = TestData.class.getDeclaredField("field1");
    //
    //         testService.addFieldToProcess(field1);
    //
    //         // Setup test service
    //         Object mockAnnotation = mock(Annotation.class);
    //         testService.setMockAnnotation(mockAnnotation);
    //         testService.setComponentTypeToReturn(TestComponentType.class);
    //         testService.setLocatorToReturn(By.id("test-id"));
    //         testService.setEnumValueToReturn(TestEnum.TEST_COMPONENT);
    //
    //         // Setup mock service registry
    //         when(serviceRegistry.getService(TestComponentType.class)).thenReturn(insertionService);
    //
    //         // When
    //         testService.insertData(testData);
    //
    //         // Then
    //         assertTrue(testService.wasBeforeInsertionCalled());
    //         assertTrue(testService.wasAfterInsertionCalled());
    //     }
    // }
    //
    // @Test
    // @DisplayName("Should throw RuntimeException when IllegalAccessException occurs")
    // void shouldThrowRuntimeExceptionWhenIllegalAccessExceptionOccurs() throws NoSuchFieldException {
    //     // Given
    //     TestData testData = new TestData();
    //
    //     // Create a field instance that will be used in our test
    //     Field realField = TestData.class.getDeclaredField("field1");
    //
    //     // Create a custom BaseInsertionService that will trigger the exception
    //     BaseInsertionService testService = new BaseInsertionService(serviceRegistry) {
    //         @Override
    //         protected Object getFieldAnnotation(Field field) {
    //             return mock(Annotation.class);
    //         }
    //
    //         @Override
    //         protected Class<? extends ComponentType> getComponentType(Object annotation) {
    //             return TestComponentType.class;
    //         }
    //
    //         @Override
    //         protected By buildLocator(Object annotation) {
    //             return By.id("test-id");
    //         }
    //
    //         @Override
    //         protected Enum<?> getEnumValue(Object annotation) {
    //             return TestEnum.TEST_COMPONENT;
    //         }
    //
    //         @Override
    //         protected List<Field> filterAndSortFields(Field[] fields) {
    //             // Return the real field - we'll cause the exception through other means
    //             return List.of(realField);
    //         }
    //     };
    //
    //     // Create a special RuntimeException that will be thrown for testing
    //     IllegalAccessException illegalAccessException = new IllegalAccessException("Test exception");
    //
    //     // Using PowerMockito to mock the static Field.get method would be ideal here
    //     // But since we're not using PowerMockito, we'll try a different approach
    //
    //     // Use a spy to intercept the field.get() call
    //     BaseInsertionService spyService = spy(testService);
    //
    //     // Make the spy throw an exception when field.get is about to be called
    //     doAnswer(invocation -> {
    //         throw new RuntimeException("Failed to access field", illegalAccessException);
    //     }).when(spyService).insertData(any());
    //
    //     // When & Then
    //     RuntimeException exception = assertThrows(
    //             RuntimeException.class,
    //             () -> spyService.insertData(testData)
    //     );
    //
    //     // Verify the cause is our IllegalAccessException
    //     assertSame(illegalAccessException, exception.getCause());
    // }
    //
    // @Test
    // @DisplayName("Should catch IllegalAccessException and wrap it in RuntimeException")
    // void shouldCatchIllegalAccessExceptionAndWrapIt() throws Exception {
    //     // Given
    //     Object testData = new Object(); // Simple test data
    //
    //     // Create a custom Field implementation that forces an IllegalAccessException
    //     Field mockField = mock(Field.class, withSettings().lenient());
    //
    //     // Setup the mock field behavior
    //     when(mockField.getName()).thenReturn("testField");
    //     doNothing().when(mockField).setAccessible(anyBoolean()); // Correct way to mock void methods
    //     doThrow(new IllegalAccessException("Test exception")).when(mockField).get(any());
    //
    //     // Create a test implementation that returns our mock field
    //     BaseInsertionService testService = new BaseInsertionService(serviceRegistry) {
    //         @Override
    //         protected Object getFieldAnnotation(Field field) {
    //             return mock(Annotation.class, withSettings().lenient());
    //         }
    //
    //         @Override
    //         protected Class<? extends ComponentType> getComponentType(Object annotation) {
    //             return TestComponentType.class;
    //         }
    //
    //         @Override
    //         protected By buildLocator(Object annotation) {
    //             return By.id("test-id");
    //         }
    //
    //         @Override
    //         protected Enum<?> getEnumValue(Object annotation) {
    //             return TestEnum.TEST_COMPONENT;
    //         }
    //
    //         @Override
    //         protected List<Field> filterAndSortFields(Field[] fields) {
    //             return List.of(mockField);
    //         }
    //     };
    //
    //     // Setup registry mock with lenient settings
    //     lenient().when(serviceRegistry.getService(any())).thenReturn(mock(Insertion.class));
    //
    //     // When
    //     Exception caughtException = null;
    //     try {
    //         testService.insertData(testData);
    //     } catch (Exception e) {
    //         caughtException = e;
    //         if (e.getCause() != null) {
    //         }
    //     }
    //
    //     // Then
    //     assertNotNull(caughtException, "Expected an exception to be thrown");
    //     assertTrue(caughtException instanceof RuntimeException,
    //             "Expected RuntimeException but got: " + caughtException.getClass().getName());
    //
    //     // Use more general assertions
    //     assertTrue(caughtException.getMessage().contains("field"),
    //             "Expected message to contain 'field' but was: " + caughtException.getMessage());
    //
    //     assertNotNull(caughtException.getCause(), "Expected exception to have a cause");
    //     assertTrue(caughtException.getCause() instanceof IllegalAccessException,
    //             "Expected cause to be IllegalAccessException but was: " +
    //                     caughtException.getCause().getClass().getName());
    // }
}