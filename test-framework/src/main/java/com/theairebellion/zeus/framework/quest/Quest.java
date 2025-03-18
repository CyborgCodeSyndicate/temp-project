package com.theairebellion.zeus.framework.quest;


import com.theairebellion.zeus.ai.metadata.model.Level;
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
@InfoAIClass(level = Level.FIRST,
    description = "Quest is the main instance in all test methods. There is single quest instance for each test method from that one all methods are chained")
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

    @InfoAI(description = "The enters method enables using specific service that has different capabilities")
    @SuppressWarnings("unchecked")
    public <T extends FluentService> T enters(
        @InfoAI(description = "Class that needs to extends from FluentService.") Class<T> worldType) {
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
    @SuppressWarnings("unchecked")
    protected <T extends FluentService> T cast(Class<T> worldType) {
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

    @InfoAI(
        description = "The complete method is the last method for each test where. If there are soft assertions it validate them all")
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
    protected <T extends FluentService, K> K artifact(Class<T> worldType, Class<K> artifactType) {
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
    protected void registerWorld(Class<? extends FluentService> worldType, FluentService world) {
        worlds.put(worldType, world);
    }

    /**
     * Removes a test service from the current execution context.
     *
     * @param worldType The class type of the test service to remove.
     */
    protected void removeWorld(Class<? extends FluentService> worldType) {
        worlds.remove(worldType);
    }

    /**
     * Provides access to the storage instance for managing temporary test data.
     *
     * @return The storage instance.
     */
    protected Storage getStorage() {
        return storage;
    }

    /**
     * Provides access to the soft assertion handler for managing test validations.
     *
     * @return The soft assertion handler.
     */
    protected CustomSoftAssertion getSoftAssertions() {
        return softAssertions;
    }

}

