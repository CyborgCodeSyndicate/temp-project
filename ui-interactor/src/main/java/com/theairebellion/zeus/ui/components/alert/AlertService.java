package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface AlertService {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    AlertComponentType DEFAULT_TYPE = getDefaultType();

    default String getValue(WebElement container) {
        return getValue(DEFAULT_TYPE, container);
    }

    String getValue(AlertComponentType componentType, WebElement container);

    default String getValue(By containerLocator) {
        return getValue(DEFAULT_TYPE, containerLocator);
    }

    String getValue(AlertComponentType componentType, By containerLocator);

    default boolean isVisible(WebElement container) {
        return isVisible(DEFAULT_TYPE, container);
    }

    boolean isVisible(AlertComponentType componentType, WebElement container);

    default boolean isVisible(By containerLocator) {
        return isVisible(DEFAULT_TYPE, containerLocator);
    }

    boolean isVisible(AlertComponentType componentType, By containerLocator);

    private static AlertComponentType getDefaultType() {
        return ReflectionUtil.findEnumImplementationsOfInterface(AlertComponentType.class,
                uiConfig.alertDefaultType(),
                uiConfig.projectPackage());
    }
}
