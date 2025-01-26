package com.theairebellion.zeus.ui.components.factory;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.Input;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

public class ComponentFactory {

    public static final UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);


    public static Input getInputComponent(InputComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Input.class, type, uiConfig.projectPackage(), smartWebDriver);
    }


    public static <T> T getComponent(Class<T> interfaceType, ComponentType componentType, String projectPackage,
                                     SmartWebDriver smartWebDriver) {
        List<Class<? extends T>> implementationsOfInterface = ReflectionUtil.findImplementationsOfInterface(
                interfaceType, projectPackage);
        try {
            return implementationsOfInterface.stream()
                    .filter(aClass -> Objects.nonNull(aClass.getAnnotation(ImplementationOfType.class)) &&
                            aClass.getAnnotation(ImplementationOfType.class).value()
                                    .equals(componentType.getType().name()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "There is no implementation using ImplementationOfType for: " +
                                    componentType.getType().name()))
                    .getDeclaredConstructor(SmartWebDriver.class)
                    .newInstance(smartWebDriver);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
