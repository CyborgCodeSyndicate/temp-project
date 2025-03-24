package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.annotations.InsertionField;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Implementation of {@link BaseInsertionService} that processes fields annotated with {@link InsertionField}.
 * <p>
 * This class identifies UI components using field annotations, retrieves their corresponding locators,
 * and delegates data insertion to registered {@link Insertion} services.
 * </p>
 *
 * <p>
 * The fields in a given object are processed in an ordered manner based on the `order` attribute
 * defined in the {@link InsertionField} annotation. This ensures a structured data entry process.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class InsertionServiceFieldImpl extends BaseInsertionService {

    /**
     * Constructs an instance of {@code InsertionServiceFieldImpl}.
     *
     * @param serviceRegistry The registry that maintains the mapping between component types and insertion services.
     */
    public InsertionServiceFieldImpl(final InsertionServiceRegistry serviceRegistry) {
        super(serviceRegistry);
    }

    /**
     * Retrieves the {@link InsertionField} annotation from a given field.
     *
     * @param field The field to inspect.
     * @return The {@link InsertionField} annotation if present, otherwise {@code null}.
     */
    @Override
    protected Object getFieldAnnotation(Field field) {
        return field.getAnnotation(InsertionField.class);
    }

    /**
     * Determines the component type associated with the given annotation.
     *
     * @param annotation The annotation containing component metadata.
     * @return The {@link ComponentType} class representing the UI component.
     */
    @Override
    protected Class<? extends ComponentType> getComponentType(Object annotation) {
        InsertionField insertionField = (InsertionField) annotation;
        return insertionField.type();
    }

    /**
     * Constructs a Selenium {@link By} locator based on the {@link InsertionField} annotation.
     *
     * @param annotation The annotation containing locator details.
     * @return A {@link By} locator instance used for locating UI elements.
     */
    @Override
    protected By buildLocator(Object annotation) {
        InsertionField insertionField = (InsertionField) annotation;
        FindBy.FindByBuilder findByBuilder = new FindBy.FindByBuilder();
        return findByBuilder.buildIt(insertionField.locator(), null);
    }

    /**
     * Retrieves the enum constant corresponding to the UI component type.
     *
     * @param annotation The annotation containing the component type information.
     * @return The enum value representing the UI component.
     */
    @Override
    protected Enum<?> getEnumValue(Object annotation) {
        InsertionField insertionField = (InsertionField) annotation;
        return (Enum<?>) ReflectionUtil.findEnumImplementationsOfInterface(
                insertionField.type(), insertionField.componentType(), getUiConfig().projectPackage());
    }

    /**
     * Filters and sorts fields annotated with {@link InsertionField} based on their order.
     * <p>
     * This ensures that fields are processed in a structured sequence defined by the user.
     * </p>
     *
     * @param fields The array of fields in the object being processed.
     * @return A sorted list of fields annotated with {@link InsertionField}.
     */
    @Override
    protected List<Field> filterAndSortFields(final Field[] fields) {
        return Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(InsertionField.class))
                .sorted(Comparator.comparing(field -> field.getAnnotation(InsertionField.class).order()))
                .collect(Collectors.toList());
    }

}
