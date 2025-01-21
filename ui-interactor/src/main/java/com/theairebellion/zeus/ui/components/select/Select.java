package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface Select {

    void selectItems(DdlMode mode, WebElement container, String... values);

    void selectItems(DdlMode mode, By containerLocator, String... values);

    List<String> selectItems(DdlMode mode, WebElement container, Strategy strategy);

    List<String> selectItems(DdlMode mode, By containerLocator, Strategy strategy);

    List<String> getAvailableItems(WebElement container);

    List<String> getAvailableItems(By containerLocator);

    List<String> getAvailableItems(WebElement container, String search);

    List<String> getAvailableItems(By containerLocator, String search);

    List<String> getSelectedItems(WebElement container);

    List<String> getSelectedItems(By containerLocator);

    boolean isOptionPresent(WebElement container, String value);

    boolean isOptionPresent(By containerLocator, String value);

    boolean isOptionEnabled(WebElement container, String value);

    boolean isOptionEnabled(By containerLocator, String value);
}