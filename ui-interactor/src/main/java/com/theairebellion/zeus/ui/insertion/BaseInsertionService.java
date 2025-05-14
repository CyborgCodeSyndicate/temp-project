package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.util.reflections.exceptions.ReflectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.openqa.selenium.By;

/**
 * Abstract base class for insertion services that handle inserting data into UI components.
 *
 * <p>This class defines a generic mechanism to process annotated fields in an object
 * and insert corresponding values into the UI using registered insertion services.
 *
 * <p>Implementing classes must define specific behaviors for extracting field annotations,
 * determining component types, building locators, and retrieving enum values.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public abstract class BaseInsertionService<A extends Annotation> implements InsertionService {

   /**
    * Registry that manages different insertion services.
    */
   protected final InsertionServiceRegistry serviceRegistry;

   /**
    * Constructs a new {@code BaseInsertionService} instance.
    *
    * @param serviceRegistry The registry containing available insertion services.
    */
   protected BaseInsertionService(final InsertionServiceRegistry serviceRegistry) {
      this.serviceRegistry = serviceRegistry;
   }

   /**
    * Resolves the specific interface that implements {@link ComponentType} from a given enum class.
    *
    * <p>If the provided {@code componentTypeClass} directly implements {@code ComponentType},
    * it is returned immediately. Otherwise, this method inspects its interfaces and returns
    * the first one that in turn extends {@code ComponentType}.</p>
    *
    * @param componentTypeClass the enum class (or interface) to inspect for a ComponentType implementation
    * @return the interface class which extends {@code ComponentType}
    * @throws IllegalStateException if no interface extending {@code ComponentType} is found
    */
   protected static Class<? extends ComponentType> extractComponentTypeClass(
         final Class<? extends ComponentType> componentTypeClass) {
      if (Arrays.asList(componentTypeClass.getInterfaces()).contains(ComponentType.class)) {
         return componentTypeClass;
      }

      @SuppressWarnings("unchecked") final Class<? extends ComponentType> resolved =
            (Class<? extends ComponentType>) Arrays.stream(componentTypeClass.getInterfaces())
                  .filter(inter -> Arrays.asList(inter.getInterfaces())
                        .contains(ComponentType.class))
                  .findFirst()
                  .orElseThrow(() -> new IllegalStateException(
                        "No interface extending ComponentType found in " + componentTypeClass
                  ));
      return resolved;
   }

   /**
    * Processes the provided data object, extracts annotated fields, and inserts values
    * into corresponding UI components.
    *
    * <p>The method:
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
    *
    * @param data The object containing values to be inserted into UI components.
    * @throws IllegalStateException If no insertion service is found for a component type.
    * @throws RuntimeException      If field access fails.
    */
   @SuppressWarnings("java:S3011")
   @Override
   public void insertData(final Object data) {
      final Field[] fields = data.getClass().getDeclaredFields();
      final List<Field> targetedFields = filterAndSortFields(fields);

      for (Field field : targetedFields) {
         final A annotation = field.getAnnotation(getAnnotationClass());

         field.setAccessible(true);
         try {
            final Class<? extends ComponentType> enumClass = getComponentTypeEnumClass(annotation);
            final Class<? extends ComponentType> componentTypeClass = extractComponentTypeClass(enumClass);
            final Insertion service = serviceRegistry.getService(componentTypeClass);
            if (service == null) {
               throw new IllegalStateException(
                     "No InsertionService registered for: " + componentTypeClass.getSimpleName()
               );
            }

            final By locator = buildLocator(annotation);
            final Object valueForField = field.get(data);

            if (valueForField != null) {
               beforeInsertion(annotation);
               service.insertion(getType(annotation), locator, valueForField);
               afterInsertion(annotation);
            }
         } catch (IllegalAccessException e) {
            throw new ReflectionException("Failed to access field: " + field.getName(), e);
         }
      }
      LogUi.info("Finished data insertion for [{}].", data.getClass().getSimpleName());
   }

   /**
    * Returns the annotation type that this insertion service handles.
    *
    * @return the {@code Class} object of the annotation this service processes
    */
   protected abstract Class<A> getAnnotationClass();

   /**
    * Determines the processing order for fields annotated with this service’s annotation.
    *
    * <p>Fields with lower order values are processed before fields with higher values.</p>
    *
    * @param annotation the annotation instance found on the field
    * @return an integer representing the priority (lower means earlier)
    */
   protected abstract int getOrder(A annotation);

   /**
    * Determines the enum class that implements {@link ComponentType} for the given annotation.
    *
    * <p>This method is used to locate the specific enum type which defines the set of
    * component types associated with the annotation. Implementations should inspect the
    * annotation’s attributes to return the correct enum class.</p>
    *
    * @param annotation the annotation instance from which to derive the component-type enum
    * @return the {@code Class} object of an enum that implements {@link ComponentType}
    */
   protected abstract Class<? extends ComponentType> getComponentTypeEnumClass(A annotation);

   /**
    * Builds the locator (e.g., XPath, CSS Selector) for identifying the target component.
    *
    * @param annotation The annotation containing locator information.
    * @return The Selenium {@link By} locator for the component.
    */
   protected abstract By buildLocator(A annotation);

   /**
    * Maps an annotation to its corresponding {@link ComponentType} enum value.
    *
    * <p>This tells the service which UI component type to use for insertion.</p>
    *
    * @param annotation the annotation instance to inspect
    * @return the enum constant (implementing {@code ComponentType}) for this annotation
    */
   protected abstract ComponentType getType(A annotation);

   /**
    * Hook method to execute any pre-insertion logic.
    *
    * <p>Implementing classes can override this to define actions before an insertion occurs.
    *
    * @param annotation The annotation associated with the field being inserted.
    */
   protected void beforeInsertion(A annotation) {
      // Can be overridden by subclasses if pre-insertion logic is needed
   }

   /**
    * Hook method to execute any post-insertion logic.
    *
    * <p>Implementing classes can override this to define actions after an insertion occurs.
    *
    * @param annotation The annotation associated with the field that was inserted.
    */
   protected void afterInsertion(A annotation) {
      // default no-op
   }

   /**
    * Filters and sorts the fields of an object before processing insertions.
    *
    * <p>Implementations can define sorting logic to prioritize certain fields over others.
    *
    * @param fields The list of declared fields in the object.
    * @return A filtered and sorted list of fields to be processed.
    */
   protected final List<Field> filterAndSortFields(final Field[] fields) {
      return Arrays.stream(fields)
            .filter(field -> field.isAnnotationPresent(getAnnotationClass()))
            .sorted(Comparator.comparingInt(field ->
                  getOrder(field.getAnnotation(getAnnotationClass()))))
            .toList();
   }

}