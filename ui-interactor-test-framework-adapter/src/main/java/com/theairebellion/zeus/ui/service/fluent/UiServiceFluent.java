package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.ui.components.accordion.AccordionServiceImpl;
import com.theairebellion.zeus.ui.components.alert.AlertServiceImpl;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.components.button.ButtonServiceImpl;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxServiceImpl;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
import com.theairebellion.zeus.ui.components.link.LinkComponentType;
import com.theairebellion.zeus.ui.components.link.LinkService;
import com.theairebellion.zeus.ui.components.link.LinkServiceImpl;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListServiceImpl;
import com.theairebellion.zeus.ui.components.loader.LoaderServiceImpl;
import com.theairebellion.zeus.ui.components.modal.ModalServiceImpl;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioServiceImpl;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.select.SelectServiceImpl;
import com.theairebellion.zeus.ui.components.tab.TabServiceImpl;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.service.TableServiceImpl;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.InsertionServiceElementImpl;
import com.theairebellion.zeus.ui.service.tables.TableServiceFluent;
import com.theairebellion.zeus.ui.validator.UiTableValidator;
import com.theairebellion.zeus.ui.validator.UiTableValidatorImpl;
import com.theairebellion.zeus.validator.core.AssertionResult;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Provides a fluent interface for UI interactions, encapsulating various UI services
 * such as buttons, inputs, checkboxes, tables, and more.
 *
 * <p>This class serves as a core service for UI automation and validation, allowing
 * seamless interaction with UI components while maintaining fluent method chaining.
 * It extends {@link FluentService}, integrating common UI operations.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@TestService("UI")
@Getter
@SuppressWarnings("unchecked")
public class UiServiceFluent<T extends UiServiceFluent<?>> extends FluentService {

   private InputServiceFluent<T> inputField;
   private ButtonServiceFluent<T> buttonField;
   private RadioServiceFluent<T> radioField;
   private CheckboxServiceFluent<T> checkboxField;
   private SelectServiceFluent<T> selectField;
   private ListServiceFluent<T> listField;
   private LoaderServiceFluent<T> loaderField;
   private LinkServiceFluent<T> linkField;
   private AlertServiceFluent<T> alertField;
   private TabServiceFluent<T> tabField;
   private ModalServiceFluent<T> modalField;
   private AccordionServiceFluent<T> accordionField;
   private TableServiceFluent<T> table;
   private ValidationServiceFluent<T> validation;
   private NavigationServiceFluent<T> navigation;
   private SmartWebDriver driver;
   private InterceptorServiceFluent<T> interceptor;
   private InsertionServiceRegistry serviceRegistry;
   private TableServiceRegistry tableServiceRegistry;
   private InsertionServiceFluent<T> insertionService;

   /**
    * Constructs a new {@code UIServiceFluent} instance with the specified WebDriver.
    *
    * @param driver The {@link SmartWebDriver} instance used for UI interactions.
    */
   @Autowired
   public UiServiceFluent(SmartWebDriver driver) {
      this.driver = driver;
   }

   /**
    * Executes a validation assertion.
    *
    * @param assertion The assertion to validate.
    * @return The current instance of {@code UIServiceFluent} for method chaining.
    */
   public T validate(Runnable assertion) {
      return (T) super.validate(assertion);
   }

   /**
    * Executes a validation assertion using a soft assertion approach.
    *
    * @param assertion The assertion to validate with soft assertions.
    * @return The current instance of {@code UIServiceFluent} for method chaining.
    */
   public T validate(Consumer<SoftAssertions> assertion) {
      return (T) super.validate(assertion);
   }

   /**
    * Initializes the necessary UI services and registers them for UI interactions.
    * This method is automatically called after setup.
    */
   @Override
   protected void postQuestSetupInitialization() {
      ButtonServiceImpl buttonService = new ButtonServiceImpl(driver);
      LinkServiceImpl linkService = new LinkServiceImpl(driver);
      buttonField = new ButtonServiceFluent(this, quest.getStorage(), buttonService, driver);
      linkField = new LinkServiceFluent(this, quest.getStorage(), linkService, driver);
      radioField = new RadioServiceFluent(this, quest.getStorage(), new RadioServiceImpl(driver), driver);
      checkboxField = new CheckboxServiceFluent(this, quest.getStorage(), new CheckboxServiceImpl(driver), driver);
      selectField = new SelectServiceFluent(this, quest.getStorage(), new SelectServiceImpl(driver), driver);
      listField = new ListServiceFluent(this, quest.getStorage(), new ItemListServiceImpl(driver), driver);
      loaderField = new LoaderServiceFluent(this, quest.getStorage(), new LoaderServiceImpl(driver), driver);
      alertField = new AlertServiceFluent(this, quest.getStorage(), new AlertServiceImpl(driver), driver);
      tabField = new TabServiceFluent(this, quest.getStorage(), new TabServiceImpl(driver), driver);
      modalField = new ModalServiceFluent(this, quest.getStorage(), new ModalServiceImpl(driver), driver);
      accordionField = new AccordionServiceFluent(this, quest.getStorage(), new AccordionServiceImpl(driver), driver);
      validation = new ValidationServiceFluent(this, driver);
      navigation = new NavigationServiceFluent(this, driver);
      interceptor = new InterceptorServiceFluent(this, quest.getStorage());
      InputService inputService = new InputServiceImpl(driver);
      inputField = new InputServiceFluent(this, quest.getStorage(), inputService, driver);
      serviceRegistry = new InsertionServiceRegistry();
      registerInsertionServices(inputService);
      tableServiceRegistry = new TableServiceRegistry();
      registerTableServices(inputService, buttonService, linkService);
      UiTableValidator uiTableValidator = new UiTableValidatorImpl();
      table =
            new TableServiceFluent(this, quest.getStorage(),
                  new TableServiceImpl(driver, tableServiceRegistry, uiTableValidator),
                  driver);
      insertionService = new InsertionServiceFluent(
            new InsertionServiceElementImpl(serviceRegistry, driver), this,
            quest.getStorage());
   }

   /**
    * Registers insertion services for different UI components.
    *
    * @param inputService The input service to register.
    */
   private void registerInsertionServices(InputService inputService) {
      serviceRegistry.registerService(RadioComponentType.class, radioField);
      serviceRegistry.registerService(CheckboxComponentType.class, checkboxField);
      serviceRegistry.registerService(SelectComponentType.class, selectField);
      serviceRegistry.registerService(ItemListComponentType.class, listField);
      serviceRegistry.registerService(InputComponentType.class, inputService);
   }

   /**
    * Registers table services for different UI components.
    *
    * @param inputService  The input service to register.
    * @param buttonService The button service to register.
    * @param linkService   The link service to register.
    */
   private void registerTableServices(InputService inputService, ButtonService buttonService,
                                      LinkService linkService) {
      tableServiceRegistry.registerService(InputComponentType.class, (TableFilter) inputService);
      tableServiceRegistry.registerService(InputComponentType.class, (TableInsertion) inputService);
      tableServiceRegistry.registerService(ButtonComponentType.class, buttonService);
      tableServiceRegistry.registerService(LinkComponentType.class, linkService);
   }

   /**
    * Retrieves the {@link SmartWebDriver} instance used for UI interactions.
    *
    * @return The {@link SmartWebDriver} instance.
    */
   protected SmartWebDriver getDriver() {
      return driver;
   }

   /**
    * Executes validation assertions on a list of results.
    *
    * @param assertionResults The list of assertion results.
    */
   @Override
   protected void validation(List<AssertionResult<Object>> assertionResults) {
      super.validation(assertionResults);
   }

}
