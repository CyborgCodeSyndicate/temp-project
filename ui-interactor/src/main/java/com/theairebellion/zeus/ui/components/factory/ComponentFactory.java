package com.theairebellion.zeus.ui.components.factory;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.Input;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.table.service.Table;
import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

public class ComponentFactory {

    //todo rename in future
    private static final String FRAMEWORK_PACKAGE = "com.theairebellion.zeus";


    private ComponentFactory() {
    }


    public static Input getInputComponent(InputComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Input.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }


    public static Table getTableComponent(TableComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Table.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }


    private static <T> T getComponent(Class<T> interfaceType, ComponentType componentType, String projectPackage,
                                      SmartWebDriver smartWebDriver) {
        List<Class<? extends T>> implementations = ReflectionUtil.findImplementationsOfInterface(interfaceType,
            projectPackage);
        implementations.addAll(ReflectionUtil.findImplementationsOfInterface(interfaceType,
            FRAMEWORK_PACKAGE));


        Optional<Class<? extends T>> implementation = implementations.stream()
                                                          .filter(
                                                              aClass -> isMatchingImplementation(aClass, componentType))
                                                          .findFirst();

        return implementation.map(aClass -> createInstance(aClass, smartWebDriver))
                   .orElseThrow(() -> new ComponentNotFoundException(
                       "No implementation found for type: " + componentType.getType().name()));
    }


    private static <T> boolean isMatchingImplementation(Class<? extends T> aClass, ComponentType componentType) {
        return Optional.ofNullable(aClass.getAnnotation(ImplementationOfType.class))
                   .map(ImplementationOfType::value)
                   .filter(value -> value.equals(componentType.getType().name()))
                   .isPresent();
    }


    private static <T> T createInstance(Class<? extends T> aClass, SmartWebDriver smartWebDriver) {
        try {
            return aClass.getDeclaredConstructor(SmartWebDriver.class).newInstance(smartWebDriver);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            LogUI.error("Failed to create instance of " + aClass.getName(), e);
            throw new ComponentCreationException("Failed to create instance of " + aClass.getName(), e);
        }
    }


    public static class ComponentNotFoundException extends RuntimeException {

        public ComponentNotFoundException(String message) {
            super(message);
        }

    }

    public static class ComponentCreationException extends RuntimeException {

        public ComponentCreationException(String message, Throwable cause) {
            super(message, cause);
        }

    }


}
