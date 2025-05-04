package com.theairebellion.zeus.ui.components.toggle.mock;

import com.theairebellion.zeus.ui.components.toggle.ToggleComponentType;
import com.theairebellion.zeus.ui.components.toggle.ToggleService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockToggleService implements ToggleService {

    public ToggleComponentType lastComponentTypeUsed;
    public ToggleComponentType explicitComponentType;
    public SmartWebElement lastContainer;
    public By lastLocator;
    public String lastText;
    public boolean returnBool; // Used for isEnabled/isActivated return

    public MockToggleService() {
        reset();
    }

    private void setLastType(ToggleComponentType type) {
        this.explicitComponentType = type;
        if (MockToggleComponentType.DUMMY_TOGGLE.equals(type)) {
            this.lastComponentTypeUsed = MockToggleComponentType.DUMMY_TOGGLE;
        } else {
            this.lastComponentTypeUsed = null;
        }
    }

    public void reset() {
        lastComponentTypeUsed = null;
        explicitComponentType = MockToggleComponentType.DUMMY_TOGGLE;
        lastContainer = null;
        lastLocator = null;
        lastText = null;
        returnBool = false; // Default return for boolean checks
    }

    @Override
    public void activate(ToggleComponentType componentType, SmartWebElement container, String toggleText) {
        setLastType(componentType);
        lastContainer = container;
        lastText = toggleText;
    }

    @Override
    public void activate(ToggleComponentType componentType, String toggleText) {
        setLastType(componentType);
        lastText = toggleText;
        lastContainer = null;
        lastLocator = null;
    }

    @Override
    public void activate(ToggleComponentType componentType, By toggleLocator) {
        setLastType(componentType);
        lastLocator = toggleLocator;
        lastContainer = null;
        lastText = null;
    }

    @Override
    public void deactivate(ToggleComponentType componentType, SmartWebElement container, String toggleText) {
        setLastType(componentType);
        lastContainer = container;
        lastText = toggleText;
    }

    @Override
    public void deactivate(ToggleComponentType componentType, String toggleText) {
        setLastType(componentType);
        lastText = toggleText;
        lastContainer = null;
        lastLocator = null;
    }

    @Override
    public void deactivate(ToggleComponentType componentType, By toggleLocator) {
        setLastType(componentType);
        lastLocator = toggleLocator;
        lastContainer = null;
        lastText = null;
    }

    @Override
    public boolean isEnabled(ToggleComponentType componentType, SmartWebElement container, String toggleText) {
        setLastType(componentType);
        lastContainer = container;
        lastText = toggleText;
        return returnBool;
    }

    @Override
    public boolean isEnabled(ToggleComponentType componentType, String toggleText) {
        setLastType(componentType);
        lastText = toggleText;
        lastContainer = null;
        lastLocator = null;
        return returnBool;
    }

    @Override
    public boolean isEnabled(ToggleComponentType componentType, By toggleLocator) {
        setLastType(componentType);
        lastLocator = toggleLocator;
        lastContainer = null;
        lastText = null;
        return returnBool;
    }

    @Override
    public boolean isActivated(ToggleComponentType componentType, SmartWebElement container, String toggleText) {
        setLastType(componentType);
        lastContainer = container;
        lastText = toggleText;
        return returnBool;
    }

    @Override
    public boolean isActivated(ToggleComponentType componentType, String toggleText) {
        setLastType(componentType);
        lastText = toggleText;
        lastContainer = null;
        lastLocator = null;
        return returnBool;
    }

    @Override
    public boolean isActivated(ToggleComponentType componentType, By toggleLocator) {
        setLastType(componentType);
        lastLocator = toggleLocator;
        lastContainer = null;
        lastText = null;
        return returnBool;
    }
}