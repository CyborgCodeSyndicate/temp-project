package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public interface Input {

    void insert(SmartWebElement container, String value);

    void insert(SmartWebElement container, String inputFieldLabel, String value);

    void insert(String inputFieldLabel, String value);

    void insert(By inputFieldContainerLocator, String value);

    void clear(SmartWebElement container);

    void clear(SmartWebElement container, String inputFieldLabel);

    void clear(String inputFieldLabel);

    void clear(By inputFieldContainerLocator);

    String getValue(SmartWebElement container);

    String getValue(SmartWebElement container, String inputFieldLabel);

    String getValue(String inputFieldLabel);

    String getValue(By inputFieldContainerLocator);

    boolean isEnabled(SmartWebElement container);

    boolean isEnabled(SmartWebElement container, String inputFieldLabel);

    boolean isEnabled(String inputFieldLabel);

    boolean isEnabled(By inputFieldContainerLocator);

    String getErrorMessage(SmartWebElement container);

    String getErrorMessage(SmartWebElement container, String inputFieldLabel);

    String getErrorMessage(String inputFieldLabel);

    String getErrorMessage(By inputFieldContainerLocator);

}
