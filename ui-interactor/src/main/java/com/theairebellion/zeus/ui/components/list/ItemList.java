package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface ItemList {

    void select(WebElement container, String... itemText);

    void select(By containerLocator, String... itemText);

    String select(WebElement container, Strategy strategy);

    String select(By containerLocator, Strategy strategy);

    void select(String... itemText);

    void select(By... itemListLocator);

    void deSelect(WebElement container, String... itemText);

    void deSelect(By containerLocator, String... itemText);

    String deSelect(WebElement container, Strategy strategy);

    String deSelect(By containerLocator, Strategy strategy);

    void deSelect(String... itemText);

    void deSelect(By... itemListLocator);

    boolean areSelected(WebElement container, String... itemText);

    boolean areSelected(By containerLocator, String... itemText);

    boolean areSelected(String... itemText);

    boolean areSelected(By... itemListLocator);

    boolean areEnabled(WebElement container, String... itemText);

    boolean areEnabled(By containerLocator, String... itemText);

    boolean areEnabled(String... itemText);

    boolean areEnabled(By... itemLocator);

    boolean arePresent(WebElement container, String... itemText);

    boolean arePresent(By containerLocator, String... itemText);

    boolean arePresent(String... itemText);

    boolean arePresent(By... itemLocator);

    List<String> getSelected(WebElement container);

    List<String> getSelected(By containerLocator);

    List<String> getAll(WebElement container);

    List<String> getAll(By containerLocator);
}