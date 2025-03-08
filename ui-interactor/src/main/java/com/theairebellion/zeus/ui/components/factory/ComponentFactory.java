package com.theairebellion.zeus.ui.components.factory;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.accordion.Accordion;
import com.theairebellion.zeus.ui.components.accordion.AccordionComponentType;
import com.theairebellion.zeus.ui.components.alert.Alert;
import com.theairebellion.zeus.ui.components.alert.AlertComponentType;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.Button;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.checkbox.Checkbox;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.components.input.Input;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.link.Link;
import com.theairebellion.zeus.ui.components.link.LinkComponentType;
import com.theairebellion.zeus.ui.components.list.ItemList;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.loader.Loader;
import com.theairebellion.zeus.ui.components.loader.LoaderComponentType;
import com.theairebellion.zeus.ui.components.modal.Modal;
import com.theairebellion.zeus.ui.components.modal.ModalComponentType;
import com.theairebellion.zeus.ui.components.radio.Radio;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.components.select.Select;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.tab.Tab;
import com.theairebellion.zeus.ui.components.tab.TabComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.service.Table;
import com.theairebellion.zeus.ui.components.toggle.Toggle;
import com.theairebellion.zeus.ui.components.toggle.ToggleComponentType;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Factory class responsible for dynamically creating UI component instances.
 * <p>
 * This class retrieves component instances based on their type using reflection.
 * It scans the project package and framework package for implementations of UI components
 * that are annotated with {@link ImplementationOfType}.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class ComponentFactory {

    /**
     * The framework package where core components are defined.
     */
    //todo rename in future
    private static final String FRAMEWORK_PACKAGE = "com.theairebellion.zeus";

    /**
     * Private constructor to prevent instantiation.
     */
    private ComponentFactory() {
    }

    /**
     * Retrieves an {@code Input} component of the specified type.
     *
     * @param type           The type of input component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Input} component instance.
     */
    public static Input getInputComponent(InputComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Input.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Button} component of the specified type.
     *
     * @param type           The type of button component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Button} component instance.
     */
    public static Button getButtonComponent(ButtonComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Button.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Radio} component of the specified type.
     *
     * @param type           The type of radio component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Radio} component instance.
     */
    public static Radio getRadioComponent(RadioComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Radio.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Select} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Select} component instance.
     */
    public static Select getSelectComponent(SelectComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Select.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code ItemList} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code ItemList} component instance.
     */
    public static ItemList getListComponent(ItemListComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(ItemList.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Loader} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Loader} component instance.
     */
    public static Loader getLoaderComponent(LoaderComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Loader.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Link} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Link} component instance.
     */
    public static Link getLinkComponent(LinkComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Link.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Alert} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Alert} component instance.
     */
    public static Alert getAlertComponent(AlertComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Alert.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Tab} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Tab} component instance.
     */
    public static Tab getTabComponent(TabComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Tab.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Checkbox} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Checkbox} component instance.
     */
    public static Checkbox getCheckBoxComponent(CheckboxComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Checkbox.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Toggle} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Toggle} component instance.
     */
    public static Toggle getToggleComponent(ToggleComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Toggle.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Modal} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Modal} component instance.
     */
    public static Modal getModalComponent(ModalComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Modal.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Accordion} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Accordion} component instance.
     */
    public static Accordion getAccordionComponent(AccordionComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Accordion.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Retrieves a {@code Table} component of the specified type.
     *
     * @param type           The type of select component.
     * @param smartWebDriver The {@code SmartWebDriver} instance used for UI interactions.
     * @return The corresponding {@code Table} component instance.
     */
    public static Table getTableComponent(TableComponentType type, SmartWebDriver smartWebDriver) {
        return getComponent(Table.class, type, getUiConfig().projectPackage(), smartWebDriver);
    }

    /**
     * Discovers and retrieves a component implementation matching the specified interface and type.
     * <p>
     * The method scans both the user's project package and the framework package
     * for classes that implement the given interface and are annotated
     * with {@link ImplementationOfType} matching the provided {@code componentType}.
     * </p>
     *
     * @param interfaceType  The class object representing the component interface (e.g. {@code Input.class}).
     * @param componentType  The enum-based type identifying the component variant.
     * @param projectPackage The user's project package to scan for implementations.
     * @param smartWebDriver The WebDriver used for UI interactions.
     * @param <T>            The generic type of the component interface.
     * @return A newly instantiated component matching the desired type.
     * @throws ComponentNotFoundException If no matching implementation class is found.
     */
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

    /**
     * Checks whether the provided class is annotated with {@link ImplementationOfType}
     * matching the given component type enum name.
     *
     * @param aClass        The class to check.
     * @param componentType The component type (enum value).
     * @param <T>           The component interface type.
     * @return {@code true} if the class annotation value matches the enum's name; {@code false} otherwise.
     */
    private static <T> boolean isMatchingImplementation(Class<? extends T> aClass, ComponentType componentType) {
        return Optional.ofNullable(aClass.getAnnotation(ImplementationOfType.class))
                .map(ImplementationOfType::value)
                .filter(value -> value.equals(componentType.getType().name()))
                .isPresent();
    }

    /**
     * Instantiates a component class by invoking its constructor that accepts a {@link SmartWebDriver}.
     *
     * @param aClass         The concrete implementation class.
     * @param smartWebDriver The WebDriver for UI interactions.
     * @param <T>            The component interface type.
     * @return A new instance of the specified class.
     * @throws ComponentCreationException If reflection-based instantiation fails.
     */
    private static <T> T createInstance(Class<? extends T> aClass, SmartWebDriver smartWebDriver) {
        try {
            return aClass.getDeclaredConstructor(SmartWebDriver.class).newInstance(smartWebDriver);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            LogUI.error("Failed to create instance of " + aClass.getName(), e);
            throw new ComponentCreationException("Failed to create instance of " + aClass.getName(), e);
        }
    }

    /**
     * Exception thrown when no matching component implementation is found for a given type.
     */
    public static class ComponentNotFoundException extends RuntimeException {

        /**
         * Constructs a {@code ComponentNotFoundException} with the specified message.
         *
         * @param message The detail message explaining why the component was not found.
         */
        public ComponentNotFoundException(String message) {
            super(message);
        }

    }

    /**
     * Exception thrown when a component implementation fails to instantiate.
     */
    public static class ComponentCreationException extends RuntimeException {

        /**
         * Constructs a {@code ComponentCreationException} with the specified message and cause.
         *
         * @param message The detail message.
         * @param cause   The underlying cause of the creation failure.
         */
        public ComponentCreationException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
