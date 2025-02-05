package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

public interface ItemList {

    void select(SmartWebElement container, String... itemText);

    void select(By containerLocator, String... itemText);

    String select(SmartWebElement container, Strategy strategy);

    String select(By containerLocator, Strategy strategy);

    void select(String... itemText);

    void select(By... itemListLocator);

    void deSelect(SmartWebElement container, String... itemText);

    void deSelect(By containerLocator, String... itemText);

    String deSelect(SmartWebElement container, Strategy strategy);

    String deSelect(By containerLocator, Strategy strategy);

    void deSelect(String... itemText);

    void deSelect(By... itemListLocator);

    boolean areSelected(SmartWebElement container, String... itemText);

    boolean areSelected(By containerLocator, String... itemText);

    boolean areSelected(String... itemText);

    boolean areSelected(By... itemListLocator);

    boolean areEnabled(SmartWebElement container, String... itemText);

    boolean areEnabled(By containerLocator, String... itemText);

    boolean areEnabled(String... itemText);

    boolean areEnabled(By... itemLocator);

    boolean areVisible(SmartWebElement container, String... itemText);

    boolean areVisible(By containerLocator, String... itemText);

    boolean areVisible(String... itemText);

    boolean areVisible(By... itemLocator);

    List<String> getSelected(SmartWebElement container);

    List<String> getSelected(By containerLocator);

    List<String> getAll(SmartWebElement container);

    List<String> getAll(By containerLocator);
}