package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

public interface Radio {

    void select(SmartWebElement container, String radioButtonText);

    String select(SmartWebElement container, Strategy strategy);

    void select(String radioButtonText);

    void select(By radioButtonLocator);

    boolean isEnabled(SmartWebElement container, String radioButtonText);

    boolean isEnabled(String radioButtonText);

    boolean isEnabled(By radioButtonLocator);

    boolean isSelected(SmartWebElement container, String radioButtonText);

    boolean isSelected(String radioButtonText);

    boolean isSelected(By radioButtonLocator);

    boolean isVisible(SmartWebElement container, String radioButtonText);

    boolean isVisible(String radioButtonText);

    boolean isVisible(By radioButtonLocator);

    String getSelected(SmartWebElement container);

    String getSelected(By containerLocator);

    List<String> getAll(SmartWebElement container);

    List<String> getAll(By containerLocator);
}