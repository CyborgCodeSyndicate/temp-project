package com.theairebellion.zeus.ui.service;

import com.theairebellion.zeus.ui.annotations.InsertionElement;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.insertion.BaseInsertionService;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link BaseInsertionService} that processes UI elements annotated with {@link InsertionElement}.
 *
 * <p>This class provides functionality to:
 * <ul>
 *   <li>Retrieve UI element metadata from {@link InsertionElement} annotations.</li>
 *   <li>Identify the corresponding {@link ComponentType} for a UI element.</li>
 *   <li>Build Selenium locators for elements.</li>
 *   <li>Perform pre-insertion and post-insertion actions.</li>
 *   <li>Sort fields based on their insertion order.</li>
 * </ul>
 * </p>
 *
 * <p>It utilizes an {@link InsertionServiceRegistry} to manage different insertion strategies and relies on
 * a {@link SmartWebDriver} instance for interacting with web elements.</p>
 *
 * @author Cyborg Code Syndicate
 */
public class InsertionServiceElementImpl extends BaseInsertionService {

    /**
     * WebDriver instance used for interacting with UI elements.
     */
    private final SmartWebDriver webDriver;

    /**
     * Constructs an {@code InsertionServiceElementImpl} instance.
     *
     * @param serviceRegistry The registry managing available insertion services.
     * @param webDriver       The WebDriver instance for element interaction.
     */
    public InsertionServiceElementImpl(final InsertionServiceRegistry serviceRegistry, final SmartWebDriver webDriver) {
        super(serviceRegistry);
        this.webDriver = webDriver;
    }

    /**
     * Retrieves the annotation from a field.
     *
     * @param field The field containing the annotation.
     * @return The {@link InsertionElement} annotation, or {@code null} if not present.
     */
    @Override
    protected Object getFieldAnnotation(Field field) {
        return field.getAnnotation(InsertionElement.class);
    }

    /**
     * Determines the {@link ComponentType} class associated with the annotation.
     *
     * @param annotation The annotation containing component metadata.
     * @return The class of the associated {@link ComponentType}.
     */
    @Override
    protected Class<? extends ComponentType> getComponentType(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        UIElement uiElement = (UIElement) Enum.valueOf(
                (Class<? extends Enum>) insertionElement.locatorClass(), insertionElement.elementEnum()
        );
        return uiElement.componentType().getClass();
    }

    /**
     * Builds the locator (XPath, CSS selector) for identifying the UI component.
     *
     * @param annotation The annotation containing locator information.
     * @return The Selenium {@link By} locator for the component.
     */
    @Override
    protected By buildLocator(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        UIElement uiElement = (UIElement) Enum.valueOf((Class<? extends Enum>) insertionElement.locatorClass(),
            insertionElement.elementEnum());

        return uiElement.locator();
    }

    /**
     * Retrieves the enum value representing the UI component type.
     *
     * @param annotation The annotation containing component metadata.
     * @return The enum value corresponding to the component type.
     */
    @Override
    protected Enum<?> getEnumValue(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        return Enum.valueOf(
                (Class<? extends Enum>) insertionElement.locatorClass(), insertionElement.elementEnum()
        );
    }

    /**
     * Filters and sorts the fields annotated with {@link InsertionElement}.
     *
     * <p>Fields are sorted based on the {@code order} specified in the annotation.</p>
     *
     * @param fields The list of declared fields in the object.
     * @return A sorted list of fields annotated with {@link InsertionElement}.
     */
    @Override
    protected List<Field> filterAndSortFields(final Field[] fields) {
        return Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(InsertionElement.class))
                .sorted(Comparator.comparing(field -> field.getAnnotation(InsertionElement.class).order()))
                .collect(Collectors.toList());
    }

    /**
     * Executes any pre-insertion logic before inserting a value.
     *
     * <p>It retrieves the UI element associated with the annotation and executes
     * the {@code before} action if defined.</p>
     *
     * @param annotation The annotation associated with the field being inserted.
     */
    @Override
    protected void beforeInsertion(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        UIElement uiElement = (UIElement) Enum.valueOf(
                (Class<? extends Enum>) insertionElement.locatorClass(), insertionElement.elementEnum()
        );
        uiElement.before().accept(webDriver);
    }

    /**
     * Executes any post-insertion logic after inserting a value.
     *
     * <p>It retrieves the UI element associated with the annotation and executes
     * the {@code after} action if defined.</p>
     *
     * @param annotation The annotation associated with the field that was inserted.
     */
    @Override
    protected void afterInsertion(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        UIElement uiElement = (UIElement) Enum.valueOf(
                (Class<? extends Enum>) insertionElement.locatorClass(), insertionElement.elementEnum()
        );
        uiElement.after().accept(webDriver);
    }

}
