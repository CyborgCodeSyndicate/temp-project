package com.theairebellion.zeus.ui.components.factory;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.Input;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ComponentFactory {

    public static final UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);


    public static Input getInputComponent(InputComponentType type, SmartSelenium smartSelenium) {
        return getComponent(Input.class, type, uiConfig.projectPackage(), smartSelenium);
    }


    public static <T> T getComponent(Class<T> interfaceType, ComponentType componentType, String projectPackage,
                                     SmartSelenium smartSelenium) {
        List<Class<? extends T>> implementationsOfInterface = ReflectionUtil.findImplementationsOfInterface(
                interfaceType, projectPackage);
        LogUI.debug("Found {} classes implementing {} in package {}.",
                implementationsOfInterface.size(),
                interfaceType.getSimpleName(),
                projectPackage);
        try {
            Class<? extends T> matchedClass = implementationsOfInterface.stream()
                    .filter(aClass -> {
                        ImplementationOfType ann = aClass.getAnnotation(ImplementationOfType.class);
                        return ann != null
                                && ann.value().equals(componentType.getType().name());
                    })
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("There is no implementation using ImplementationOfType for: "
                            + componentType.getType().name()));
            return matchedClass.getDeclaredConstructor(SmartSelenium.class)
                    .newInstance(smartSelenium);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            LogUI.error("Failed to instantiate component of type {} in package {}: {}",
                    componentType.getType().name(), projectPackage, e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
