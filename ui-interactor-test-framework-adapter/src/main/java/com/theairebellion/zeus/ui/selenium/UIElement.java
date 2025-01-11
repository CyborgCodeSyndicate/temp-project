package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import org.openqa.selenium.By;

public interface UIElement {

    By locator();

    <T extends ComponentType> T componentType();

    Enum<?> enumImpl();

}
