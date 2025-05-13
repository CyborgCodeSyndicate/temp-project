package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.annotations.InsertionField;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import com.theairebellion.zeus.util.reflections.exceptions.ReflectionException;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mockStatic;

public class InsertionServiceFieldImplTest {

   private InsertionServiceRegistry registry;
   private InsertionServiceFieldImpl insertionService;
   private Insertion mockInsertion;

   @BeforeEach
   void setUp() {
      registry = new InsertionServiceRegistry();
      insertionService = new InsertionServiceFieldImpl(registry);
      mockInsertion = Mockito.mock(Insertion.class);

      // For the test, we register an Insertion service for the InputComponentType interface:
      registry.registerService(InputComponentType.class, mockInsertion);
   }

   // -----------------------------------------------------------
   // 1) DATA CLASSES & USAGE EXAMPLES
   // -----------------------------------------------------------

   // We'll reuse the annotation from your code:
   // @InsertionField(locator = @FindBy(css = "locator"), type = InputComponentType.class,
   //                 componentType = "MD_INPUT", order = 1)

   // A simple enum that implements InputComponentType
   public enum TestInputFields implements InputComponentType {
      MD_INPUT, OTHER_INPUT;

      @Override
      public Enum<?> getType() {
         return this;
      }
   }

   // A DTO with multiple annotated fields:
   static class ExampleDTOUI {

      // Annotated field #1 (lowest order => inserted first)
      @InsertionField(locator = @FindBy(css = "field1-css"),
            type = InputComponentType.class,
            componentType = "MD_INPUT",
            order = 1)
      private String fieldOne = "value1";

      // Annotated field #2 (higher order => inserted second)
      @InsertionField(locator = @FindBy(css = "field2-css"),
            type = InputComponentType.class,
            componentType = "MD_INPUT",
            order = 2)
      private String fieldTwo = "value2";

      // Annotated field #3 but null => should be skipped
      @InsertionField(locator = @FindBy(css = "field3-css"),
            type = InputComponentType.class,
            componentType = "MD_INPUT",
            order = 3)
      private String fieldThree = null;

      // Non-annotated => should be ignored
      private String notAnnotated = "ignored";
   }

   // A DTO with an annotation that references a bogus componentType => Reflection fails
   static class BadReflectionDTO {

      @InsertionField(locator = @FindBy(css = "bad-css"),
            type = InputComponentType.class,
            componentType = "DOES_NOT_EXIST",
            order = 1)
      private String badField = "test";
   }

   // A DTO with an annotation but the registry has no Insertion for the type => fails
   static class UnregisteredTypeDTO {

      @InsertionField(locator = @FindBy(css = "other-css"),
            type = AnotherComponentType.class,
            componentType = "ANOTHER",
            order = 1)
      private String unregisteredField = "test2";
   }

   public enum AnotherComponentType implements ComponentType {
      ;


      @Override
      public Enum<?> getType() {
         return null;
      }
   }

   // -----------------------------------------------------------
   // 2) TESTS FOR insertData SCENARIOS
   // -----------------------------------------------------------
   @Nested
   @DisplayName("Method: insertData(Object)")
   class InsertDataTests {

      @Test
      @DisplayName("Happy Path: multiple annotated fields => calls insertion in ascending order; skips null fields")
      void testInsertDataHappyPath() {
         // We'll mock ReflectionUtil so that it returns the matching enum for "MD_INPUT".
         try (var reflectionMock = mockStatic(ReflectionUtil.class)) {
            // If the componentType is "MD_INPUT", return TestInputFields.MD_INPUT
            reflectionMock.when(() ->
                        ReflectionUtil.findEnumImplementationsOfInterface(eq(InputComponentType.class),
                              eq("MD_INPUT"),
                              nullable(String.class)))
                  .thenReturn(TestInputFields.MD_INPUT);

            // Create a test DTO
            ExampleDTOUI dto = new ExampleDTOUI();

            // Now call insertData
            insertionService.insertData(dto);

            // We expect insertions for fieldOne and fieldTwo only (fieldThree is null => skip)
            InOrder inOrder = Mockito.inOrder(mockInsertion);
            inOrder.verify(mockInsertion).insertion(
                  eq(TestInputFields.MD_INPUT),
                  argThat(locator -> locator instanceof By /* e.g. By.cssSelector("field1-css") */),
                  eq("value1")
            );
            inOrder.verify(mockInsertion).insertion(
                  eq(TestInputFields.MD_INPUT),
                  argThat(locator -> locator instanceof By /* e.g. By.cssSelector("field2-css") */),
                  eq("value2")
            );
            inOrder.verifyNoMoreInteractions();

            // Confirm ReflectionUtil was called exactly twice for those two fields
            reflectionMock.verify(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                  eq(InputComponentType.class), eq("MD_INPUT"), nullable(String.class)), Mockito.times(2));
         }
      }

      @Test
      @DisplayName("When reflection fails to find a matching enum, it throws ReflectionException => insertion aborted")
      void testInsertDataReflectionFailure() {
         try (var reflectionMock = mockStatic(ReflectionUtil.class)) {
            // Reflection method throws ReflectionException
            reflectionMock.when(() ->
                        ReflectionUtil.findEnumImplementationsOfInterface(eq(InputComponentType.class),
                              eq("DOES_NOT_EXIST"),
                              nullable(String.class)))
                  .thenThrow(new ReflectionException("No enum found"));

            BadReflectionDTO dto = new BadReflectionDTO();

            // We expect that the ReflectionException will bubble up as runtime
            ReflectionException ex = assertThrows(ReflectionException.class,
                  () -> insertionService.insertData(dto));
            assertTrue(ex.getMessage().contains("No enum found"));

            // The insertion service should not be called at all
            Mockito.verifyNoInteractions(mockInsertion);
         }
      }

      @Test
      @DisplayName("When service registry has no matching Insertion => throws IllegalStateException")
      void testInsertDataNoServiceFound() {
         try (var reflectionMock = mockStatic(ReflectionUtil.class)) {
            // Reflection is successful
            reflectionMock.when(() ->
                        ReflectionUtil.findEnumImplementationsOfInterface(any(), anyString(), nullable(String.class)))
                  .thenReturn(TestInputFields.OTHER_INPUT);

            UnregisteredTypeDTO dto = new UnregisteredTypeDTO();
            // This field references AnotherComponentType, which we did NOT register => should fail
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                  () -> insertionService.insertData(dto));
            assertTrue(ex.getMessage().contains("No InsertionService registered for"),
                  "Expected exception about missing InsertionService");
            Mockito.verifyNoInteractions(mockInsertion);
         }
      }

      @Test
      @DisplayName("When annotation is missing => insertion is skipped without error (covers annotation == null check)")
      void testInsertDataNoAnnotation() {
         class NoAnnotationDTO {
            private String field = "test";
         }
         NoAnnotationDTO dto = new NoAnnotationDTO();

         // Even if reflection is set up, it won't matter because the field is not annotated
         insertionService.insertData(dto);

         // No interactions with mock insertion => no error
         Mockito.verifyNoInteractions(mockInsertion);
      }
   }

   // -----------------------------------------------------------
   // 3) DIRECT TESTS OF OVERRIDDEN METHODS
   //    (buildLocator, getOrder, etc.)
   // -----------------------------------------------------------
   @Nested
   @DisplayName("Overridden Methods Tests")
   class OverriddenMethodsTests {

      @Test
      @DisplayName("buildLocator should return By based on the annotation's locator")
      void testBuildLocator() throws NoSuchFieldException {
         // 1. Create a dummy class with a real @InsertionField
         class Dummy {
            @InsertionField(
                  locator = @FindBy(css = "div.test-locator"),
                  type = InputComponentType.class,
                  componentType = "MD_INPUT",
                  order = 1
            )
            private String field;
         }

         // 2. Get the annotation via reflection
         Field field = Dummy.class.getDeclaredField("field");
         InsertionField annotation = field.getAnnotation(InsertionField.class);

         // 3. Call buildLocator
         By by = insertionService.buildLocator(annotation);

         // 4. Assert
         assertNotNull(by);
         assertEquals(By.cssSelector("div.test-locator").toString(), by.toString());
      }

      @Test
      @DisplayName("getOrder simply returns annotation.order()")
      void testGetOrder() {
         InsertionField annotation = Mockito.mock(InsertionField.class);
         Mockito.when(annotation.order()).thenReturn(123);

         int order = insertionService.getOrder(annotation);
         assertEquals(123, order);
      }

      @SuppressWarnings("unchecked")
      @Test
      @DisplayName("getComponentTypeEnumClass returns annotation.type()")
      void testGetComponentTypeEnumClass() {
         InsertionField annotation = Mockito.mock(InsertionField.class);
         // Use an explicit type witness and a double cast to satisfy the generic bounds:
         Mockito.<Class<? extends ComponentType>>when(annotation.type())
               .thenReturn(InputComponentType.class);
         Class<? extends ComponentType> result = insertionService.getComponentTypeEnumClass(annotation);
         assertEquals(InputComponentType.class, result);
      }

      @Test
      @DisplayName("getAnnotationClass returns InsertionField.class")
      void testGetAnnotationClass() {
         assertEquals(InsertionField.class, insertionService.getAnnotationClass());
      }

      @Test
      @DisplayName("getType invokes ReflectionUtil.findEnumImplementationsOfInterface with annotation data")
      void testGetType() {
         // given
         InsertionField annotation = Mockito.mock(InsertionField.class);
         Mockito.when(annotation.componentType()).thenReturn("MD_INPUT");
         Mockito.<Class<? extends ComponentType>>when(annotation.type())
               .thenReturn(InputComponentType.class);

         try (var reflectionMock = mockStatic(ReflectionUtil.class)) {
            reflectionMock.when(() ->
                        ReflectionUtil.findEnumImplementationsOfInterface(
                              eq(InputComponentType.class),
                              eq("MD_INPUT"),
                              nullable(String.class)))
                  .thenReturn(TestInputFields.MD_INPUT);

            // when
            ComponentType result = insertionService.getType(annotation);

            // then
            assertEquals(TestInputFields.MD_INPUT, result);
            reflectionMock.verify(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                  eq(InputComponentType.class), eq("MD_INPUT"), nullable(String.class)));
         }
      }
   }

   // -----------------------------------------------------------
   // 4) LOGGING TEST
   // -----------------------------------------------------------
   @Nested
   @DisplayName("Logging Tests")
   class LoggingTests {
      @Test
      @DisplayName("After successful insertion, LogUI.info(...) is called")
      void testLogIsCalled() {
         ExampleDTOUI dto = new ExampleDTOUI();
         // We only need to mock reflection for the fields that have non-null values
         try (var reflectionMock = mockStatic(ReflectionUtil.class);
              var logUIMock = mockStatic(LogUi.class)) {

            reflectionMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                        eq(InputComponentType.class), eq("MD_INPUT"), nullable(String.class)))
                  .thenReturn(TestInputFields.MD_INPUT);

            insertionService.insertData(dto);

            // The final line in insertData() logs "Finished data insertion..."
            logUIMock.verify(() -> LogUi.info(Mockito.contains("Finished data insertion"), any()));
         }
      }
   }
}