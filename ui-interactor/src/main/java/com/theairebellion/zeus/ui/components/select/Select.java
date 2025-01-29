package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface Select {

    void selectItems(WebElement container, String... values);

    void selectItems(By containerLocator, String... values);

    List<String> selectItems(WebElement container, Strategy strategy);

    List<String> selectItems(By containerLocator, Strategy strategy);

    List<String> getAvailableItems(WebElement container);

    List<String> getAvailableItems(By containerLocator);

    List<String> getSelectedItems(WebElement container);

    List<String> getSelectedItems(By containerLocator);

    boolean isOptionVisible(WebElement container, String value);

    boolean isOptionVisible(By containerLocator, String value);

    boolean isOptionEnabled(WebElement container, String value);

    boolean isOptionEnabled(By containerLocator, String value);
}