package com.theairebellion.zeus.ui.service.facade;

import com.theairebellion.zeus.ui.components.alert.AlertService;
import com.theairebellion.zeus.ui.components.alert.AlertServiceImpl;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.components.button.ButtonServiceImpl;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxService;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxServiceImpl;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
import com.theairebellion.zeus.ui.components.link.LinkComponentType;
import com.theairebellion.zeus.ui.components.link.LinkService;
import com.theairebellion.zeus.ui.components.link.LinkServiceImpl;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListService;
import com.theairebellion.zeus.ui.components.list.ItemListServiceImpl;
import com.theairebellion.zeus.ui.components.loader.LoaderService;
import com.theairebellion.zeus.ui.components.loader.LoaderServiceImpl;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioService;
import com.theairebellion.zeus.ui.components.radio.RadioServiceImpl;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.select.SelectService;
import com.theairebellion.zeus.ui.components.select.SelectServiceImpl;
import com.theairebellion.zeus.ui.components.tab.TabService;
import com.theairebellion.zeus.ui.components.tab.TabServiceImpl;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.service.TableService;
import com.theairebellion.zeus.ui.components.table.service.TableServiceImpl;
import com.theairebellion.zeus.ui.components.toggle.ToggleService;
import com.theairebellion.zeus.ui.components.toggle.ToggleServiceImpl;
import com.theairebellion.zeus.ui.insertion.InsertionService;
import com.theairebellion.zeus.ui.insertion.InsertionServiceFieldImpl;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.validator.UiTableValidator;
import com.theairebellion.zeus.ui.validator.UiTableValidatorImpl;
import lombok.Getter;

/**
 * Centralized UI service facade that provides access to various UI component services.
 *
 * <p>This class serves as a single entry point for interacting with different UI components
 * such as input fields, buttons, checkboxes, toggles, tables, and more.
 * It initializes and manages instances of these services, allowing simplified access
 * throughout the application.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
public class UiService {

   private final SmartWebDriver driver;
   private final InputService inputField;
   private final ButtonService buttonField;
   private final RadioService radioField;
   private final CheckboxService checkboxField;
   private final ToggleService toggleField;
   private final SelectService selectField;
   private final ItemListService listField;
   private final LoaderService loaderField;
   private final LinkService linkField;
   private final AlertService alertField;
   private final TabService tabField;
   private final InsertionServiceRegistry serviceRegistry;
   private final InsertionService insertionService;
   private final TableServiceRegistry tableServiceRegistry;
   private final TableService tableService;
   private final UiTableValidator uiTableValidator;

   /**
    * Constructs the UI service, initializing various UI component services.
    *
    * @param driver The {@link SmartWebDriver} instance used for UI interactions.
    */
   public UiService(SmartWebDriver driver) {
      this.driver = driver;
      inputField = new InputServiceImpl(driver);
      buttonField = new ButtonServiceImpl(driver);
      radioField = new RadioServiceImpl(driver);
      selectField = new SelectServiceImpl(driver);
      listField = new ItemListServiceImpl(driver);
      loaderField = new LoaderServiceImpl(driver);
      linkField = new LinkServiceImpl(driver);
      alertField = new AlertServiceImpl(driver);
      tabField = new TabServiceImpl(driver);
      checkboxField = new CheckboxServiceImpl(driver);
      toggleField = new ToggleServiceImpl(driver);
      serviceRegistry = new InsertionServiceRegistry();
      tableServiceRegistry = new TableServiceRegistry();
      registerInsertionServices();
      insertionService = new InsertionServiceFieldImpl(serviceRegistry);
      uiTableValidator = new UiTableValidatorImpl();
      tableService = new TableServiceImpl(driver, tableServiceRegistry, uiTableValidator);
   }

   /**
    * Registers various UI component services to the respective registries.
    *
    * <p>This method ensures that UI components such as input fields, radio buttons,
    * checkboxes, select fields, item lists, and table components are registered
    * so they can be properly managed and accessed throughout the application.
    */
   private void registerInsertionServices() {
      serviceRegistry.registerService(InputComponentType.class, inputField);
      serviceRegistry.registerService(RadioComponentType.class, radioField);
      serviceRegistry.registerService(CheckboxComponentType.class, checkboxField);
      serviceRegistry.registerService(SelectComponentType.class, selectField);
      serviceRegistry.registerService(ItemListComponentType.class, listField);
      tableServiceRegistry.registerService(ButtonComponentType.class, buttonField);
      tableServiceRegistry.registerService(LinkComponentType.class, linkField);
      tableServiceRegistry.registerService(InputComponentType.class, (TableFilter) inputField);
      tableServiceRegistry.registerService(InputComponentType.class, (TableInsertion) inputField);
   }

}
