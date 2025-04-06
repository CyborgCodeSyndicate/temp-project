package com.theairebellion.zeus.ui.service;

import com.theairebellion.zeus.ui.annotations.InsertionElement;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.insertion.BaseInsertionService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

/**
 * Reworked test class that avoids mocking the Insertion interface/annotation directly,
 * preventing reflection-based NullPointerExceptions under Maven Surefire.
 */
public class InsertionServiceElementImplTest {

    private InsertionServiceRegistry registry;
    private InsertionServiceElementImpl insertionService;
    private CallCapturingInsertion insertionStub;
    private SmartWebDriver mockWebDriver;

    @BeforeEach
    void setUp() {
        registry = new InsertionServiceRegistry();
        // Our custom "CallCapturingInsertion" avoids Mockito reflection issues
        insertionStub = new CallCapturingInsertion();
        mockWebDriver = Mockito.mock(SmartWebDriver.class);

        // Class under test
        insertionService = new InsertionServiceElementImpl(registry, mockWebDriver);
    }

    // ------------------------------------------------------------------------
    // 1) SAMPLE ENUMS & CLASSES FOR THE TEST
    // ------------------------------------------------------------------------

    public enum MyTestInputType implements ComponentType {
        VALUE1,
        VALUE2;

        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    public enum TestUIElement implements UIElement {
        FIELD_ONE(By.id("fieldOne"), MyTestInputType.VALUE1),
        FIELD_TWO(By.id("fieldTwo"), MyTestInputType.VALUE2),
        FIELD_THREE(By.id("fieldThree"), MyTestInputType.VALUE1);

        private final By locator;
        private final ComponentType componentType;

        TestUIElement(By locator, ComponentType componentType) {
            this.locator = locator;
            this.componentType = componentType;
        }

        @Override
        public By locator() {
            return locator;
        }

        @Override
        public <T extends ComponentType> T componentType() {
            return (T) componentType;
        }

        @Override
        public Enum<?> enumImpl() {
            return this;
        }

        @Override
        public Consumer<SmartWebDriver> before() {
            return w -> { /* no-op for test */ };
        }

        @Override
        public Consumer<SmartWebDriver> after() {
            return w -> { /* no-op for test */ };
        }
    }

    static class OrderDTO {
        @InsertionElement(locatorClass = TestUIElement.class, elementEnum = "FIELD_ONE", order = 1)
        private String fieldOne = "value1";

        @InsertionElement(locatorClass = TestUIElement.class, elementEnum = "FIELD_TWO", order = 2)
        private String fieldTwo = "value2";

        @InsertionElement(locatorClass = TestUIElement.class, elementEnum = "FIELD_THREE", order = 3)
        private String fieldThree = null; // null => skip

        // Not annotated => skip
        private String notAnnotated = "ignored";
    }

    static class UnregisteredDTO {
        @InsertionElement(locatorClass = TestUIElement.class, elementEnum = "FIELD_ONE", order = 1)
        private String fieldOne = "unregistered";
    }

    // ------------------------------------------------------------------------
    // 2) TESTS FOR insertData(Object)
    // ------------------------------------------------------------------------
    @Nested
    @DisplayName("Method: insertData(Object)")
    class InsertDataTests {

        @Test
        @DisplayName("Happy path: multiple fields inserted by ascending order; skip null; call before/after")
        void testInsertDataHappyPath() {
            registry.registerService(MyTestInputType.class, insertionStub);

            OrderDTO dto = new OrderDTO();
            insertionService.insertData(dto);

            // We expect insertion calls for fieldOne & fieldTwo, in order
            List<CallCapturingInsertion.CallRecord> calls = insertionStub.getCalls();
            assertEquals(2, calls.size(), "Expected 2 calls");

            // First call => fieldOne
            assertEquals(MyTestInputType.VALUE1, calls.get(0).componentType);
            assertEquals(By.id("fieldOne"), calls.get(0).locator);
            assertEquals("value1", calls.get(0).values[0]);

            // Second call => fieldTwo
            assertEquals(MyTestInputType.VALUE2, calls.get(1).componentType);
            assertEquals(By.id("fieldTwo"), calls.get(1).locator);
            assertEquals("value2", calls.get(1).values[0]);
        }

        @Test
        @DisplayName("When annotation is missing => skip silently")
        void testInsertDataNoAnnotation() {
            class NoAnnotationDTO {
                private String field = "test";
            }
            NoAnnotationDTO dto = new NoAnnotationDTO();

            // We'll capture calls if any
            CallCapturingInsertion localStub = new CallCapturingInsertion();
            registry.registerService(MyTestInputType.class, localStub);

            insertionService.insertData(dto);
            assertTrue(localStub.getCalls().isEmpty(), "Expected no calls when annotation missing");
        }

        @Test
        @DisplayName("When the service registry has no matching insertion => throws IllegalStateException")
        void testInsertDataNoServiceFound() {
            UnregisteredDTO dto = new UnregisteredDTO();

            IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> insertionService.insertData(dto)
            );
            assertTrue(ex.getMessage().contains("No InsertionService registered for"));
        }
    }

    // ------------------------------------------------------------------------
    // 3) DIRECT TESTS OF OVERRIDDEN METHODS
    // ------------------------------------------------------------------------
    @Nested
    @DisplayName("Overridden Methods Tests")
    class OverriddenMethodsTests {

        @Test
        @DisplayName("getAnnotationClass() returns InsertionElement.class")
        void testGetAnnotationClass() {
            assertEquals(InsertionElement.class, insertionService.getAnnotationClass());
        }

        @Test
        @DisplayName("getOrder() => from real annotation, not a mock")
        void testGetOrder() throws NoSuchFieldException {
            class WithAnnot {
                @InsertionElement(locatorClass = TestUIElement.class, elementEnum = "FIELD_ONE", order = 99)
                private String field;
            }
            Field field = WithAnnot.class.getDeclaredField("field");
            InsertionElement annotation = field.getAnnotation(InsertionElement.class);

            int result = insertionService.getOrder(annotation);
            assertEquals(99, result, "Expected annotation order=99");
        }

        @Test
        @DisplayName("buildLocator() => By from the UIElement enum (locator())")
        void testBuildLocator() throws NoSuchFieldException {
            class Dummy {
                @InsertionElement(locatorClass = TestUIElement.class, elementEnum = "FIELD_ONE", order = 1)
                private String field;
            }
            Field field = Dummy.class.getDeclaredField("field");
            InsertionElement annotation = field.getAnnotation(InsertionElement.class);

            By by = insertionService.buildLocator(annotation);
            assertNotNull(by);
            assertEquals(By.id("fieldOne").toString(), by.toString());
        }

        @Test
        @DisplayName("getType() => uses the UIElement's componentType()")
        void testGetType() throws NoSuchFieldException {
            class Dummy {
                @InsertionElement(locatorClass = TestUIElement.class, elementEnum = "FIELD_TWO", order = 2)
                private String field;
            }
            Field field = Dummy.class.getDeclaredField("field");
            InsertionElement annotation = field.getAnnotation(InsertionElement.class);

            ComponentType result = insertionService.getType(annotation);
            assertEquals(MyTestInputType.VALUE2, result);
        }

        @Test
        @DisplayName("getComponentTypeEnumClass() => calls UIElement.componentType().getClass()")
        void testGetComponentTypeEnumClass() throws NoSuchFieldException {
            class Dummy {
                @InsertionElement(locatorClass = TestUIElement.class, elementEnum = "FIELD_TWO", order = 2)
                private String field;
            }
            Field fld = Dummy.class.getDeclaredField("field");
            InsertionElement annotation = fld.getAnnotation(InsertionElement.class);

            Class<? extends ComponentType> clazz = insertionService.getComponentTypeEnumClass(annotation);
            assertEquals(MyTestInputType.class, clazz);
        }

        @Test
        @DisplayName("beforeInsertion() => calls uiElement.before().accept(webDriver)")
        void testBeforeInsertion() throws NoSuchFieldException {
            class Dummy {
                @InsertionElement(locatorClass = TestUIElement.class, elementEnum = "FIELD_ONE", order = 1)
                private String field;
            }
            Field fld = Dummy.class.getDeclaredField("field");
            InsertionElement annotation = fld.getAnnotation(InsertionElement.class);

            // no exception => success
            insertionService.beforeInsertion(annotation);
        }

        @Test
        @DisplayName("afterInsertion() => calls uiElement.after().accept(webDriver)")
        void testAfterInsertion() throws NoSuchFieldException {
            class Dummy {
                @InsertionElement(locatorClass = TestUIElement.class, elementEnum = "FIELD_TWO", order = 2)
                private String field;
            }
            Field fld = Dummy.class.getDeclaredField("field");
            InsertionElement annotation = fld.getAnnotation(InsertionElement.class);

            // no exception => success
            insertionService.afterInsertion(annotation);
        }
    }

    // ------------------------------------------------------------------------
    // 4) LOGGING TEST
    // ------------------------------------------------------------------------
    @Nested
    @DisplayName("Logging Tests")
    class LoggingTests {

        @Test
        @DisplayName("After successful insertion, LogUI.info(...) is called")
        void testLogIsCalled() {
            // Register MyTestInputType => so insertion won't fail
            registry.registerService(MyTestInputType.class, insertionStub);

            // Create test object
            OrderDTO dto = new OrderDTO();

            // We'll do the insertion inside a mockStatic(LogUI)
            try (var logUIMock = mockStatic(LogUI.class)) {
                insertionService.insertData(dto);

                // The final line in insertData() => LogUI.info("Finished data insertion for ...")
                logUIMock.verify(() ->
                                     LogUI.info(Mockito.contains("Finished data insertion"), any())
                );
            }
        }
    }

    // ------------------------------------------------------------------------
    // 5) HELPER CLASS: Captures calls to insertion(...) in a list
    // ------------------------------------------------------------------------
    static class CallCapturingInsertion implements Insertion {

        private final List<CallRecord> calls = new ArrayList<>();

        @Override
        public void insertion(ComponentType componentType, By locator, Object... values) {
            calls.add(new CallRecord(componentType, locator, values));
        }

        public List<CallRecord> getCalls() {
            return calls;
        }

        static class CallRecord {
            final ComponentType componentType;
            final By locator;
            final Object[] values;

            CallRecord(ComponentType c, By l, Object[] v) {
                this.componentType = c;
                this.locator = l;
                this.values = v;
            }
        }
    }
}
