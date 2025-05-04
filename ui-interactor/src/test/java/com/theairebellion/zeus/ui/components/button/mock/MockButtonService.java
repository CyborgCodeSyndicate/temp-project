package com.theairebellion.zeus.ui.components.button.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockButtonService implements ButtonService {

    public ButtonComponentType lastComponentTypeUsed;
    public ButtonComponentType explicitComponentType;
    public SmartWebElement lastContainer;
    public String lastButtonText;
    public By lastLocator;
    public SmartWebElement lastCellElement;
    public String[] lastValues;

    public boolean returnEnabled;
    public boolean returnVisible;

    public MockButtonService() {
        reset();
    }

    private void setLastType(ButtonComponentType type) {
        this.explicitComponentType = type;
        if (MockButtonComponentType.DUMMY_BUTTON.equals(type)) {
            this.lastComponentTypeUsed = MockButtonComponentType.DUMMY_BUTTON;
        } else {
            this.lastComponentTypeUsed = null;
        }
    }


    public void reset() {
        lastComponentTypeUsed = null;
        explicitComponentType = MockButtonComponentType.DUMMY_BUTTON;
        lastContainer = null;
        lastButtonText = null;
        lastLocator = null;
        lastCellElement = null;
        lastValues = null;
        returnEnabled = true;
        returnVisible = true;
    }

    @Override
    public final <T extends ButtonComponentType> void click(T componentType, SmartWebElement container, String buttonText) {
        setLastType(componentType);
        lastContainer = container;
        lastButtonText = buttonText;
    }

    @Override
    public final <T extends ButtonComponentType> void click(T componentType, SmartWebElement container) {
        setLastType(componentType);
        lastContainer = container;
        lastButtonText = null;
    }

    @Override
    public final <T extends ButtonComponentType> void click(T componentType, String buttonText) {
        setLastType(componentType);
        lastButtonText = buttonText;
        lastContainer = null;
    }

    @Override
    public final <T extends ButtonComponentType> void click(T componentType, By buttonLocator) {
        setLastType(componentType);
        lastLocator = buttonLocator;
        lastContainer = null;
        lastButtonText = null;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement container, String buttonText) {
        setLastType(componentType);
        lastContainer = container;
        lastButtonText = buttonText;
        return returnEnabled;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement container) {
        setLastType(componentType);
        lastContainer = container;
        lastButtonText = null;
        return returnEnabled;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isEnabled(T componentType, String buttonText) {
        setLastType(componentType);
        lastButtonText = buttonText;
        lastContainer = null;
        return returnEnabled;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isEnabled(T componentType, By buttonLocator) {
        setLastType(componentType);
        lastLocator = buttonLocator;
        lastContainer = null;
        lastButtonText = null;
        return returnEnabled;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement container, String buttonText) {
        setLastType(componentType);
        lastContainer = container;
        lastButtonText = buttonText;
        return returnVisible;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement container) {
        setLastType(componentType);
        lastContainer = container;
        lastButtonText = null;
        return returnVisible;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isVisible(T componentType, String buttonText) {
        setLastType(componentType);
        lastButtonText = buttonText;
        lastContainer = null;
        return returnVisible;
    }

    @Override
    public final <T extends ButtonComponentType> boolean isVisible(T componentType, By buttonLocator) {
        setLastType(componentType);
        lastLocator = buttonLocator;
        lastContainer = null;
        lastButtonText = null;
        return returnVisible;
    }

    @Override
    public void tableInsertion(SmartWebElement cellElement, ComponentType componentType, String... values) {
        lastCellElement = cellElement;
        // Attempt cast for testing purposes, real impl handles type safety
        if (componentType instanceof ButtonComponentType) {
            setLastType((ButtonComponentType) componentType);
        }
        lastValues = values;
    }
}