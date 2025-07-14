package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.components.button.ButtonServiceImpl;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.link.LinkService;
import com.theairebellion.zeus.ui.components.link.LinkServiceImpl;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebDriver.Window;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("UIServiceFluent Test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UiServiceFluentTest extends BaseUnitUITest {

   private UiServiceFluent<?> sut;

   @Mock
   private WebDriver rawWebDriver;

   @Mock
   private Navigation navigation;

   @Mock
   private TargetLocator targetLocator;

   @Mock
   private Options options;

   @Mock
   private Window window;

   @Mock
   private SmartWebDriver driver;

   @Mock
   private Storage storage;

   @Mock
   private Quest originalQuest;

   private SuperQuest quest;

   @BeforeEach
   void setUp() throws Exception {
      // Setup WebDriver chain
      when(rawWebDriver.navigate()).thenReturn(navigation);
      when(rawWebDriver.switchTo()).thenReturn(targetLocator);
      when(rawWebDriver.manage()).thenReturn(options);
      when(options.window()).thenReturn(window);

      // Configure SmartWebDriver
      when(driver.getOriginal()).thenReturn(rawWebDriver);

      // Create the SUT with the mocked driver
      sut = new UiServiceFluent<>(driver);

      // Instead of mocking getStorage(), set the storage field directly on the original quest
      Field originalQuestStorageField = Quest.class.getDeclaredField("storage");
      originalQuestStorageField.setAccessible(true);
      originalQuestStorageField.set(originalQuest, storage);

      // Create SuperQuest with the mocked original Quest
      quest = new SuperQuest(originalQuest);

      // Access private Quest field in FluentService using reflection
      Field questField = FluentService.class.getDeclaredField("quest");
      questField.setAccessible(true);

      // Set the field directly with our SuperQuest
      questField.set(sut, quest);
   }

   @Test
   void constructorCoverage() {
      UiServiceFluent<?> other = new UiServiceFluent<>(driver);
      assertThat(other).isNotNull();
   }

   @Test
   @DisplayName("validate(Runnable) should delegate to super and return self")
   void validateRunnableShouldReturnSelf() {
      Runnable mockRunnable = mock(Runnable.class);
      UiServiceFluent<?> result = sut.validate(mockRunnable);
      assertThat(result).isSameAs(sut);
   }

   @Test
   @DisplayName("validate(Consumer<SoftAssertions>) should delegate to super and return self")
   void validateSoftAssertionsShouldReturnSelf() {
      Consumer<SoftAssertions> mockConsumer = mock(Consumer.class);
      UiServiceFluent<?> result = sut.validate(mockConsumer);
      assertThat(result).isSameAs(sut);
   }

   @Test
   void getDriverTest() {
      assertThat(sut.getDriver()).isSameAs(driver);
   }

   @Test
   @Disabled
   void postQuestSetupInitializationTest() throws Exception {

      // Create minimal test instance
      UiServiceFluent<?> testInstance = new UiServiceFluent<>(driver);

      // Create a mocked original quest
      Quest questSpy = spy(new Quest() {});
      Storage mockStorage = mock(Storage.class);

      // Set the storage field directly
      Field originalQuestStorageField = Quest.class.getDeclaredField("storage");
      originalQuestStorageField.setAccessible(true);
      originalQuestStorageField.set(questSpy, mockStorage);

      // Create SuperQuest with the mocked original Quest
      SuperQuest testQuest = new SuperQuest(questSpy);

      // Inject the quest
      Field questField = FluentService.class.getDeclaredField("quest");
      questField.setAccessible(true);
      questField.set(testInstance, testQuest);

      Method postQuestMethod = UiServiceFluent.class.getDeclaredMethod("postQuestSetupInitialization");
      postQuestMethod.setAccessible(true);
      postQuestMethod.invoke(testInstance);

      assertThat(testInstance.getInputField()).isNotNull();
      assertThat(testInstance.getButtonField()).isNotNull();
      assertThat(testInstance.getRadioField()).isNotNull();
      assertThat(testInstance.getCheckboxField()).isNotNull();
      assertThat(testInstance.getSelectField()).isNotNull();
      assertThat(testInstance.getListField()).isNotNull();
      assertThat(testInstance.getLoaderField()).isNotNull();
      assertThat(testInstance.getLinkField()).isNotNull();
      assertThat(testInstance.getAlertField()).isNotNull();
      assertThat(testInstance.getTabField()).isNotNull();
      assertThat(testInstance.getModalField()).isNotNull();
      assertThat(testInstance.getAccordionField()).isNotNull();
      assertThat(testInstance.getValidation()).isNotNull();
      assertThat(testInstance.getNavigation()).isNotNull();
      assertThat(testInstance.getInterceptor()).isNotNull();
      assertThat(testInstance.getTable()).isNotNull();
      assertThat(testInstance.getInsertionService()).isNotNull();
   }

   @Test
   void registerInsertionServicesTest() throws Exception {
      // Create a spy of the SUT
      UiServiceFluent<?> spySut = spy(sut);

      // Set up required fields using reflection
      InsertionServiceRegistry mockRegistry = mock(InsertionServiceRegistry.class);
      setPrivateField(spySut, "serviceRegistry", mockRegistry);

      RadioServiceFluent<?> mockRadioField = mock(RadioServiceFluent.class);
      setPrivateField(spySut, "radioField", mockRadioField);

      CheckboxServiceFluent<?> mockCheckboxField = mock(CheckboxServiceFluent.class);
      setPrivateField(spySut, "checkboxField", mockCheckboxField);

      SelectServiceFluent<?> mockSelectField = mock(SelectServiceFluent.class);
      setPrivateField(spySut, "selectField", mockSelectField);

      ListServiceFluent<?> mockListField = mock(ListServiceFluent.class);
      setPrivateField(spySut, "listField", mockListField);

      // Create a mock InputService
      InputService mockInputService = mock(InputService.class);

      // Access the private method via reflection
      Method registerMethod = UiServiceFluent.class.getDeclaredMethod("registerInsertionServices", InputService.class);
      registerMethod.setAccessible(true);

      // Call the method
      registerMethod.invoke(spySut, mockInputService);

      // Verify the registry calls
      verify(mockRegistry).registerService(RadioComponentType.class, mockRadioField);
      verify(mockRegistry).registerService(CheckboxComponentType.class, mockCheckboxField);
      verify(mockRegistry).registerService(SelectComponentType.class, mockSelectField);
      verify(mockRegistry).registerService(ItemListComponentType.class, mockListField);
      verify(mockRegistry).registerService(InputComponentType.class, mockInputService);
   }

   @Test
   void registerTableServicesTest() {
      try {
         // Create a spy of the SUT
         UiServiceFluent<?> spySut = spy(sut);

         // Set up TableServiceRegistry with a real object we can examine
         TableServiceRegistry registry = new TableServiceRegistry();
         setPrivateField(spySut, "tableServiceRegistry", registry);

         // Create mock services
         InputService mockInputService = mock(InputService.class);
         ButtonServiceImpl mockButtonService = mock(ButtonServiceImpl.class);
         LinkServiceImpl mockLinkService = mock(LinkServiceImpl.class);

         // Don't try to stub any instanceof checks

         // Access the private method via reflection
         Method registerMethod = UiServiceFluent.class.getDeclaredMethod(
               "registerTableServices",
               InputService.class,
               ButtonService.class,
               LinkService.class
         );
         registerMethod.setAccessible(true);

         // Call the method - we're testing that it executes without exceptions
         registerMethod.invoke(spySut, mockInputService, mockButtonService, mockLinkService);

         // The method execution without exceptions is sufficient for this test
      } catch (Exception ignored) {
         fail();
      }
   }

   // Helper method to set private fields using reflection
   private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
      Field field = UiServiceFluent.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
   }
}