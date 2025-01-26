package com.example.project.ui.components.checkbox;

import com.example.project.ui.types.CheckboxFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.checkbox.Checkbox;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.ui.util.strategy.StrategyGenerator.*;


@ImplementationOfType(CheckboxFieldTypes.MD_CHECKBOX)
public class CheckboxMDImpl extends BaseComponent implements Checkbox {

    private static final By CHECKBOX_ELEMENT_SELECTOR = By.tagName("mat-checkbox");
    private static final String CHECKED_CLASS_INDICATOR = "checked";
    private static final String DISABLED_STATE = "disabled";
    public static final By CHECKBOX_LABEL_LOCATOR = By.className("mat-checkbox-label");


    public CheckboxMDImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }

    @Override
    public void select(WebElement container, String... checkBoxText) {
        performActionOnCheckboxes(container, checkBoxText, true);
    }

    @Override
    public String select(WebElement container, Strategy strategy) {
        return performActionOnCheckboxesWithStrategy(container, strategy, true);
    }

    @Override
    public void select(String... checkBoxText) {
        performActionOnCheckboxes(null, checkBoxText, true);
    }

    @Override
    public void select(By... checkBoxLocator) {
        performActionOnCheckboxesByLocator(checkBoxLocator, true);
    }

    @Override
    public void deSelect(WebElement container, String... checkBoxText) {
        performActionOnCheckboxes(container, checkBoxText, false);
    }

    @Override
    public String deSelect(WebElement container, Strategy strategy) {
        return performActionOnCheckboxesWithStrategy(container, strategy, false);
    }

    @Override
    public void deSelect(String... checkBoxText) {
        performActionOnCheckboxes(null, checkBoxText, false);
    }

    @Override
    public void deSelect(By... checkBoxLocator) {
        performActionOnCheckboxesByLocator(checkBoxLocator, false);
    }

    @Override
    public boolean areSelected(WebElement container, String... checkBoxText) {
        return checkCheckboxState(container, checkBoxText);
    }

    @Override
    public boolean areSelected(String... checkBoxText) {
        return checkCheckboxState(null, checkBoxText);
    }

    @Override
    public boolean areSelected(By... checkBoxLocator) {
        return checkCheckboxStateByLocator(checkBoxLocator);
    }

    @Override
    public boolean areEnabled(WebElement container, String... checkBoxText) {
        return checkCheckboxEnabledState(container, checkBoxText);
    }

    @Override
    public boolean areEnabled(String... checkBoxText) {
        return checkCheckboxEnabledState(null, checkBoxText);
    }

    @Override
    public boolean areEnabled(By... checkBoxLocator) {
        return checkCheckboxEnabledStateByLocator(checkBoxLocator);
    }

    @Override
    public List<String> getSelected(WebElement container) {
        List<WebElement> checkBoxes = findCheckboxes(container, true);
        return checkBoxes.stream().map(this::getLabel).collect(Collectors.toList());
    }

    @Override
    public List<String> getSelected(By containerLocator) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return getSelected(container);
    }

    @Override
    public List<String> getAll(WebElement container) {
        List<WebElement> checkBoxes = findCheckboxes(container, null);
        return checkBoxes.stream().map(this::getLabel).collect(Collectors.toList());
    }

    @Override
    public List<String> getAll(By containerLocator) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return getAll(container);
    }

    private List<WebElement> findCheckboxes(WebElement container, Boolean onlySelected) {
        List<WebElement> checkBoxes = container != null
                ? smartSelenium.waitAndFindElements(container, CHECKBOX_ELEMENT_SELECTOR)
                : smartSelenium.smartFindElements(CHECKBOX_ELEMENT_SELECTOR);
        if (Objects.isNull(onlySelected)) {
            return checkBoxes;
        }
        return onlySelected ? checkBoxes.stream().filter(this::isChecked).collect(Collectors.toList()) :
                checkBoxes.stream().filter(checkBox -> !isChecked(checkBox)).collect(Collectors.toList());
    }

    private void performActionOnCheckboxes(WebElement container, String[] checkBoxText, boolean select) {
        List<WebElement> checkBoxes = findCheckboxes(container, !select);
        checkBoxes = filterCheckboxesByLabel(checkBoxes, checkBoxText);
        checkBoxes.forEach(this::clickIfEnabled);
    }

    private String performActionOnCheckboxesWithStrategy(WebElement container, Strategy strategy, boolean select) {
        List<WebElement> checkBoxes = findCheckboxes(container, !select);
        return applyStrategyAndClick(checkBoxes, strategy);
    }

    private void performActionOnCheckboxesByLocator(By[] checkBoxLocator, boolean select) {
        List<WebElement> checkBoxes = Arrays.stream(checkBoxLocator)
                .map(smartSelenium::waitAndFindElement)
                .collect(Collectors.toList());
        checkBoxes = checkBoxes.stream().filter(checkBox -> select != isChecked(checkBox)).toList();
        checkBoxes.forEach(this::clickIfEnabled);
    }

    private boolean checkCheckboxState(WebElement container, String[] checkBoxText) {
        List<WebElement> checkBoxes = findCheckboxes(container, true);
        Set<String> labelSet = Set.of(checkBoxText);
        List<WebElement> matchingCheckBoxes = checkBoxes.stream()
                .filter(checkBox -> labelSet.contains(getLabel(checkBox)))
                .toList();
        return matchingCheckBoxes.size() == checkBoxText.length;
    }

    private boolean checkCheckboxStateByLocator(By[] checkBoxLocator) {
        return Arrays.stream(checkBoxLocator)
                .map(smartSelenium::waitAndFindElement)
                .allMatch(this::isChecked);
    }

    private boolean checkCheckboxEnabledState(WebElement container, String[] checkBoxText) {
        List<WebElement> checkBoxes = findCheckboxes(container, null);
        Set<String> labelSet = Set.of(checkBoxText);
        return checkBoxes.stream()
                .filter(checkBox -> labelSet.contains(getLabel(checkBox)))
                .allMatch(this::isEnabled);
    }

    private boolean checkCheckboxEnabledStateByLocator(By[] checkBoxLocator) {
        return Arrays.stream(checkBoxLocator)
                .map(smartSelenium::waitAndFindElement)
                .allMatch(this::isEnabled);
    }

    private List<WebElement> filterCheckboxesByLabel(List<WebElement> checkBoxes, String[] labels) {
        Set<String> labelSet = Set.of(labels);
        return checkBoxes.stream().filter(checkBox -> labelSet.contains(getLabel(checkBox)))
                .collect(Collectors.toList());
    }

    private String applyStrategyAndClick(List<WebElement> checkBoxes, Strategy strategy) {
        if (checkBoxes.isEmpty()) {
            return "No action required";
        }
        if (strategy != null) {
            String selectedCheckBoxLabel;
            switch (strategy) {
                case RANDOM:
                    WebElement randomCheckBox = getRandomElementFromElements(checkBoxes);
                    clickIfEnabled(randomCheckBox);
                    selectedCheckBoxLabel = getLabel(randomCheckBox);
                    //info("Select or Deselect checkbox with text: " + selectedCheckBoxLabel);
                    return selectedCheckBoxLabel;
                case FIRST:
                    WebElement firstCheckBox = getFirstElementFromElements(checkBoxes);
                    clickIfEnabled(firstCheckBox);
                    selectedCheckBoxLabel = getLabel(firstCheckBox);
                    //info("Select or Deselect checkbox with text: " + selectedCheckBoxLabel);
                    return selectedCheckBoxLabel;
                case LAST:
                    WebElement lastCheckBox = getLastElementFromElements(checkBoxes);
                    clickIfEnabled(lastCheckBox);
                    selectedCheckBoxLabel = getLabel(lastCheckBox);
                    //info("Select or Deselect checkbox with text: " + selectedCheckBoxLabel);
                    return selectedCheckBoxLabel;
                case ALL:
                    String allSelected = checkBoxes.stream().map(this::getLabel).toList()
                            .toString();
                    checkBoxes.forEach(this::clickIfEnabled);
                    //info("Select or Deselect all checkboxes");
                    return allSelected;
            }
        } else {
            checkBoxes.forEach(this::clickIfEnabled);
        }
        return null;
    }

    private void clickIfEnabled(WebElement checkBox) {
        if (isEnabled(checkBox)) {
            String checkBoxClass = smartSelenium.smartGetAttribute(checkBox, "class");
            smartSelenium.smartClick(checkBox);
            smartSelenium.waitUntilAttributeValueIsChanged(checkBox, "class", checkBoxClass);
            checkBoxClass = smartSelenium.smartGetAttribute(checkBox, "class");
            smartSelenium.waitUntilAttributeValueIsChanged(checkBox, "class", checkBoxClass);
        }
    }

    private boolean isChecked(WebElement checkBox) {
        return smartSelenium.smartGetAttribute(checkBox, "class").contains(CHECKED_CLASS_INDICATOR);
    }

    private boolean isEnabled(WebElement checkBox) {
        return !smartSelenium.smartGetAttribute(checkBox, "class").contains(DISABLED_STATE);
    }

    private String getLabel(WebElement checkBox) {
        WebElement label = smartSelenium.smartFindElement(checkBox, CHECKBOX_LABEL_LOCATOR);
        return smartSelenium.smartGetText(label).trim();
    }
}
