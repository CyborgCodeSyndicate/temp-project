package com.example.project.ui.components.list;

import com.example.project.ui.types.ListFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.list.ItemList;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.ui.util.strategy.StrategyGenerator;
import org.openqa.selenium.By;

import java.util.*;
import java.util.stream.Collectors;

@ImplementationOfType(ListFieldTypes.MD_LIST)
public class ListMDImpl extends BaseComponent implements ItemList {

    private static final By LIST_ITEM_ELEMENT_SELECTOR = By.tagName("mat-list-option");
    private static final By ITEM_LABEL_LOCATOR = By.className("mat-list-text");
    private static final String SELECTED_STATE = "aria-selected";
    private static final String DISABLED_STATE = "mat-list-item-disabled";


    public ListMDImpl(SmartWebDriver driver) {
        super(driver);
    }


    @Override
    public void select(final SmartWebElement container, final String... itemText) {
        performActionOnListItems(container, itemText, true);
    }


    @Override
    public void select(final By containerLocator, final String... itemText) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        select(container, itemText);
    }


    @Override
    public String select(final SmartWebElement container, final Strategy strategy) {
        return performActionOnListItemsWithStrategy(container, strategy, true);
    }


    @Override
    public String select(final By containerLocator, final Strategy strategy) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return select(container, strategy);
    }


    @Override
    public void select(final String... itemText) {
        performActionOnListItems(null, itemText, true);
    }


    @Override
    public void select(final By... itemListLocator) {
        performActionOnListItemsByLocator(itemListLocator, true);
    }


    @Override
    public void deSelect(final SmartWebElement container, final String... itemText) {
        performActionOnListItems(container, itemText, false);
    }


    @Override
    public void deSelect(final By containerLocator, final String... itemText) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        deSelect(container, itemText);
    }


    @Override
    public String deSelect(final SmartWebElement container, final Strategy strategy) {
        return performActionOnListItemsWithStrategy(container, strategy, false);
    }


    @Override
    public String deSelect(final By containerLocator, final Strategy strategy) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return deSelect(container, strategy);
    }


    @Override
    public void deSelect(final String... itemText) {
        performActionOnListItems(null, itemText, false);
    }


    @Override
    public void deSelect(final By... itemListLocator) {
        performActionOnListItemsByLocator(itemListLocator, false);
    }


    @Override
    public boolean areSelected(final SmartWebElement container, final String... itemText) {
        return checkListItemState(container, itemText);
    }


    @Override
    public boolean areSelected(final By containerLocator, final String... itemText) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return areSelected(container, itemText);
    }


    @Override
    public boolean areSelected(final String... itemText) {
        return checkListItemState(null, itemText);
    }


    @Override
    public boolean areSelected(final By... itemListLocator) {
        return checkListItemStateByLocator(itemListLocator);
    }


    @Override
    public boolean areEnabled(final SmartWebElement container, final String... itemText) {
        return checkListItemsEnabledState(container, itemText);
    }


    @Override
    public boolean areEnabled(final By containerLocator, final String... itemText) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return areEnabled(container, itemText);
    }


    @Override
    public boolean areEnabled(final String... itemText) {
        return checkListItemsEnabledState(null, itemText);
    }


    @Override
    public boolean areEnabled(final By... itemLocator) {
        return checkListItemEnabledStateByLocator(itemLocator);
    }


    @Override
    public boolean areVisible(final SmartWebElement container, final String... itemText) {
        return checkListItemsVisibleState(container, itemText);
    }


    @Override
    public boolean areVisible(final By containerLocator, final String... itemText) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return areVisible(container, itemText);
    }


    @Override
    public boolean areVisible(final String... itemText) {
        return checkListItemsVisibleState(null, itemText);
    }


    @Override
    public boolean areVisible(final By... itemLocator) {
        return checkListItemsVisibleStateByLocator(itemLocator);
    }


    @Override
    public List<String> getSelected(final SmartWebElement container) {
        List<SmartWebElement> listItems = findListItems(container, true);
        return listItems.stream().map(this::getLabel).collect(Collectors.toList());
    }


    @Override
    public List<String> getSelected(final By containerLocator) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return getSelected(container);
    }


    @Override
    public List<String> getAll(final SmartWebElement container) {
        List<SmartWebElement> listItems = findListItems(container, null);
        return listItems.stream().map(this::getLabel).collect(Collectors.toList());
    }


    @Override
    public List<String> getAll(final By containerLocator) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return getAll(container);
    }


    private List<SmartWebElement> findListItems(SmartWebElement container, Boolean onlySelected) {
        List<SmartWebElement> listItems = container != null
                ? container.findSmartElements(LIST_ITEM_ELEMENT_SELECTOR)
                : driver.findSmartElements(LIST_ITEM_ELEMENT_SELECTOR);
        if (Objects.isNull(onlySelected)) {
            return listItems;
        }
        return onlySelected ? listItems.stream().filter(this::isSelected).toList() :
                listItems.stream().filter(listItem -> !isSelected(listItem)).toList();
    }


    private void performActionOnListItems(SmartWebElement container, String[] itemText, boolean select) {
        List<SmartWebElement> listItems = findListItems(container, !select);
        listItems = filterListItemsByLabel(listItems, itemText);
        listItems.forEach(this::clickIfEnabled);
    }


    private String performActionOnListItemsWithStrategy(SmartWebElement container, Strategy strategy, boolean select) {
        List<SmartWebElement> listItems = findListItems(container, !select);
        return applyStrategyAndClick(listItems, strategy);
    }


    private void performActionOnListItemsByLocator(By[] itemLocator, boolean select) {
        List<SmartWebElement> listItems = Arrays.stream(itemLocator)
                .map(driver::findSmartElement)
                .collect(Collectors.toList());
        listItems = listItems.stream().filter(listItem -> select != isSelected(listItem)).toList();
        listItems.forEach(this::clickIfEnabled);
    }


    private boolean checkListItemState(SmartWebElement container, String[] itemText) {
        List<SmartWebElement> listItems = findListItems(container, true);
        Set<String> labelSet = Set.of(itemText);
        List<SmartWebElement> matchingListItems = listItems.stream()
                .filter(listItem -> labelSet.contains(getLabel(listItem)))
                .toList();
        return matchingListItems.size() == itemText.length;
    }


    private boolean checkListItemStateByLocator(By[] itemLocator) {
        return Arrays.stream(itemLocator)
                .map(driver::findSmartElement)
                .allMatch(this::isSelected);
    }


    private boolean checkListItemsVisibleState(SmartWebElement container, String[] itemText) {
        List<SmartWebElement> listItems = findListItems(container, null);
        Set<String> labelSet = Set.of(itemText);
        List<String> itemListAllLabels = listItems.stream().map(this::getLabel).toList();
        return new HashSet<>(itemListAllLabels).containsAll(labelSet);
    }


    private boolean checkListItemsVisibleStateByLocator(By[] itemLocator) {
        List<SmartWebElement> listItems = findListItems(null, null);
        Set<SmartWebElement> labelSet = Arrays.stream(itemLocator)
                .map(driver::findSmartElement)
                .collect(Collectors.toSet());
        return new HashSet<>(listItems).containsAll(labelSet);
    }


    private boolean checkListItemsEnabledState(SmartWebElement container, String[] itemText) {
        List<SmartWebElement> listItems = findListItems(container, null);
        Set<String> labelSet = Set.of(itemText);
        return listItems.stream()
                .filter(listItem -> labelSet.contains(getLabel(listItem)))
                .allMatch(this::isEnabled);
    }


    private boolean checkListItemEnabledStateByLocator(By[] itemLocator) {
        return Arrays.stream(itemLocator)
                .map(driver::findSmartElement)
                .allMatch(this::isEnabled);
    }


    private List<SmartWebElement> filterListItemsByLabel(List<SmartWebElement> listItems, String[] labels) {
        Set<String> labelSet = Set.of(labels);
        return listItems.stream()
                .filter(listItem -> labelSet.contains(getLabel(listItem)))
                .collect(Collectors.toList());
    }


    private String applyStrategyAndClick(List<SmartWebElement> listItems, Strategy strategy) {
        if (listItems.isEmpty()) {
            return "No action required";
        }
        if (strategy != null) {
            String selectedListItemLabel;
            switch (strategy) {
                case RANDOM -> {
                    SmartWebElement randomListItem = StrategyGenerator.getRandomElementFromElements(listItems);
                    clickIfEnabled(randomListItem);
                    selectedListItemLabel = getLabel(randomListItem);
                    return selectedListItemLabel;
                }
                case FIRST -> {
                    SmartWebElement firstListItem = StrategyGenerator.getFirstElementFromElements(listItems);
                    clickIfEnabled(firstListItem);
                    selectedListItemLabel = getLabel(firstListItem);
                    return selectedListItemLabel;
                }
                case LAST -> {
                    SmartWebElement lastListItems = StrategyGenerator.getLastElementFromElements(listItems);
                    clickIfEnabled(lastListItems);
                    selectedListItemLabel = getLabel(lastListItems);
                    return selectedListItemLabel;
                }
                case ALL -> {
                    String allSelected = listItems.stream()
                            .map(this::getLabel)
                            .toList()
                            .toString();
                    listItems.forEach(this::clickIfEnabled);
                    return allSelected;
                }
            }
        } else {
            listItems.forEach(this::clickIfEnabled);
        }
        return null;
    }


    private void clickIfEnabled(SmartWebElement listItem) {
        if (isEnabled(listItem)) {
            String listItemClass = listItem.getAttribute("class");
            listItem.click();
            listItem.waitUntilAttributeValueIsChanged("class", listItemClass);
            listItemClass = listItem.getAttribute("class");
            listItem.waitUntilAttributeValueIsChanged("class", listItemClass);
        }
    }


    private String getLabel(SmartWebElement listItem) {
        SmartWebElement label = listItem.findSmartElement(ITEM_LABEL_LOCATOR);
        return label.getText().trim();
    }


    private boolean isSelected(SmartWebElement listItem) {
        return Objects.requireNonNull(listItem.getAttribute(SELECTED_STATE)).equalsIgnoreCase("true");
    }


    private boolean isEnabled(SmartWebElement listItem) {
        return !Objects.requireNonNull(listItem.getAttribute("class")).contains(DISABLED_STATE);
    }
}