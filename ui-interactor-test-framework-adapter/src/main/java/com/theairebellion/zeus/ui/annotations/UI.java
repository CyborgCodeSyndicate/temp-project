package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import com.theairebellion.zeus.ui.extensions.UiTestExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Marks a test class as a UI test.
 *
 * <p>This annotation enables UI testing by applying the {@link UiTestExtension} and
 * integrating UI-related framework functionalities. It ensures that necessary UI
 * configurations, drivers, and interactions are properly managed during test execution.
 *
 * <p>Applying this annotation to a test class automatically enables UI-specific features
 * such as UI element handling, request interception, and authentication mechanisms.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ExtendWith(UiTestExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@FrameworkAdapter(basePackages = {"com.theairebellion.zeus.ui"})
public @interface UI {

}
