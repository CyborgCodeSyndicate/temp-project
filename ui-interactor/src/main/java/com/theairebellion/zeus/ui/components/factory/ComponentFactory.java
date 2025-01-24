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
import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

public class ComponentFactory {

    public static final UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);


    public static Input getInputComponent(InputComponentType type, SmartSelenium smartSelenium) {
        return getComponent(Input.class, type, uiConfig.projectPackage(), smartSelenium);
    }

    public static Button getButtonComponent(ButtonComponentType type, SmartSelenium smartSelenium) {
        return getComponent(Button.class, type, uiConfig.projectPackage(), smartSelenium);
    }

    public static Radio getRadioComponent(RadioComponentType type, SmartSelenium smartSelenium) {
        return getComponent(Radio.class, type, uiConfig.projectPackage(), smartSelenium);
    }

    public static Select getSelectComponent(SelectComponentType type, SmartSelenium smartSelenium) {
        return getComponent(Select.class, type, uiConfig.projectPackage(), smartSelenium);
    }

    public static ItemList getListComponent(ItemListComponentType type, SmartSelenium smartSelenium) {
        return getComponent(ItemList.class, type, uiConfig.projectPackage(), smartSelenium);
    }

    public static Loader getLoaderComponent(LoaderComponentType type, SmartSelenium smartSelenium) {
        return getComponent(Loader.class, type, uiConfig.projectPackage(), smartSelenium);
    }

    public static Link getLinkComponent(LinkComponentType type, SmartSelenium smartSelenium) {
        return getComponent(Link.class, type, uiConfig.projectPackage(), smartSelenium);
    }

    public static Alert getAlertComponent(AlertComponentType type, SmartSelenium smartSelenium) {
        return getComponent(Alert.class, type, uiConfig.projectPackage(), smartSelenium);
    }


    public static <T> T getComponent(Class<T> interfaceType, ComponentType componentType, String projectPackage,
                                     SmartSelenium smartSelenium) {
        List<Class<? extends T>> implementationsOfInterface = ReflectionUtil.findImplementationsOfInterface(
                interfaceType, projectPackage);
        try {
            return implementationsOfInterface.stream()
                    .filter(aClass -> Objects.nonNull(aClass.getAnnotation(ImplementationOfType.class)) &&
                            aClass.getAnnotation(ImplementationOfType.class).value().equals(componentType.getType().name()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "There is no implementation using ImplementationOfType for: " + componentType.getType().name()))
                    .getDeclaredConstructor(SmartSelenium.class)
                    .newInstance(smartSelenium);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
