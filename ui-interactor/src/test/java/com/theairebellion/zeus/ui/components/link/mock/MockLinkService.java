package com.theairebellion.zeus.ui.components.link.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonService;
import com.theairebellion.zeus.ui.components.link.LinkComponentType;
import com.theairebellion.zeus.ui.components.link.LinkService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockLinkService extends MockButtonService implements LinkService {

    public LinkComponentType lastLinkType;

    public void resetLinkSpecificFields() {
        lastLinkType = null;
    }

    @Override
    public void doubleClick(LinkComponentType componentType, SmartWebElement container, String linkText) {
        this.explicitComponentType = componentType;
        setLastType(componentType);
        lastLinkType = componentType;
        lastContainer = container;
        lastButtonText = linkText;
        lastLocator = null;
    }

    @Override
    public void doubleClick(LinkComponentType componentType, SmartWebElement container) {
        this.explicitComponentType = componentType;
        setLastType(componentType);
        lastLinkType = componentType;
        lastContainer = container;
        lastButtonText = null;
        lastLocator = null;
    }

    @Override
    public void doubleClick(LinkComponentType componentType, String linkText) {
        this.explicitComponentType = componentType;
        setLastType(componentType);
        lastLinkType = componentType;
        lastButtonText = linkText;
        lastContainer = null;
        lastLocator = null;
    }

    @Override
    public void doubleClick(LinkComponentType componentType, By linkLocator) {
        this.explicitComponentType = componentType;
        setLastType(componentType);
        lastLinkType = componentType;
        lastLocator = linkLocator;
        lastContainer = null;
        lastButtonText = null;
    }

    private void setLastType(LinkComponentType type) {
        super.explicitComponentType = type;
        if (MockLinkComponentType.DUMMY_LINK.equals(type)) {
            super.lastComponentTypeUsed = MockLinkComponentType.DUMMY_LINK;
        } else {
            super.lastComponentTypeUsed = null;
        }
    }

    @Override
    public void reset() {
        super.reset();
        resetLinkSpecificFields();
        super.explicitComponentType = MockLinkComponentType.DUMMY_LINK;
    }

    @Override
    public void tableInsertion(SmartWebElement cellElement, ComponentType componentType, String... values) {
        super.tableInsertion(cellElement, componentType, values);
        if (componentType instanceof LinkComponentType) {
            setLastType((LinkComponentType)componentType);
            this.lastLinkType = (LinkComponentType) componentType;
        }
    }
}