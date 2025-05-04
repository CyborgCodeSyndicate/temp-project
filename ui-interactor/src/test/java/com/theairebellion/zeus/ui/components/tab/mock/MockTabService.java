package com.theairebellion.zeus.ui.components.tab.mock;

import com.theairebellion.zeus.ui.components.button.mock.MockButtonService;
import com.theairebellion.zeus.ui.components.tab.TabComponentType;
import com.theairebellion.zeus.ui.components.tab.TabService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockTabService extends MockButtonService implements TabService {

    public boolean returnIsSelected = false;

    public MockTabService() {
        super();
        reset();
    }

    private void setLastTabType(TabComponentType type) {
        super.explicitComponentType = type;
        if (MockTabComponentType.DUMMY_TAB.equals(type)) {
            super.lastComponentTypeUsed = type;
        } else {
            super.lastComponentTypeUsed = null;
        }
        super.lastButtonText = null;
        super.lastLocator = null;
    }

    @Override
    public void reset() {
        super.reset();
        this.returnIsSelected = false;
    }

    @Override
    public boolean isSelected(TabComponentType componentType, SmartWebElement container, String tabText) {
        setLastTabType(componentType);
        super.lastContainer = container;
        super.lastButtonText = tabText;
        super.lastLocator = null;
        return returnIsSelected;
    }

    @Override
    public boolean isSelected(TabComponentType componentType, SmartWebElement container) {
        setLastTabType(componentType);
        super.lastContainer = container;
        super.lastButtonText = null;
        super.lastLocator = null;
        return returnIsSelected;
    }

    @Override
    public boolean isSelected(TabComponentType componentType, String tabText) {
        setLastTabType(componentType);
        super.lastButtonText = tabText;
        super.lastContainer = null;
        super.lastLocator = null;
        return returnIsSelected;
    }

    @Override
    public boolean isSelected(TabComponentType componentType, By tabLocator) {
        setLastTabType(componentType);
        super.lastLocator = tabLocator;
        super.lastContainer = null;
        super.lastButtonText = null;
        return returnIsSelected;
    }

}