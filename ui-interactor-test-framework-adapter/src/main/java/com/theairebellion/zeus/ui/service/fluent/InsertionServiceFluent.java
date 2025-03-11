package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.insertion.InsertionService;

/**
 * A fluent service for handling data insertion operations.
 * <p>
 * This class provides a streamlined interface to facilitate data insertion while maintaining
 * fluent interactions with the UI testing framework.
 *
 * @param <T> The type parameter extending {@link UIServiceFluent}, allowing method chaining.
 *
 * @author Cyborg Code Syndicate
 */
public class InsertionServiceFluent<T extends UIServiceFluent<?>> {

    private final InsertionService insertionService;
    private final T uiServiceFluent;
    private final Storage storage;

    /**
     * Constructs an {@code InsertionServiceFluent} instance.
     *
     * @param insertionService The service responsible for handling insertion operations.
     * @param uiServiceFluent  The UI service fluent instance to maintain fluent method chaining.
     * @param storage          The storage instance used to maintain test data.
     */
    public InsertionServiceFluent(final InsertionService insertionService, final T uiServiceFluent,
                                  final Storage storage) {
        this.insertionService = insertionService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }

    /**
     * Inserts the specified data using the {@link InsertionService}.
     *
     * @param data The data to be inserted.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T insertData(Object data) {
        insertionService.insertData(data);
        return uiServiceFluent;
    }

}
