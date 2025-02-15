package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

public interface Accordion {

    void expand(SmartWebElement container, String... accordionText);

    String expand(SmartWebElement container, Strategy strategy);

    void expand(String... accordionText);

    void expand(By... accordionLocator);

    void collapse(SmartWebElement container, String... accordionText);

    String collapse(SmartWebElement container, Strategy strategy);

    void collapse(String... accordionText);

    void collapse(By... accordionLocator);

    boolean areEnabled(SmartWebElement container, String... accordionText);

    boolean areEnabled(String... accordionText);

    boolean areEnabled(By... accordionLocator);

    List<String> getExpanded(SmartWebElement container);

    List<String> getCollapsed(SmartWebElement container);

    List<String> getAll(SmartWebElement container);

    String getTitle(By accordionLocator);

    String getText(By accordionLocator);
}
