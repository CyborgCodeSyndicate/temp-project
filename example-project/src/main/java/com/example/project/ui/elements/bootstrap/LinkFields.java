package com.example.project.ui.elements.bootstrap;

import com.example.project.ui.types.LinkFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.link.LinkComponentType;
import com.theairebellion.zeus.ui.selenium.UIElement;
import org.openqa.selenium.By;

public enum LinkFields implements UIElement {

    CHECKING_ACCOUNT_ACTIVITY(By.id("account_activity_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
    TRANSFER_FUNDS(By.id("transfer_funds_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE),
    MY_MONEY_MAP(By.id("money_map_link"), LinkFieldTypes.BOOTSTRAP_LINK_TYPE);

    private final By locator;
    private final LinkComponentType componentType;


    LinkFields(final By locator, final LinkComponentType componentType) {
        this.locator = locator;
        this.componentType = componentType;
    }


    @Override
    public By locator() {
        return locator;
    }


    @Override
    public <T extends ComponentType> T componentType() {
        return (T) componentType;
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }

}
