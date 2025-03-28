package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.log.LogUI;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * Abstract base class for insertion services that handle inserting data into UI components.
 * <p>
 * This class defines a generic mechanism to process annotated fields in an object
 * and insert corresponding values into the UI using registered insertion services.
 * </p>
 *
 * <p>Implementing classes must define specific behaviors for extracting field annotations,
 * determining component types, building locators, and retrieving enum values.</p>
 *
 * @author Cyborg Code Syndicate
 */
public abstract class BaseInsertionService implements InsertionService {

    /** Registry that manages different insertion services. */
    protected final InsertionServiceRegistry serviceRegistry;

    /**
     * Constructs a new {@code BaseInsertionService} instance.
     *
     * @param serviceRegistry The registry containing available insertion services.
     */
    public BaseInsertionService(final InsertionServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * Processes the provided data object, extracts annotated fields, and inserts values
     * into corresponding UI components.
     * <p>
     * The method:
     * <ol>
     *   <li>Retrieves all declared fields of the given object.</li>
     *   <li>Filters and sorts the fields based on the implementation logic.</li>
     *   <li>For each field:
     *     <ul>
     *       <li>Retrieves the annotation (if present).</li>
     *       <li>Identifies the corresponding {@link ComponentType}.</li>
     *       <li>Fetches the appropriate insertion service.</li>
     *       <li>Builds the locator and retrieves the field value.</li>
     *       <li>Performs pre-insertion operations (if any).</li>
     *       <li>Invokes the insertion service to insert the value.</li>
     *       <li>Performs post-insertion operations (if any).</li>
     *     </ul>
     *   </li>
     * </ol>
     * </p>
     *
     * @param data The object containing values to be inserted into UI components.
     * @throws IllegalStateException If no insertion service is found for a component type.
     * @throws RuntimeException If field access fails.
     */
    @Override
    public void insertData(final Object data) {
        Field[] fields = data.getClass().getDeclaredFields();
        List<Field> targetedFields = filterAndSortFields(fields);

        for (Field field : targetedFields) {
            Optional<?> annotation = Optional.ofNullable(getFieldAnnotation(field));
            if (annotation.isEmpty()) {
                continue;
            }
            field.setAccessible(true);

            try {
                Class<? extends ComponentType> componentTypeClass = getComponentType(annotation);
                Insertion service = serviceRegistry.getService(componentTypeClass);
                if (service == null) {
                    throw new IllegalStateException(
                            "No InsertionService registered for: " + componentTypeClass.getSimpleName()
                    );
                }

                By locator = buildLocator(annotation);
                LogUI.debug("Field [{}] -> Locator: [{}]", field.getName(), locator);

                Enum<?> enumValue = getEnumValue(annotation);
                LogUI.debug("Field [{}] -> Enum component type: [{}]", field.getName(), enumValue);

                Object valueForField = field.get(data);

                if (valueForField != null) {
                    beforeInsertion(annotation);
                    service.insertion((ComponentType) enumValue, locator, valueForField);
                    afterInsertion(annotation);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
        LogUI.info("Finished data insertion for [{}].", data.getClass().getSimpleName());
    }

    /**
     * Retrieves the annotation applied to the given field.
     *
     * @param field The field to inspect.
     * @return The annotation object, or {@code null} if no relevant annotation is present.
     */
    protected abstract Object getFieldAnnotation(Field field);

    /**
     * Determines the component type associated with the given annotation.
     *
     * @param annotation The annotation containing component metadata.
     * @return The component type class associated with the annotation.
     */
    protected abstract Class<? extends ComponentType> getComponentType(Object annotation);

    /**
     * Builds the locator (e.g., XPath, CSS Selector) for identifying the target component.
     *
     * @param annotation The annotation containing locator information.
     * @return The Selenium {@link By} locator for the component.
     */
    protected abstract By buildLocator(Object annotation);

    /**
     * Retrieves the enum value representing the UI component type.
     *
     * @param annotation The annotation containing component metadata.
     * @return The enum value corresponding to the component type.
     */
    protected abstract Enum<?> getEnumValue(Object annotation);

    /**
     * Filters and sorts the fields of an object before processing insertions.
     * <p>
     * Implementations can define sorting logic to prioritize certain fields over others.
     * </p>
     *
     * @param fields The list of declared fields in the object.
     * @return A filtered and sorted list of fields to be processed.
     */
    protected abstract List<Field> filterAndSortFields(Field[] fields);

    /**
     * Hook method to execute any pre-insertion logic.
     * <p>
     * Implementing classes can override this to define actions before an insertion occurs.
     * </p>
     *
     * @param annotation The annotation associated with the field being inserted.
     */
    protected void beforeInsertion(Object annotation) {
        // Can be overridden by subclasses if pre-insertion logic is needed
    }

    /**
     * Hook method to execute any post-insertion logic.
     * <p>
     * Implementing classes can override this to define actions after an insertion occurs.
     * </p>
     *
     * @param annotation The annotation associated with the field that was inserted.
     */
    protected void afterInsertion(Object annotation) {
    }
}
