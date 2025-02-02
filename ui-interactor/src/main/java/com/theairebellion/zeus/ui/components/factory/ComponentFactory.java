package com.theairebellion.zeus.ui.components.factory;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.alert.Alert;
import com.theairebellion.zeus.ui.components.alert.AlertComponentType;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.Button;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.input.Input;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.link.Link;
import com.theairebellion.zeus.ui.components.link.LinkComponentType;
import com.theairebellion.zeus.ui.components.list.ItemList;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.loader.Loader;
import com.theairebellion.zeus.ui.components.loader.LoaderComponentType;
import com.theairebellion.zeus.ui.components.radio.Radio;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.components.select.Select;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.tab.Tab;
import com.theairebellion.zeus.ui.components.tab.TabComponentType;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

public class ComponentFactory {

    private ComponentFactory() {
    }


    public static Input getInputComponent(InputComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Input.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    public static Button getButtonComponent(ButtonComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Button.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    public static Radio getRadioComponent(RadioComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Radio.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    public static Select getSelectComponent(SelectComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Select.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    public static ItemList getListComponent(ItemListComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(ItemList.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    public static Loader getLoaderComponent(LoaderComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Loader.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    public static Link getLinkComponent(LinkComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Link.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    public static Alert getAlertComponent(AlertComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Alert.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    public static Tab getTabComponent(TabComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Tab.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }


    private static <T> T getComponent(Class<T> interfaceType, ComponentType componentType, String projectPackage,
                                      SmartWebDriver smartWebDriver) {
        List<Class<? extends T>> implementations = ReflectionUtil.findImplementationsOfInterface(interfaceType,
            projectPackage);

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
