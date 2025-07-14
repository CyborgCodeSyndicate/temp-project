package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAccordionComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAccordionService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAccordionUiElement;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import java.util.function.Consumer;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("AccordionServiceFluent Tests")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccordionServiceFluentTest extends BaseUnitUITest {

   private MockAccordionService mockService;
   private AccordionServiceFluent<UiServiceFluent<?>> sut;
   private Storage storageUI;
   private UiServiceFluent<?> uiServiceFluent;
   private MockAccordionUiElement element;

   @BeforeEach
   void setUp() {
      mockService = new MockAccordionService();
      Storage storage = mock(Storage.class);
      storageUI = mock(Storage.class);
      uiServiceFluent = mock(UiServiceFluent.class);
      SmartWebDriver driver = mock(SmartWebDriver.class);

      // Create a real MockAccordionUIElement instance
      By locator = By.id("testAccordion");
      element = new MockAccordionUiElement(locator, MockAccordionComponentType.DUMMY);

      doAnswer(invocation -> {
         Runnable runnable = invocation.getArgument(0);
         runnable.run(); // now your assertion runs
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Runnable.class));

      doAnswer(invocation -> {
         Consumer<SoftAssertions> consumer = invocation.getArgument(0);
         SoftAssertions softly = new SoftAssertions();
         consumer.accept(softly); // run assertions
         softly.assertAll(); // to trigger any collected failures
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Consumer.class));

      when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);

      // Create the SUT using our real MockAccordionService
      sut = new AccordionServiceFluent<>(uiServiceFluent, storage, mockService, driver);
   }

   @Test
   @DisplayName("expand should trigger expand logic and return fluent instance")
   void expandTest() {
      mockService.reset();

      // Act
      sut.expand(element);

      // Verify
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());

      // Verify fluent return
      assertThat(sut.expand(element)).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("collapse should trigger collapse logic and return fluent instance")
   void collapseTest() {
      mockService.reset();

      // Act
      sut.collapse(element);

      // Verify
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());

      // Verify fluent return
      assertThat(sut.collapse(element)).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("areEnabled should store enabled state and return fluent instance")
   void areEnabledTest() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = true;
      mockService.returnEnabled = shouldBeEnabled;
      String elementText = "Example1";

      // Act
      sut.areEnabled(element, elementText);

      // Verify
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionText).containsExactly(elementText);
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);

      // Verify fluent return
      assertThat(sut.areEnabled(element, elementText)).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateAreEnabled should assert enabled values and return fluent instance")
   void validateAreEnabledTest() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = true;
      mockService.returnEnabled = shouldBeEnabled;
      String[] values = {"Accordion1", "Accordion2"};

      // When
      UiServiceFluent<?> result = sut.validateAreEnabled(element, values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionText).isEqualTo(values);
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateAreEnabled should assert with soft assertions")
   void validateAreEnabledWithSoftAssertionTest() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = true;
      mockService.returnEnabled = shouldBeEnabled;
      String[] values = {"Accordion1", "Accordion2"};

      // When
      UiServiceFluent<?> result = sut.validateAreEnabled(element, true, values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionText).isEqualTo(values);
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateAreDisabled should assert disabled values and return fluent instance")
   void validateAreDisabledTest() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = false;
      mockService.returnEnabled = shouldBeEnabled;
      String[] values = {"Accordion1", "Accordion2"};

      // When
      UiServiceFluent<?> result = sut.validateAreDisabled(element, values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionText).isEqualTo(values);
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateAreDisabled should assert with soft assertions")
   void validateAreDisabledWithSoftAssertionTest() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = false;
      mockService.returnEnabled = shouldBeEnabled;
      String[] values = {"Accordion1", "Accordion2"};

      // When
      UiServiceFluent<?> result = sut.validateAreDisabled(element, true, values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionText).isEqualTo(values);
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("isEnabled should store enabled state and return fluent instance")
   void isEnabledTest() {
      mockService.reset();
      boolean shouldBeEnabled = true;
      mockService.returnEnabled = shouldBeEnabled;

      // Act
      sut.isEnabled(element);

      // Verify
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);

      // Verify fluent return
      assertThat(sut.isEnabled(element)).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsEnabled should assert enabled values and return fluent instance")
   void validateIsEnabledTest() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = true;
      mockService.returnEnabled = shouldBeEnabled;
      String value = "Accordion1";

      // When
      UiServiceFluent<?> result = sut.validateIsEnabled(element, value);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionText).containsExactly(value);
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsEnabled should assert with soft assertions")
   void validateIsEnabledWithSoftAssertionTest() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = true;
      mockService.returnEnabled = shouldBeEnabled;
      String value = "Accordion1";

      // When
      UiServiceFluent<?> result = sut.validateIsEnabled(element, true, value);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionText).containsExactly(value);
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("isDisabled should assert disabled value and return fluent instance")
   void validateIsDisabledTest() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = false;
      mockService.returnEnabled = shouldBeEnabled;
      String value = "Accordion1";

      // When
      UiServiceFluent<?> result = sut.validateIsDisabled(element, value);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionText).containsExactly(value);
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsDisabled should assert with soft assertions")
   void validateIsDisabledWithSoftAssertionTest() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = false;
      mockService.returnEnabled = shouldBeEnabled;
      String value = "Accordion1";

      // When
      UiServiceFluent<?> result = sut.validateIsDisabled(element, true, value);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionText).containsExactly(value);
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getExpanded should fetch expanded accordion items and return fluent instance")
   void getExpandedTest() {
      mockService.reset();

      // Act
      sut.getExpanded(element);

      // Verify
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());
      verify(storageUI).put(element.enumImpl(), MockAccordionService.EXPANDED_LIST);

      // Verify fluent return
      assertThat(sut.getExpanded(element)).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateExpandedItems should validate expanded accordion items and return fluent instance")
   void validateExpandedItemsTest() {
      // Given
      mockService.reset();
      String[] values = MockAccordionService.EXPANDED_LIST.toArray(new String[0]);

      // When
      UiServiceFluent<?> result = sut.validateExpandedItems(element, values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), MockAccordionService.EXPANDED_LIST);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateExpandedItems should soft-assert expanded accordion items and return fluent instance")
   void validateExpandedItemsWithSoftAssertionTest() {
      // Given
      mockService.reset();
      String[] values = MockAccordionService.EXPANDED_LIST.toArray(new String[0]);

      // When
      UiServiceFluent<?> result = sut.validateExpandedItems(element, true, values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), MockAccordionService.EXPANDED_LIST);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getCollapsed should fetch collapsed accordion items and return fluent instance")
   void getCollapsedTest() {
      mockService.reset();

      // Act
      sut.getCollapsed(element);

      // Verify
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());
      verify(storageUI).put(element.enumImpl(), MockAccordionService.COLLAPSED_LIST);

      // Verify fluent return
      assertThat(sut.getCollapsed(element)).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateCollapsedItems should validate collapsed accordion items and return fluent instance")
   void validateCollapsedItemsTest() {
      // Given
      mockService.reset();
      String[] values = MockAccordionService.COLLAPSED_LIST.toArray(new String[0]);

      // When
      UiServiceFluent<?> result = sut.validateCollapsedItems(element, values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), MockAccordionService.COLLAPSED_LIST);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateCollapsedItems should soft-assert collapsed accordion items and return fluent instance")
   void validateCollapsedItemsWithSoftAssertionTest() {
      // Given
      mockService.reset();
      String[] values = MockAccordionService.COLLAPSED_LIST.toArray(new String[0]);

      // When
      UiServiceFluent<?> result = sut.validateCollapsedItems(element, true, values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), MockAccordionService.COLLAPSED_LIST);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getAll should fetch all accordion items and return fluent instance")
   void getAllTest() {
      mockService.reset();

      // Act
      sut.getAll(element);

      // Verify
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());
      verify(storageUI).put(element.enumImpl(), MockAccordionService.ALL_LIST);

      // Verify fluent return
      assertThat(sut.getAll(element)).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateAllAccordions should validate all accordion items and return fluent instance")
   void validateAllAccordionsTest() {
      // Given
      mockService.reset();
      String[] values = MockAccordionService.ALL_LIST.toArray(new String[0]);

      // When
      UiServiceFluent<?> result = sut.validateAllAccordions(element, values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), MockAccordionService.ALL_LIST);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateAllAccordions should soft-assert all accordion items and return fluent instance")
   void validateAllAccordionsWithSoftAssertionTest() {
      // Given
      mockService.reset();
      String[] values = MockAccordionService.ALL_LIST.toArray(new String[0]);

      // When
      UiServiceFluent<?> result = sut.validateAllAccordions(element, true, values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), MockAccordionService.ALL_LIST);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getTitle should fetch accordion title and return fluent instance")
   void getTitleTest() {
      mockService.reset();

      // Act
      sut.getTitle(element);

      // Verify
      verify(storageUI).put(element.enumImpl(), MockAccordionService.TITLE);
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).hasSize(1);
      assertThat(mockService.lastAccordionLocators[0]).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), MockAccordionService.TITLE);

      // Verify fluent return
      assertThat(sut.getTitle(element)).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateTitle should validate accordion title and return fluent instance")
   void validateTitleTest() {
      // Given
      mockService.reset();
      String value = MockAccordionService.TITLE;

      // When
      UiServiceFluent<?> result = sut.validateTitle(element, value);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());
      verify(storageUI).put(element.enumImpl(), MockAccordionService.TITLE);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateTitle should soft-assert accordion title and return fluent instance")
   void validateTitleWithSoftAssertionTest() {
      // Given
      mockService.reset();
      String value = MockAccordionService.TITLE;

      // When
      UiServiceFluent<?> result = sut.validateTitle(element, true, value);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());
      verify(storageUI).put(element.enumImpl(), MockAccordionService.TITLE);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getText should fetch accordion text content and return fluent instance")
   void getTextTest() {
      mockService.reset();

      // Act
      sut.getText(element);

      // Verify
      verify(storageUI).put(element.enumImpl(), MockAccordionService.TEXT);
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).hasSize(1);
      assertThat(mockService.lastAccordionLocators[0]).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), MockAccordionService.TEXT);

      // Verify fluent return
      assertThat(sut.getText(element)).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateText should validate accordion text content and return fluent instance")
   void validateTextTest() {
      // Given
      mockService.reset();
      String value = MockAccordionService.TEXT;

      // When
      UiServiceFluent<?> result = sut.validateText(element, value);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());
      verify(storageUI).put(element.enumImpl(), MockAccordionService.TEXT);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateText should soft-assert accordion text content and return fluent instance")
   void validateTextWithSoftAssertionTest() {
      // Given
      mockService.reset();
      String value = MockAccordionService.TEXT;

      // When
      UiServiceFluent<?> result = sut.validateText(element, true, value);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());
      verify(storageUI).put(element.enumImpl(), MockAccordionService.TEXT);
      assertThat(result).isSameAs(uiServiceFluent);
   }
}