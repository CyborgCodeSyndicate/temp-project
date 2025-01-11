package com.theairebellion.zeus.ui.components.input;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Input {

    void insert(WebElement container, String value);

    void insert(WebElement container, String inputFieldLabel, String value);

    void insert(String inputFieldLabel, String value);

    void insert(By inputFieldContainerLocator, String value);

    void clear(WebElement container);

    void clear(WebElement container, String inputFieldLabel);

    void clear(String inputFieldLabel);

    void clear(By inputFieldContainerLocator);

    String getValue(WebElement container);

    String getValue(WebElement container, String inputFieldLabel);

    String getValue(String inputFieldLabel);

    String getValue(By inputFieldContainerLocator);

    boolean isEnabled(WebElement container);

    boolean isEnabled(WebElement container, String inputFieldLabel);

    boolean isEnabled(String inputFieldLabel);

    boolean isEnabled(By inputFieldContainerLocator);

    String getErrorMessage(WebElement container);

    String getErrorMessage(WebElement container, String inputFieldLabel);

    String getErrorMessage(String inputFieldLabel);

    String getErrorMessage(By inputFieldContainerLocator);


}
