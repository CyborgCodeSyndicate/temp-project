package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface Checkbox {

    void select(WebElement container, String... checkBoxText);

    String select(WebElement container, Strategy strategy);

    void select(String... checkBoxText);

    void select(By... checkBoxLocator);

    void deSelect(WebElement container, String... checkBoxText);

    String deSelect(WebElement container, Strategy strategy);

    void deSelect(String... checkBoxText);

    void deSelect(By... checkBoxLocator);

    boolean areSelected(WebElement container, String... checkBoxText);

    boolean areSelected(String... checkBoxText);

    boolean areSelected(By... checkBoxLocator);

    boolean areEnabled(WebElement container, String... checkBoxText);

    boolean areEnabled(String... checkBoxText);

    boolean areEnabled(By... checkBoxLocator);

    List<String> getSelected(WebElement container);

    List<String> getSelected(By containerLocator);

    List<String> getAll(WebElement WebElement);

    List<String> getAll(By containerLocator);
}
