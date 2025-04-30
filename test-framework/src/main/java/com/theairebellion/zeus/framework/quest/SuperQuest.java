package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.storage.Storage;
import lombok.experimental.Delegate;

/**
 * Provides an enhanced test execution context by delegating core operations to an underlying {@code Quest} instance.
 * <p>
 * The {@code SuperQuest} class wraps a {@code Quest} instance, delegating methods such as registering or removing test services,
 * retrieving artifacts, accessing storage, and managing soft assertions. This ensures that all functionality available in {@code Quest}
 * is seamlessly accessible, while also enabling extended behaviors in the test execution flow.
 * </p>
 *
 * <p>
 * Methods in this class mirror those in {@code Quest} and provide consistent documentation for interacting with the test execution context.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public final class SuperQuest extends Quest {

    /**
     * The underlying {@code Quest} instance that holds the original test execution context.
     */
    @Delegate
    private final Quest original;


    /**
     * Constructs a new {@code SuperQuest} by wrapping the provided {@code Quest}.
     *
     * @param quest the original {@code Quest} instance to be wrapped.
     */
    public SuperQuest(Quest quest) {
        this.original = quest;
    }


    /**
     * Retrieves an artifact from the underlying quest.
     * <p>
     * This method delegates the call to the underlying {@code Quest} instance,
     * extracting an instance of the specified artifact type from the corresponding test service.
     * </p>
     *
     * @param worldType    the class type of the test service.
     * @param artifactType the class type of the artifact to retrieve.
     * @param <T>          the type of the test service.
     * @param <K>          the type of the artifact.
     * @return the artifact instance retrieved from the underlying quest.
     */
    @Override
    public <T extends FluentService, K> K artifact(Class<T> worldType, Class<K> artifactType) {
        return original.artifact(worldType, artifactType);
    }


    /**
     * Registers a test service (world) into the underlying quest.
     * <p>
     * This method delegates the registration of the test service to the underlying {@code Quest} instance,
     * ensuring that the test execution context is aware of the new service.
     * </p>
     *
     * @param worldType the class type of the test service.
     * @param world     the instance of the test service.
     */
    @Override
    public void registerWorld(Class<? extends FluentService> worldType, FluentService world) {
        original.registerWorld(worldType, world);
    }


    /**
     * Retrieves a registered test service from the underlying quest without modifying the context.
     * <p>
     * This method delegates the retrieval to the underlying {@code Quest} instance,
     * returning the requested test service instance.
     * </p>
     *
     * @param worldType the class type of the test service to retrieve.
     * @param <T>       the type of the test service.
     * @return the corresponding test service instance.
     */
    @Override
    public <T extends FluentService> T cast(Class<T> worldType) {
        return super.cast(worldType);
    }


    /**
     * Removes a test service from the underlying quest.
     * <p>
     * This method delegates the removal operation to the underlying {@code Quest} instance,
     * ensuring that the specified test service is no longer part of the execution context.
     * </p>
     *
     * @param worldType the class type of the test service to remove.
     */
    @Override
    public void removeWorld(Class<? extends FluentService> worldType) {
        original.removeWorld(worldType);
    }


    /**
     * Provides access to the storage instance from the underlying quest.
     * <p>
     * The storage instance is used for managing temporary test data during execution.
     * </p>
     *
     * @return the {@link Storage} instance for the underlying quest.
     */
    @Override
    public Storage getStorage() {
        return original.getStorage();
    }


    /**
     * Provides access to the soft assertion handler from the underlying quest.
     * <p>
     * The soft assertion handler collects and verifies soft assertions during test execution.
     * </p>
     *
     * @return the {@link CustomSoftAssertion} instance for the underlying quest.
     */
    @Override
    public CustomSoftAssertion getSoftAssertions() {
        return original.getSoftAssertions();
    }


    public Quest getOriginal() {
        return original;
    }

}
