package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAccordionComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAccordionService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAccordionUiElement;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("AccordionServiceFluent Tests")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccordionServiceFluentTest extends BaseUnitUITest {

   private MockAccordionService mockService;
   private AccordionServiceFluent<UiServiceFluent<?>> sut;
   private Storage storage;
   private Storage storageUI;
   private UiServiceFluent<?> uiServiceFluent;
   private SmartWebDriver driver;
   private MockAccordionUiElement element;

   @BeforeEach
   void setUp() {
      mockService = new MockAccordionService();
      storage = mock(Storage.class);
      storageUI = mock(Storage.class);
      uiServiceFluent = mock(UiServiceFluent.class);
      driver = mock(SmartWebDriver.class);

      // Create a real MockAccordionUIElement instance
      By locator = By.id("testAccordion");
      element = new MockAccordionUiElement(locator, MockAccordionComponentType.DUMMY);

      doReturn(uiServiceFluent)
            .when(uiServiceFluent)
            .validate(any(Runnable.class));

      doReturn(uiServiceFluent)
            .when(uiServiceFluent)
            .validate(any(java.util.function.Consumer.class));

      when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);

      // Create the SUT using our real MockAccordionService
      sut = new AccordionServiceFluent<>(uiServiceFluent, storage, mockService, driver);
   }

   @Test
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
   void areEnabledTest() {
      mockService.reset();

      // Act
      sut.areEnabled(element);

      // Verify
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).containsExactly(element.locator());
      verify(storageUI).put(element.enumImpl(), true);

      // Verify fluent return
      assertThat(sut.areEnabled(element)).isSameAs(uiServiceFluent);
   }

   @Test
   void getTitleTest() {
      mockService.reset();

      // Act
      sut.getTitle(element);

      // Verify
      verify(storageUI).put(element.enumImpl(), MockAccordionService.TITLE);
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).hasSize(1);
      assertThat(mockService.lastAccordionLocators[0]).isEqualTo(element.locator());

      // Verify fluent return
      assertThat(sut.getTitle(element)).isSameAs(uiServiceFluent);
   }

   @Test
   void getTextTest() {
      mockService.reset();

      // Act
      sut.getText(element);

      // Verify
      verify(storageUI).put(element.enumImpl(), MockAccordionService.TEXT);
      assertThat(mockService.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
      assertThat(mockService.lastAccordionLocators).hasSize(1);
      assertThat(mockService.lastAccordionLocators[0]).isEqualTo(element.locator());

      // Verify fluent return
      assertThat(sut.getText(element)).isSameAs(uiServiceFluent);
   }
}