package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface Radio {

    void select(WebElement container, String radioButtonText);

    String select(WebElement container, Strategy strategy);

    void select(String radioButtonText);

    void select(By radioButtonLocator);

    boolean isEnabled(WebElement container, String radioButtonText);

    boolean isEnabled(String radioButtonText);

    boolean isEnabled(By radioButtonLocator);

    boolean isSelected(WebElement container, String radioButtonText);

    boolean isSelected(String radioButtonText);

    boolean isSelected(By radioButtonLocator);

    boolean isVisible(WebElement container, String radioButtonText);

    boolean isVisible(String radioButtonText);

    boolean isVisible(By radioButtonLocator);

    String getSelected(WebElement container);

    String getSelected(By containerLocator);

    List<String> getAll(WebElement container);

    List<String> getAll(By containerLocator);
}