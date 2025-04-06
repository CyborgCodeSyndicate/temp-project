package com.theairebellion.zeus.ui.components.link.mock;

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
        lastLinkType = componentType;
        lastContainer = container;
        lastButtonText = linkText;
    }

    @Override
    public void doubleClick(LinkComponentType componentType, SmartWebElement container) {
        lastLinkType = componentType;
        lastContainer = container;
    }

    @Override
    public void doubleClick(LinkComponentType componentType, String linkText) {
        lastLinkType = componentType;
        lastButtonText = linkText;
    }

    @Override
    public void doubleClick(LinkComponentType componentType, By linkLocator) {
        lastLinkType = componentType;
        lastLocator = linkLocator;
    }

    @Override
    public void reset() {
        super.reset();
        resetLinkSpecificFields();
    }
}