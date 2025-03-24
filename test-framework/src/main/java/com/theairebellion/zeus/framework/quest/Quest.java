package com.theairebellion.zeus.framework.quest;


import com.theairebellion.zeus.ai.metadata.model.classes.Level;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.storage.Storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.theairebellion.zeus.util.reflections.ReflectionUtil.getFieldValue;

/**
 * Manages the execution flow and data storage for test scenarios.
 * <p>
 * This class acts as the central controller for executing test operations,
 * managing service interactions, and storing data during a test run.
 * It allows transitioning between different testing contexts (worlds) and
 * ensures test completion with proper validations.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@InfoAIClass(
        level = Level.FIRST,
        description = "Quest is the core instance in all test methods. " +
                "Each test method has a unique Quest instance that manages the execution flow, transitions between different testing contexts (worlds), and ensures proper validation.")
public class Quest {

    /**
     * Stores registered test services, mapping their types to instances.
     */
    private final Map<Class<? extends FluentService>, FluentService> worlds = new HashMap<>();

    /**
     * Storage instance for temporarily holding test data within a test execution.
     */
    private final Storage storage;

    /**
     * Handles soft assertions for validation checks during the test execution.
     */
    private final CustomSoftAssertion softAssertions = new CustomSoftAssertion();

    /**
     * Initializes a new {@code Quest} instance with a fresh storage object.
     */
    public Quest() {
        this.storage = new Storage();
    }

    /**
     * Transitions the test execution context into the specified world (test service).
     * <p>
     * This method retrieves a registered service by its class type and allows the test execution
     * to proceed within that context.
     * </p>
     *
     * @param worldType The class type of the desired test service.
     * @param <T>       The type of the fluent service.
     * @return The corresponding fluent service instance.
     * @throws IllegalArgumentException If the specified world is not registered.
     */
    @InfoAI(description = "Switches the execution context to a specific test service (world). " +
            "This enables using specialized functionalities provided by different services in the test execution flow.")
    @SuppressWarnings("unchecked")
    public <T extends FluentService> T enters(
            @InfoAI(description = "The class representing a test service that extends FluentService.") Class<T> worldType) {
        Optional<Class<? extends FluentService>> match =
                worlds.keySet().stream().filter(worldType::isAssignableFrom).findFirst();
        if (match.isEmpty()) {
            throw new IllegalArgumentException("World not initialized: " + worldType.getName());
        }
        TestService worldName = worldType.getAnnotation(TestService.class);
        if (worldName == null) {
            worldName = worldType.getAnnotation(TestService.class);
        }
        String message = worldName == null
                ? "The quest has undertaken a journey through: '" + worldType.getName() + "'"
                : "The quest has undertaken a journey through: '" + worldName.value() + "'";

        LogTest.info(message);
        return (T) worlds.get(match.get());
    }

    /**
     * Retrieves a registered fluent service without modifying the execution context.
     *
     * @param worldType The class type of the service to retrieve.
     * @param <T>       The type of the fluent service.
     * @return The corresponding service instance.
     * @throws IllegalArgumentException If the specified world is not registered.
     */
    @InfoAI(description = "Retrieves an instance of a registered test service without switching the execution context. " +
            "This allows accessing service-specific functionalities while maintaining the current flow.")
    @SuppressWarnings("unchecked")
    protected <T extends FluentService> T cast(
            @InfoAI(description = "The class representing a registered test service that extends FluentService.") Class<T> worldType) {
        Optional<Class<? extends FluentService>> match =
                worlds.keySet().stream().filter(worldType::isAssignableFrom).findFirst();
        if (match.isEmpty()) {
            throw new IllegalArgumentException("World not initialized: " + worldType.getName());
        }
        return (T) worlds.get(match.get());
    }

    /**
     * Marks the completion of the test execution.
     * <p>
     * This method logs the completion, clears the test execution state, and verifies all
     * soft assertions collected during the test execution.
     * </p>
     */
    @InfoAI(description = "Finalizes the test execution by clearing the test state and validating all accumulated soft assertions. " +
            "This method must be called at the end of each test to ensure all validations are processed.")
    public void complete() {
        LogTest.info("The quest has reached his end");
        QuestHolder.clear();
        softAssertions.assertAll();
    }

    /**
     * Retrieves an artifact associated with a specific test service.
     * <p>
     * This method extracts an instance of the specified artifact type from the
     * requested test service.
     * </p>
     *
     * @param worldType    The class type of the test service.
     * @param artifactType The class type of the artifact to retrieve.
     * @param <T>          The type of the test service.
     * @param <K>          The type of the artifact.
     * @return The retrieved artifact instance.
     * @throws IllegalArgumentException If input parameters are null.
     * @throws IllegalStateException    If the requested world is not available.
     */
    @InfoAI(description = "Retrieves a specific artifact from a test service instance. " +
            "The artifact is an object extracted from the given service type.")
    protected <T extends FluentService, K> K artifact(
            @InfoAI(description = "The test service class from which the artifact should be retrieved.") Class<T> worldType,
            @InfoAI(description = "The class type of the artifact to extract from the test service.") Class<K> artifactType) {
        if (worldType == null || artifactType == null) {
            throw new IllegalArgumentException("Parameters worldType and artifactType must not be null.");
        }

        T world = cast(worldType);
        if (world == null) {
            throw new IllegalStateException(
                    "Could not retrieve an instance of the specified worldType: " + worldType.getName());
        }

        return getFieldValue(world, artifactType);
    }

    /**
     * Registers a new test service into the current execution context.
     *
     * @param worldType The class type of the test service.
     * @param world     The instance of the test service.
     */
    @InfoAI(description = "Registers a new test service instance in the execution context, " +
            "making it available for use in the current test session.")
    protected void registerWorld(
            @InfoAI(description = "The class type of the test service to be registered.") Class<? extends FluentService> worldType,
            @InfoAI(description = "The instance of the test service being registered.") FluentService world) {
        worlds.put(worldType, world);
    }

    /**
     * Removes a test service from the current execution context.
     *
     * @param worldType The class type of the test service to remove.
     */
    @InfoAI(description = "Removes a previously registered test service from the execution context, " +
            "making it unavailable for further use in the current test session.")
    protected void removeWorld(
            @InfoAI(description = "The class type of the test service to be removed.") Class<? extends FluentService> worldType) {
        worlds.remove(worldType);
    }

    /**
     * Provides access to the storage instance for managing temporary test data.
     *
     * @return The storage instance.
     */
    @InfoAI(description = "Retrieves the storage instance used for temporarily holding test data within a test execution.")
    protected Storage getStorage() {
        return storage;
    }

    /**
     * Provides access to the soft assertion handler for managing test validations.
     *
     * @return The soft assertion handler.
     */
    @InfoAI(description = "Retrieves the soft assertion handler used for managing test validations, " +
            "allowing multiple assertions to be checked without stopping execution.")
    protected CustomSoftAssertion getSoftAssertions() {
        return softAssertions;
    }

}
