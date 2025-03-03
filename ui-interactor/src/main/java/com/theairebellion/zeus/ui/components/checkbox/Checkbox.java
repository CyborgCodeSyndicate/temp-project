package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

public interface Checkbox {

    void select(SmartWebElement container, String... checkBoxText);

    String select(SmartWebElement container, Strategy strategy);

    void select(String... checkBoxText);

    void select(By... checkBoxLocator);

    void deSelect(SmartWebElement container, String... checkBoxText);

    String deSelect(SmartWebElement container, Strategy strategy);

    void deSelect(String... checkBoxText);

    void deSelect(By... checkBoxLocator);

    boolean areSelected(SmartWebElement container, String... checkBoxText);

    boolean areSelected(String... checkBoxText);

    boolean areSelected(By... checkBoxLocator);

    boolean areEnabled(SmartWebElement container, String... checkBoxText);

    boolean areEnabled(String... checkBoxText);

    boolean areEnabled(By... checkBoxLocator);

    List<String> getSelected(SmartWebElement container);

    List<String> getSelected(By containerLocator);

    List<String> getAll(SmartWebElement SmartWebElement);

    List<String> getAll(By containerLocator);
}
