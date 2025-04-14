package com.theairebellion.zeus.ui.service.facade;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.link.LinkComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
@DisplayName("UIService Tests")
class UiServiceTest extends BaseUnitUITest {

   @Mock
   private WebDriver webDriver;

   private SmartWebDriver smartWebDriver;
   private UiService uiService;

   @BeforeEach
   void setUp() {
      smartWebDriver = new SmartWebDriver(webDriver);
   }

   @Test
   @DisplayName("Should initialize all service components")
   void testInitializeAllComponents() {
      // When
      uiService = new UiService(smartWebDriver);

      // Then
      assertNotNull(uiService.getDriver());
      assertNotNull(uiService.getInputField());
      assertNotNull(uiService.getButtonField());
      assertNotNull(uiService.getRadioField());
      assertNotNull(uiService.getCheckboxField());
      assertNotNull(uiService.getToggleField());
      assertNotNull(uiService.getSelectField());
      assertNotNull(uiService.getListField());
      assertNotNull(uiService.getLoaderField());
      assertNotNull(uiService.getLinkField());
      assertNotNull(uiService.getAlertField());
      assertNotNull(uiService.getTabField());
      assertNotNull(uiService.getServiceRegistry());
      assertNotNull(uiService.getInsertionService());
      assertNotNull(uiService.getTableServiceRegistry());
      assertNotNull(uiService.getTableService());
      assertNotNull(uiService.getUiTableValidator());
   }

   @Test
   @DisplayName("Should register all services in the insertion registry")
   void testInsertionRegistryServices() {
      // When
      uiService = new UiService(smartWebDriver);
      InsertionServiceRegistry registry = uiService.getServiceRegistry();

      // Then - verify registries contain expected services
      // Check input registration
      Insertion inputService = registry.getService(InputComponentType.class);
      assertNotNull(inputService);
      assertSame(uiService.getInputField(), inputService);

      // Check radio registration
      Insertion radioService = registry.getService(RadioComponentType.class);
      assertNotNull(radioService);
      assertSame(uiService.getRadioField(), radioService);

      // Check checkbox registration
      Insertion checkboxService = registry.getService(CheckboxComponentType.class);
      assertNotNull(checkboxService);
      assertSame(uiService.getCheckboxField(), checkboxService);

      // Check select registration
      Insertion selectService = registry.getService(SelectComponentType.class);
      assertNotNull(selectService);
      assertSame(uiService.getSelectField(), selectService);

      // Check list registration
      Insertion listService = registry.getService(ItemListComponentType.class);
      assertNotNull(listService);
      assertSame(uiService.getListField(), listService);
   }

   @Test
   @DisplayName("Should register all services in the table registry")
   void testTableRegistryServices() {
      // When
      uiService = new UiService(smartWebDriver);
      TableServiceRegistry registry = uiService.getTableServiceRegistry();

      // Then - verify registries contain expected services
      // Check button registration
      TableInsertion buttonService = registry.getTableService(ButtonComponentType.class);
      assertNotNull(buttonService);

      // Check link registration
      TableInsertion linkService = registry.getTableService(LinkComponentType.class);
      assertNotNull(linkService);

      // Check input registration as TableFilter
      TableFilter inputFilterService = registry.getFilterService(InputComponentType.class);
      assertNotNull(inputFilterService);

      // Check input registration as TableInsertion
      TableInsertion inputInsertionService = registry.getTableService(InputComponentType.class);
      assertNotNull(inputInsertionService);
   }

   @Test
   @DisplayName("Should pass the driver to all components")
   void testDriverPropagation() {
      // When
      uiService = new UiService(smartWebDriver);

      // Then - verify driver is passed correctly
      assertSame(smartWebDriver, uiService.getDriver());
   }
}