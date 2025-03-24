package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.annotations.AvailableOptionsAI;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.framework.parameters.DataForge;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines test data to be used within a pre-test setup journey.
 * <p>
 * This annotation is used in combination with {@code @Journey} to specify
 * predefined test data models that should be injected during the execution
 * of a test precondition.
 * </p>
 *
 * <p>
 * The framework resolves the specified data model dynamically using reflection,
 * ensuring that the appropriate test data object is provided as part of the test setup.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@InfoAI(description = "Annotation used to specify a data model that should be created as part of a pre-test setup journey. " +
        "Applied inside a @Journey annotation to define which data objects should be crafted and injected into the test context.")
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface JourneyData {

    /**
     * Specifies the test data model to be injected.
     * <p>
     * The value should match a predefined test data identifier, which the framework
     * resolves dynamically to generate the corresponding test object.
     * </p>
     *
     * @return The name of the test data model.
     */
    @InfoAI(description = "Add the string value representing the enum name of the Data Object")
    @AvailableOptionsAI(interfaceClass = DataForge.class)
    String value();

    /**
     * Indicates whether the test data should be created lazily.
     * <p>
     * If set to {@code true}, the test data object is not created immediately.
     * Instead, it remains a deferred reference and must be explicitly accessed
     * when needed.
     * <br>
     * If set to {@code false}, the object is created just before the test execution,
     * ensuring it is available during the test.
     * </p>
     *
     * @return {@code true} if the test data should be created lazily and requires
     * explicit resolution before use, otherwise {@code false}.
     */
    @InfoAI(description = "Set to true if this data object for creation is requesting something from previous journey")
    boolean late() default false;

}
