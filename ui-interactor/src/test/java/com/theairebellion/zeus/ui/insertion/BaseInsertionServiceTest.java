package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.log.LogUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BaseInsertionServiceTest {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DummyAnnotation {
        int order();
        String locator(); // For testing, we don't use this value.
        Class<? extends ComponentType> type();
        String componentType();
    }

    // --- Dummy Component Type interface and enum ---
    public interface DummyComponentType extends ComponentType {
    }

    public enum DummyEnum implements DummyComponentType {
        VALUE1, VALUE2;

        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    // --- Dummy DTO with annotated fields ---
    public static class DummyDTO {
        // Field with order 2 and a non-null value
        @DummyAnnotation(order = 2, locator = "dummy", type = DummyEnum.class, componentType = "VALUE1")
        private String field2 = "value2";

        // Field with order 1 and a non-null value
        @DummyAnnotation(order = 1, locator = "dummy", type = DummyEnum.class, componentType = "VALUE1")
        private String field1 = "value1";

        // Field without the annotation (should be ignored)
        private String nonAnnotated = "non";

        // Field with order 3 but null value (should be skipped for insertion)
        @DummyAnnotation(order = 3, locator = "dummy", type = DummyEnum.class, componentType = "VALUE1")
        private String fieldNull = null;
    }

    // --- Concrete subclass of BaseInsertionService for testing ---
    public static class TestInsertionService extends BaseInsertionService<DummyAnnotation> {

        public boolean beforeCalled = false;
        public boolean afterCalled = false;

        public TestInsertionService(InsertionServiceRegistry registry) {
            super(registry);
        }

        @Override
        protected Class<DummyAnnotation> getAnnotationClass() {
            return DummyAnnotation.class;
        }

        @Override
        protected int getOrder(DummyAnnotation annotation) {
            return annotation.order();
        }

        @Override
        protected Class<? extends ComponentType> getComponentTypeEnumClass(DummyAnnotation annotation) {
            return annotation.type();
        }

        @Override
        protected By buildLocator(DummyAnnotation annotation) {
            // For testing, simply return a dummy locator.
            return By.id("dummy");
        }

        @Override
        protected ComponentType getType(DummyAnnotation annotation) {
            // For testing, if the annotation's componentType equals "VALUE1", return DummyEnum.VALUE1.
            if ("VALUE1".equals(annotation.componentType())) {
                return DummyEnum.VALUE1;
            } else {
                throw new IllegalArgumentException("Unknown component type");
            }
        }

        @Override
        protected void beforeInsertion(DummyAnnotation annotation) {
            beforeCalled = true;
        }

        // Although not defined in the abstract class, we add afterInsertion in our test subclass.
        protected void afterInsertion(DummyAnnotation annotation) {
            afterCalled = true;
        }
    }

    // --- Tests for the insertData method ---
    @Nested
    @DisplayName("Method: insertData(Object)")
    class InsertDataTests {
        private InsertionServiceRegistry registry;
        private TestInsertionService testService;
        private Insertion mockInsertion;

        @BeforeEach
        void setUp() {
            registry = new InsertionServiceRegistry();
            testService = new TestInsertionService(registry);
            mockInsertion = mock(Insertion.class);
            // For DummyEnum, extractComponentTypeClass(DummyEnum.class) will resolve to DummyComponentType.
            // We register the mockInsertion for DummyComponentType.
            registry.registerService(DummyComponentType.class, mockInsertion);
        }

        @Test
        @DisplayName("When field value is non-null, insertion is executed and hooks are called in order")
        void testInsertDataWithNonNullFieldValue() {
            DummyDTO dto = new DummyDTO();
            // Reset hook flags.
            testService.beforeCalled = false;
            testService.afterCalled = false;

            testService.insertData(dto);

            // The insertion should be executed for field1 and field2 (fields with non-null values).
            // Field order should be: field1 (order 1) then field2 (order 2).
            verify(mockInsertion, times(2))
                .insertion(eq(DummyEnum.VALUE1), any(By.class), any());
            InOrder inOrder = inOrder(mockInsertion);
            inOrder.verify(mockInsertion)
                .insertion(eq(DummyEnum.VALUE1), any(By.class), eq("value1"));
            inOrder.verify(mockInsertion)
                .insertion(eq(DummyEnum.VALUE1), any(By.class), eq("value2"));

            // Verify that the beforeInsertion and afterInsertion hooks were called.
            assertTrue(testService.beforeCalled, "Expected beforeInsertion to be called");
            assertTrue(testService.afterCalled, "Expected afterInsertion to be called");
        }

        @Test
        @DisplayName("When field value is null, insertion is not executed")
        void testInsertDataWithNullFieldValue() {
            DummyDTO dto = new DummyDTO();
            // Set non-null annotated fields to null.
            try {
                Field field1 = DummyDTO.class.getDeclaredField("field1");
                Field field2 = DummyDTO.class.getDeclaredField("field2");
                field1.setAccessible(true);
                field2.setAccessible(true);
                field1.set(dto, null);
                field2.set(dto, null);
            } catch (Exception e) {
                fail("Failed to set field to null: " + e.getMessage());
            }
            testService.beforeCalled = false;
            testService.afterCalled = false;

            testService.insertData(dto);

            // With no non-null values, the insertion service should not be called.
            verifyNoInteractions(mockInsertion);
            assertFalse(testService.beforeCalled, "Expected beforeInsertion not to be called");
            assertFalse(testService.afterCalled, "Expected afterInsertion not to be called");
        }

        @Test
        @DisplayName("When no Insertion service is registered, then IllegalStateException is thrown")
        void testInsertDataWithNoServiceRegistered() {
            DummyDTO dto = new DummyDTO();
            // Create a fresh registry with no services registered.
            registry = new InsertionServiceRegistry();
            testService = new TestInsertionService(registry);
            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> testService.insertData(dto));
            assertTrue(ex.getMessage().contains("No InsertionService registered for"),
                "Expected exception message to mention missing insertion service");
        }

        @Test
        @DisplayName("When a field has no annotation, it is skipped without error")
        void testInsertDataSkipsNonAnnotatedFields() {
            class MixedDTO {
                private String nonAnnotatedField = "nope";
            }

            MixedDTO dto = new MixedDTO();

            TestInsertionService service = new TestInsertionService(new InsertionServiceRegistry());
            service.insertData(dto);

            // We expect no exception, and nothing else needs to be verified
            // This test exists to cover the 'continue' line when annotation is null
        }
    }

    // --- Tests for the extractComponentTypeClass helper method ---
    @Nested
    @DisplayName("Method: extractComponentTypeClass")
    class ExtractComponentTypeClassTests {

        @Test
        @DisplayName("When given a valid enum class, extractComponentTypeClass returns the proper interface")
        void testExtractComponentTypeClassSuccess() {
            // For DummyEnum, extractComponentTypeClass should resolve to DummyComponentType.
            Class<? extends ComponentType> result = TestInsertionService.extractComponentTypeClass(DummyEnum.class);
            assertEquals(DummyComponentType.class, result,
                "Expected extractComponentTypeClass to return DummyComponentType interface");
        }

        @Test
        @DisplayName("When calling with correct component type class extractComponentTypeClass should return it\"")
        void testExtractComponentTypeClassWithInterfaceArgument() {

            Class<? extends ComponentType> result = TestInsertionService.extractComponentTypeClass(
                InputComponentType.class);
            assertEquals(InputComponentType.class, result, "Same component type class should be returned.");
        }

        @Test
        @DisplayName("When given an enum class without a valid interface, then IllegalStateException is thrown")
        void testExtractComponentTypeClassFailure() {

            interface FaultyInterface extends InputComponentType {

            }

            enum FaultyEnum implements FaultyInterface {
                VALUE;


                @Override
                public Enum<?> getType() {
                    return this;
                }
            }
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> TestInsertionService.extractComponentTypeClass(FaultyEnum.class));
            assertTrue(ex.getMessage().contains("No interface extending ComponentType found"),
                "Expected exception message to mention missing interface");
        }
    }

    @Nested
    @DisplayName("Methods: beforeInsertion and afterInsertion")
    class BeforeAndAfterInsertionClassTests {

        @Test
        @DisplayName("Default beforeInsertion and afterInsertion do nothing")
        void testDefaultHooksAreNoOp() {
            class NoOpService extends BaseInsertionService<DummyAnnotation> {

                public NoOpService(InsertionServiceRegistry registry) {
                    super(registry);
                }


                @Override
                protected Class<DummyAnnotation> getAnnotationClass() {
                    return DummyAnnotation.class;
                }


                @Override
                protected int getOrder(DummyAnnotation annotation) {
                    return annotation.order();
                }


                @Override
                protected Class<? extends ComponentType> getComponentTypeEnumClass(DummyAnnotation annotation) {
                    return annotation.type();
                }


                @Override
                protected By buildLocator(DummyAnnotation annotation) {
                    return By.id("dummy");
                }


                @Override
                protected ComponentType getType(DummyAnnotation annotation) {
                    return DummyEnum.VALUE1;
                }

            }

            DummyDTO dto = new DummyDTO();
            Insertion mockInsertion = mock(Insertion.class);
            InsertionServiceRegistry registry = new InsertionServiceRegistry();
            registry.registerService(DummyComponentType.class, mockInsertion);

            NoOpService service = new NoOpService(registry);
            service.insertData(dto);

            verify(mockInsertion, atLeastOnce())
                .insertion(any(), any(), any());
            // This test is to cover the default no-op hooks
        }

    }

    // --- Tests for the filterAndSortFields helper method ---
    @Nested
    @DisplayName("Method: filterAndSortFields")
    class FilterAndSortFieldsTests {

        @Test
        @DisplayName("When multiple fields are annotated, filterAndSortFields returns them in sorted order")
        void testFilterAndSortFieldsSorting() {
            DummyDTO dto = new DummyDTO();
            Field[] fields = DummyDTO.class.getDeclaredFields();
            TestInsertionService service = new TestInsertionService(new InsertionServiceRegistry());
            List<Field> sortedFields = service.filterAndSortFields(fields);
            // Expect only the annotated fields (field1, field2, fieldNull) in order of their 'order' values.
            assertEquals(3, sortedFields.size(), "Expected 3 annotated fields");
            assertEquals("field1", sortedFields.get(0).getName());
            assertEquals("field2", sortedFields.get(1).getName());
            assertEquals("fieldNull", sortedFields.get(2).getName());
        }
    }

    // --- Test to verify that LogUI.info is invoked after insertion ---
    @Nested
    @DisplayName("Logging Tests")
    class LoggingTests {

        @Test
        @DisplayName("After insertion, LogUI.info is called")
        void testLogUIInfoCalled() {
            DummyDTO dto = new DummyDTO();
            InsertionServiceRegistry registry = new InsertionServiceRegistry();
            TestInsertionService service = new TestInsertionService(registry);
            Insertion mockInsertion = mock(Insertion.class);
            registry.registerService(DummyComponentType.class, mockInsertion);

            // Use Mockito to mock static methods of LogUI.
            try (var mockedStatic = mockStatic(LogUI.class)) {
                service.insertData(dto);
                // Verify that LogUI.info was called at least once.
                mockedStatic.verify(() -> LogUI.info(anyString(), any()));
            }
        }
    }
}