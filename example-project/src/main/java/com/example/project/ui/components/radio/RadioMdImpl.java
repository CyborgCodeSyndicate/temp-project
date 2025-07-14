package com.example.project.ui.components.radio;

import com.example.project.ui.types.RadioFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.radio.Radio;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.ui.util.strategy.StrategyGenerator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;

@ImplementationOfType(RadioFieldTypes.MD_RADIO)
public class RadioMdImpl extends BaseComponent implements Radio {

   private static final By RADIO_ELEMENT_SELECTOR = By.tagName("mat-radio-button");
   private static final By RADIO_ELEMENT_CONTENT_LOCATOR = By.className("mat-radio-label-content");
   public static final String CHECKED_CLASS_INDICATOR = "mat-radio-checked";
   public static final String DISABLED_CLASS_INDICATOR = "mat-radio-disabled";
   public static final String VISIBLE_CLASS_INDICATOR = "hidden";


   public RadioMdImpl(SmartWebDriver driver) {
      super(driver);
   }


   @Override
   public void select(final SmartWebElement container, final String radioButtonText) {
      SmartWebElement radioButton = findRadioButton(container, radioButtonText, null);
      selectElement(radioButton);
   }


   @Override
   public String select(final SmartWebElement container, final Strategy strategy) {
      SmartWebElement radioButton = findRadioButton(container, null, strategy);
      return selectElement(radioButton);
   }


   @Override
   public void select(final String radioButtonText) {
      SmartWebElement radioButton = findRadioButton(null, radioButtonText, null);
      selectElement(radioButton);
   }


   @Override
   public void select(final By radioButtonLocator) {
      SmartWebElement radioButton = driver.findSmartElement(radioButtonLocator);
      selectElement(radioButton);
   }


   @Override
   public boolean isEnabled(final SmartWebElement container, final String radioButtonText) {
      SmartWebElement radioButton = findRadioButton(container, radioButtonText, null);
      return isElementEnabled(radioButton);
   }


   @Override
   public boolean isEnabled(final String radioButtonText) {
      SmartWebElement radioButton = findRadioButton(null, radioButtonText, null);
      return isElementEnabled(radioButton);
   }


   @Override
   public boolean isEnabled(final By radioButtonLocator) {
      SmartWebElement radioButton = driver.findSmartElement(radioButtonLocator);
      return isElementEnabled(radioButton);
   }


   @Override
   public boolean isSelected(final SmartWebElement container, final String radioButtonText) {
      SmartWebElement radioButton = findRadioButton(container, radioButtonText, null);
      return isElementSelected(radioButton);
   }


   @Override
   public boolean isSelected(final String radioButtonText) {
      SmartWebElement radioButton = findRadioButton(null, radioButtonText, null);
      return isElementSelected(radioButton);
   }


   @Override
   public boolean isSelected(final By radioButtonLocator) {
      SmartWebElement radioButton = driver.findSmartElement(radioButtonLocator);
      return isElementEnabled(radioButton);
   }


   @Override
   public boolean isVisible(final SmartWebElement container, final String radioButtonText) {
      SmartWebElement radioButton = findRadioButton(container, radioButtonText, null);
      return isElementVisible(radioButton);
   }


   @Override
   public boolean isVisible(final String radioButtonText) {
      SmartWebElement radioButton = findRadioButton(null, radioButtonText, null);
      return isElementVisible(radioButton);
   }


   @Override
   public boolean isVisible(final By radioButtonLocator) {
      SmartWebElement radioButton = driver.findSmartElement(radioButtonLocator);
      return isElementVisible(radioButton);
   }


   @Override
   public String getSelected(final SmartWebElement container) {
      SmartWebElement selectedRadioElement = container.findSmartElements(RADIO_ELEMENT_SELECTOR)
            .stream().filter(this::isElementSelected)
            .findFirst().orElseThrow(() -> new NoSuchElementException("There is not radio element in the container"));
      return getElementText(selectedRadioElement);
   }


   @Override
   public String getSelected(final By containerLocator) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return getSelected(container);
   }


   @Override
   public List<String> getAll(final SmartWebElement container) {
      return container.findSmartElements(RADIO_ELEMENT_SELECTOR)
            .stream().map(this::getElementText)
            .collect(Collectors.toList());
   }


   @Override
   public List<String> getAll(final By containerLocator) {
      SmartWebElement container = driver.findSmartElement(containerLocator);
      return getAll(container);
   }


   private SmartWebElement findRadioButton(SmartWebElement container, String value, Strategy strategy) {

      List<SmartWebElement> radioButtons = Objects.nonNull(container)
            ? container.findSmartElements(RADIO_ELEMENT_SELECTOR)
            : driver.findSmartElements(RADIO_ELEMENT_SELECTOR);

      SmartWebElement targetedRadioButton = null;

      if (Objects.nonNull(value)) {
         targetedRadioButton = radioButtons.stream().filter(
                     radio -> getElementText(radio)
                           .equalsIgnoreCase(value.trim())).findFirst()
               .orElseThrow(() -> new NotFoundException("Element with text: " + value + " can't be found"));
      }
      if (Objects.nonNull(strategy)) {

         targetedRadioButton = switch (strategy) {
            case RANDOM -> StrategyGenerator.getRandomElementFromElements(radioButtons);
            case FIRST -> StrategyGenerator.getFirstElementFromElements(radioButtons);
            case LAST -> StrategyGenerator.getLastElementFromElements(radioButtons);
            case ALL -> throw new IllegalArgumentException("Only single radio button can be selected");
         };
      }

      return targetedRadioButton;
   }


   private boolean isElementEnabled(SmartWebElement radioButtonElement) {
      return !Objects.requireNonNull(radioButtonElement.getAttribute("class")).contains(DISABLED_CLASS_INDICATOR);
   }


   private boolean isElementSelected(SmartWebElement radioButtonElement) {
      return Objects.requireNonNull(radioButtonElement.getAttribute("class")).contains(CHECKED_CLASS_INDICATOR);
   }


   private boolean isElementVisible(SmartWebElement radioButtonElement) {
      return !Objects.requireNonNull(radioButtonElement.getAttribute("class")).contains(VISIBLE_CLASS_INDICATOR);
   }


   private String getElementText(SmartWebElement radioButtonElement) {
      return radioButtonElement.findSmartElement(RADIO_ELEMENT_CONTENT_LOCATOR).getText().trim();
   }


   private String selectElement(SmartWebElement radioButtonElement) {
      String elementText = null;
      if (isElementSelected(radioButtonElement)) {
         elementText = getElementText(radioButtonElement);
      }
      if (isElementVisible(radioButtonElement) && isElementEnabled(radioButtonElement)
            && !isElementSelected(radioButtonElement)) {
         elementText = getElementText(radioButtonElement);
         String radioButtonClass = radioButtonElement.getAttribute("class");
         radioButtonElement.click();
         radioButtonElement.waitUntilAttributeValueIsChanged("class", radioButtonClass);
      }
      return elementText;
   }
}