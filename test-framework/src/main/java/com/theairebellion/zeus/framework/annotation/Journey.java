package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.annotations.AvailableOptionsAI;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.framework.parameters.PreQuestJourney;

import java.lang.annotation.*;

/**
 * Defines a pre-test setup step ("journey") to be executed before the test runs.
 * <p>
 * This annotation is used to specify a predefined setup action ("journey") that should be executed
 * before a test starts. It is typically applied in combination with {@code @PreQuest}
 * to configure multiple setup steps in a defined order.
 * </p>
 *
 * <p>
 * The framework processes this annotation using reflection in order to resolve the specified journey,
 * execute the corresponding setup logic, and inject any required test data.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@InfoAI(description = "Defines a setup action (journey) that must run before the test. " +
        "Used inside @PreQuest to organize the preconditions before a test method runs.")
@Repeatable(PreQuest.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Journey {

    /**
     * Specifies the journey to be executed before the test.
     * <p>
     * The value should match a predefined journey identifier, which the framework
     * resolves and executes to set up the necessary preconditions for the test.
     * </p>
     *
     * @return The name of the journey.
     */
    @InfoAI(description = "Add the string value representing the enum name of the Before Action")
    @AvailableOptionsAI(interfaceClass = PreQuestJourney.class)
    String value();

    /**
     * Defines additional test data to be used within the journey.
     * <p>
     * This allows the journey to be parameterized with specific test data objects,
     * which are resolved dynamically by the framework.
     * </p>
     *
     * @return An array of {@code JourneyData} objects containing test data.
     */
    JourneyData[] journeyData() default {};

    /**
     * Specifies the execution order of the journey when multiple journeys are defined.
     * <p>
     * Lower values indicate higher priority, meaning they will be executed first.
     * </p>
     *
     * @return The execution order of the journey.
     */
    @InfoAI(description = "In what order to execute the Journeys of there are multiple in PreQuest for a specific test")
    int order() default 0;

}
