package com.example.project.ui.components.list;

import com.example.project.ui.types.ListFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.list.ItemList;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.ui.util.strategy.StrategyGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.stream.Collectors;

@ImplementationOfType(ListFieldTypes.BOOTSTRAP_LIST)
public class ListBootstrapImpl extends BaseComponent implements ItemList {

    private static final By LIST_ITEM_ELEMENT_SELECTOR = By.tagName("li");
    private static final By ITEM_LABEL_LOCATOR = By.tagName("a");
    private static final String SELECTED_STATE = "selected";
    private static final String DISABLED_STATE = "disabled";

    public ListBootstrapImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }

    @Override
    public void select(WebElement container, String... itemText) {
        performActionOnListItems(container, itemText, true);
    }

    @Override
    public void select(By containerLocator, String... itemText) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        select(container, itemText);
    }

    @Override
    public String select(WebElement container, Strategy strategy) {
        return performActionOnListItemsWithStrategy(container, strategy, true);
    }

    @Override
    public String select(By containerLocator, Strategy strategy) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return select(container, strategy);
    }

    @Override
    public void select(String... itemText) {
        performActionOnListItems(null, itemText, true);
    }

    @Override
    public void select(By... itemListLocator) {
        performActionOnListItemsByLocator(itemListLocator, true);
    }

    @Override
    public void deSelect(WebElement container, String... itemText) {
        performActionOnListItems(container, itemText, false);
    }

    @Override
    public void deSelect(By containerLocator, String... itemText) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        deSelect(container, itemText);
    }

    @Override
    public String deSelect(WebElement container, Strategy strategy) {
        return performActionOnListItemsWithStrategy(container, strategy, false);
    }

    @Override
    public String deSelect(By containerLocator, Strategy strategy) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return deSelect(container, strategy);
    }

    @Override
    public void deSelect(String... itemText) {
        performActionOnListItems(null, itemText, false);
    }

    @Override
    public void deSelect(By... itemListLocator) {
        performActionOnListItemsByLocator(itemListLocator, false);
    }

    @Override
    public boolean areSelected(WebElement container, String... itemText) {
        return checkListItemState(container, itemText);
    }

    @Override
    public boolean areSelected(By containerLocator, String... itemText) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return areSelected(container, itemText);
    }

    @Override
    public boolean areSelected(String... itemText) {
        return checkListItemState(null, itemText);
    }

    @Override
    public boolean areSelected(By... itemListLocator) {
        return checkListItemStateByLocator(itemListLocator);
    }

    @Override
    public boolean areEnabled(WebElement container, String... itemText) {
        return checkListItemsEnabledState(container, itemText);
    }

    @Override
    public boolean areEnabled(By containerLocator, String... itemText) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return areEnabled(container, itemText);
    }

    @Override
    public boolean areEnabled(String... itemText) {
        return checkListItemsEnabledState(null, itemText);
    }

    @Override
    public boolean areEnabled(By... itemLocator) {
        return checkListItemEnabledStateByLocator(itemLocator);
    }

    @Override
    public boolean areVisible(WebElement container, String... itemText) {
        return checkListItemsVisibleState(container, itemText);
    }

    @Override
    public boolean areVisible(By containerLocator, String... itemText) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return areVisible(container, itemText);
    }

    @Override
    public boolean areVisible(String... itemText) {
        return checkListItemsVisibleState(null, itemText);
    }

    @Override
    public boolean areVisible(By... itemLocator) {
        return checkListItemsVisibleStateByLocator(itemLocator);
    }

    @Override
    public List<String> getSelected(WebElement container) {
        List<WebElement> listItems = findListItems(container, true);
        return listItems.stream().map(this::getLabel).collect(Collectors.toList());
    }

    @Override
    public List<String> getSelected(By containerLocator) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return getSelected(container);
    }

    @Override
    public List<String> getAll(WebElement container) {
        List<WebElement> listItems = findListItems(container, null);
        return listItems.stream().map(this::getLabel).collect(Collectors.toList());
    }

    @Override
    public List<String> getAll(By containerLocator) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return getAll(container);
    }


    private List<WebElement> findListItems(WebElement container, Boolean onlySelected) {
        List<WebElement> listItems = container != null
                ? smartSelenium.waitAndFindElements(container, LIST_ITEM_ELEMENT_SELECTOR)
                : smartSelenium.smartFindElements(LIST_ITEM_ELEMENT_SELECTOR);
        if (Objects.isNull(onlySelected)) {
            return listItems;
        }
        return onlySelected ? listItems.stream().filter(this::isSelected).toList() :
                listItems.stream().filter(listItem -> !isSelected(listItem)).toList();
    }

    private void performActionOnListItems(WebElement container, String[] itemText, boolean select) {
        List<WebElement> listItems = findListItems(container, !select);
        listItems = filterListItemsByLabel(listItems, itemText);
        listItems.forEach(this::clickIfEnabled);
    }

    private String performActionOnListItemsWithStrategy(WebElement container, Strategy strategy, boolean select) {
        List<WebElement> listItems = findListItems(container, !select);
        return applyStrategyAndClick(listItems, strategy);
    }

    private void performActionOnListItemsByLocator(By[] itemLocator, boolean select) {
        List<WebElement> listItems = Arrays.stream(itemLocator)
                .map(smartSelenium::waitAndFindElement)
                .collect(Collectors.toList());
        listItems = listItems.stream().filter(listItem -> select != isSelected(listItem)).toList();
        listItems.forEach(this::clickIfEnabled);
    }

    private boolean checkListItemState(WebElement container, String[] itemText) {
        List<WebElement> listItems = findListItems(container, true);
        Set<String> labelSet = Set.of(itemText);
        List<WebElement> matchingListItems = listItems.stream()
                .filter(listItem -> labelSet.contains(getLabel(listItem)))
                .toList();
        return matchingListItems.size() == itemText.length;
    }

    private boolean checkListItemStateByLocator(By[] itemLocator) {
        return Arrays.stream(itemLocator)
                .map(smartSelenium::waitAndFindElement)
                .allMatch(this::isSelected);
    }

    private boolean checkListItemsVisibleState(WebElement container, String[] itemText) {
        List<WebElement> listItems = findListItems(container, null);
        Set<String> labelSet = Set.of(itemText);
        List<String> itemListAllLabels = listItems.stream().map(this::getLabel).toList();
        return new HashSet<>(itemListAllLabels).containsAll(labelSet);
    }

    private boolean checkListItemsVisibleStateByLocator(By[] itemLocator) {
        List<WebElement> listItems = findListItems(null, null);
        Set<WebElement> labelSet = Arrays.stream(itemLocator)
                .map(smartSelenium::waitAndFindElement)
                .collect(Collectors.toSet());
        return new HashSet<>(listItems).containsAll(labelSet);
    }

    private boolean checkListItemsEnabledState(WebElement container, String[] itemText) {
        List<WebElement> listItems = findListItems(container, null);
        Set<String> labelSet = Set.of(itemText);
        return listItems.stream()
                .filter(listItem -> labelSet.contains(getLabel(listItem)))
                .allMatch(this::isEnabled);
    }

    private boolean checkListItemEnabledStateByLocator(By[] itemLocator) {
        return Arrays.stream(itemLocator)
                .map(smartSelenium::waitAndFindElement)
                .allMatch(this::isEnabled);
    }

    private List<WebElement> filterListItemsByLabel(List<WebElement> listItems, String[] labels) {
        Set<String> labelSet = Set.of(labels);
        return listItems.stream()
                .filter(listItem -> labelSet.contains(getLabel(listItem)))
                .collect(Collectors.toList());
    }

    private String applyStrategyAndClick(List<WebElement> listItems, Strategy strategy) {
        if (listItems.isEmpty()) {
            return "No action required";
        }
        if (strategy != null) {
            String selectedListItemLabel;
            switch (strategy) {
                case RANDOM -> {
                    WebElement randomListItem = StrategyGenerator.getRandomElementFromElements(listItems);
                    clickIfEnabled(randomListItem);
                    selectedListItemLabel = getLabel(randomListItem);
                    return selectedListItemLabel;
                }
                case FIRST -> {
                    WebElement firstListItem = StrategyGenerator.getFirstElementFromElements(listItems);
                    clickIfEnabled(firstListItem);
                    selectedListItemLabel = getLabel(firstListItem);
                    return selectedListItemLabel;
                }
                case LAST -> {
                    WebElement lastListItems = StrategyGenerator.getLastElementFromElements(listItems);
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

    private void clickIfEnabled(WebElement listItem) {
        if (isEnabled(listItem)) {
            smartSelenium.smartClick(listItem);
        }
    }

    private String getLabel(WebElement listItem) {
        WebElement label = smartSelenium.smartFindElement(listItem, ITEM_LABEL_LOCATOR);
        return smartSelenium.smartGetText(label).trim();
    }

    private boolean isSelected(WebElement listItem) {
        return smartSelenium.smartGetAttribute(listItem, "class").contains(SELECTED_STATE);
    }

    private boolean isEnabled(WebElement listItem) {
        return !smartSelenium.smartGetAttribute(listItem, "class").contains(DISABLED_STATE);
    }
}