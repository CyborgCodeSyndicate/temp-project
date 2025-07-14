package com.theairebellion.zeus.framework.spring;

import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

/**
 * A factory for creating {@link ContextCustomizer} instances based on {@code @FrameworkAdapter} annotations.
 *
 * <p>This class scans the test class for any {@link FrameworkAdapter} annotations, collects their specified base
 * packages, and constructs a {@link FrameworkAdapterContextCustomizer} to add those packages to the Spring context
 * scan. If no {@code @FrameworkAdapter} annotations are present, it returns {@code null}, indicating that no
 * customization is needed.
 *
 * <p>This approach allows for flexible configuration of additional base packages without modifying the default Spring
 * context setup.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class FrameworkAdapterContextCustomizerFactory implements ContextCustomizerFactory {

   /**
    * Creates a new {@link ContextCustomizer} if the test class contains one or more {@link FrameworkAdapter}
    * annotations.
    *
    * <p>Each adapter's base packages are gathered and combined, ensuring duplicates are removed before creating
    * the resulting customizer.
    *
    * @param testClass        The test class to inspect for {@code @FrameworkAdapter} annotations.
    * @param configAttributes The context configuration attributes (not used in this implementation).
    * @return A {@link FrameworkAdapterContextCustomizer} if any {@code @FrameworkAdapter} annotations are found,
    *     otherwise {@code null}.
    */
   @Override
   public ContextCustomizer createContextCustomizer(
         @NonNull Class<?> testClass,
         @Nullable List<ContextConfigurationAttributes> configAttributes) {

      Set<FrameworkAdapter> annotations = AnnotatedElementUtils.findAllMergedAnnotations(testClass,
            FrameworkAdapter.class);

      if (annotations.isEmpty()) {
         return null;
      }

      return new FrameworkAdapterContextCustomizer(annotations.stream()
            .flatMap(adapter -> Arrays.stream(adapter.basePackages()))
            .distinct().toArray(String[]::new));
   }

}

