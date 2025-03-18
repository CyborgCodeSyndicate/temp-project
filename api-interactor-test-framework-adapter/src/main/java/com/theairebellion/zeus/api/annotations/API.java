package com.theairebellion.zeus.api.annotations;

import com.theairebellion.zeus.api.extensions.ApiTestExtension;
import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a test class as an API test.
 * <p>
 * This annotation enables API testing by applying the {@link ApiTestExtension} and
 * integrating API-related framework functionalities. It ensures that necessary API
 * configurations, authentication mechanisms, and request handling are properly managed
 * during test execution.
 * </p>
 *
 * <p>Applying this annotation to a test class automatically enables API-specific features
 * such as API request execution, response validation, and authentication handling.</p>
 *
 * @author Cyborg Code Syndicate
 */
@ExtendWith(ApiTestExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@FrameworkAdapter(basePackages = {"com.theairebellion.zeus.api"})
public @interface API {

}

