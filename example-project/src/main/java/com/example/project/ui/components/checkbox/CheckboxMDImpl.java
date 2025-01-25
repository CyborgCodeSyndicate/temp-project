package com.example.project.ui.components.checkbox;

import com.example.project.ui.types.CheckboxFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.checkbox.Checkbox;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;


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
        return checkCheckboxState(container, checkBoxText, true);
    }

    @Override
    public boolean areSelected(String... checkBoxText) {
        return checkCheckboxState(null, checkBoxText, true);
    }

    @Override
    public boolean areSelected(By... checkBoxLocator) {
        return checkCheckboxStateByLocator(checkBoxLocator, true);
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
    public List<String> getAll(WebElement container) {
        List<WebElement> checkBoxes = findCheckboxes(container, null);
        return checkBoxes.stream().map(this::getLabel).collect(Collectors.toList());
    }

    private List<WebElement> findCheckboxes(WebElement container, Boolean onlySelected) {
        return List.of(); //todo
    }

    private void performActionOnCheckboxes(WebElement container, String[] checkBoxText, boolean select) {
    } //todo

    private String performActionOnCheckboxesWithStrategy(WebElement container, Strategy strategy, boolean select) {
        return ""; //todo
    }

    private void performActionOnCheckboxesByLocator(By[] checkBoxLocator, boolean isSelected) {
    } //todo

    private boolean checkCheckboxState(WebElement container, String[] checkBoxText, boolean isSelected) {
        return true; //todo
    }

    private boolean checkCheckboxStateByLocator(By[] checkBoxLocator, boolean isSelected) {
        return true; //todo
    }

    private boolean checkCheckboxEnabledState(WebElement container, String[] checkBoxText) {
        return true; //todo
    }

    private boolean checkCheckboxEnabledStateByLocator(By[] checkBoxLocator) {
        return true; //todo
    }

    private String getLabel(WebElement checkBox) {
        return ""; //todo
    }
}
