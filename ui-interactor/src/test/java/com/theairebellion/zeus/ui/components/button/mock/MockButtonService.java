package com.theairebellion.zeus.ui.components.button.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockButtonService implements ButtonService {

    public ButtonComponentType lastComponentType;
    public SmartWebElement lastContainer;
    public String lastButtonText;
    public By lastLocator;

    public boolean returnEnabled;
    public boolean returnVisible;

    public void reset() {
        lastComponentType = null;
        lastContainer = null;
        lastButtonText = null;
        lastLocator = null;
        returnEnabled = true;
        returnVisible = true;
    }

    @Override
    public final <T extends ButtonComponentType> void click(T componentType, SmartWebElement container, String buttonText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastButtonText = buttonText;
    }

    @Override
    public final <T extends ButtonComponentType> void click(T componentType, SmartWebElement container) {
        lastComponentType = componentType;
        lastContainer = container;
    }

    @Override
    public final <T extends ButtonComponentType> void click(T componentType, String buttonText) {
        lastComponentType = componentType;
        lastButtonText = buttonText;
    }

    @Override
    public final <T extends ButtonComponentType> void click(T componentType, By buttonLocator) {
        lastComponentType = componentType;
        lastLocator = buttonLocator;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement container, String buttonText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastButtonText = buttonText;
        return returnEnabled;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement container) {
        lastComponentType = componentType;
        lastContainer = container;
        return returnEnabled;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isEnabled(T componentType, String buttonText) {
        lastComponentType = componentType;
        lastButtonText = buttonText;
        return returnEnabled;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isEnabled(T componentType, By buttonLocator) {
        lastComponentType = componentType;
        lastLocator = buttonLocator;
        return returnEnabled;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement container, String buttonText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastButtonText = buttonText;
        return returnVisible;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement container) {
        lastComponentType = componentType;
        lastContainer = container;
        return returnVisible;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isVisible(T componentType, String buttonText) {
        lastComponentType = componentType;
        lastButtonText = buttonText;
        return returnVisible;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isVisible(T componentType, By buttonLocator) {
        lastComponentType = componentType;
        lastLocator = buttonLocator;
        return returnVisible;
    }

    @Override
    public void tableInsertion(SmartWebElement cellElement, ComponentType componentType, String... values) {
    }
}