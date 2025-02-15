package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

public interface Select {

    void selectOptions(SmartWebElement container, String... values);

    void selectOptions(By containerLocator, String... values);

    List<String> selectOptions(SmartWebElement container, Strategy strategy);

    List<String> selectOptions(By containerLocator, Strategy strategy);

    List<String> getAvailableOptions(SmartWebElement container);

    List<String> getAvailableOptions(By containerLocator);

    List<String> getSelectedOptions(SmartWebElement container);

    List<String> getSelectedOptions(By containerLocator);

    boolean isOptionVisible(SmartWebElement container, String value);

    boolean isOptionVisible(By containerLocator, String value);

    boolean isOptionEnabled(SmartWebElement container, String value);

    boolean isOptionEnabled(By containerLocator, String value);
}