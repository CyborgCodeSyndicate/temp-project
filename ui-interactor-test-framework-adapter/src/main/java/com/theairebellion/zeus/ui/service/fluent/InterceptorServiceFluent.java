package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.qameta.allure.Allure;

import java.util.List;

/**
 * A fluent service for handling API response validation and interception.
 * <p>
 * This service provides methods to validate API responses stored in the UI storage,
 * ensuring that requests contain expected status codes.
 * </p>
 *
 * The generic type {@code T} represents the UI service fluent implementation that extends {@link UIServiceFluent},
 * allowing method chaining for seamless interaction.
 *
 * @author Cyborg Code Syndicate
 */
public class InterceptorServiceFluent<T extends UIServiceFluent<?>> {

    private final T uiServiceFluent;
    private final Storage storage;

    /**
     * Constructs an {@code InterceptorServiceFluent} instance.
     *
     * @param uiServiceFluent The UI service fluent instance to maintain fluent method chaining.
     * @param storage         The storage instance where API responses are stored for validation.
     */
    public InterceptorServiceFluent(T uiServiceFluent, Storage storage) {
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }

    /**
     * Validates that all API responses matching a given URL substring have a status code
     * starting with the specified prefix.
     *
     * @param requestUrlSubString The substring to match against API response URLs.
     * @param statusPrefix        The expected status code prefix (e.g., 2 for 200-level responses).
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T validateResponseHaveStatus(final String requestUrlSubString, int statusPrefix) {
        Allure.step("Validate that all API responses containing URL substring: " + requestUrlSubString + " have status prefix: " + statusPrefix);
        return validateResponseHaveStatus(requestUrlSubString, statusPrefix, false);
    }

    /**
     * Validates that all API responses matching a given URL substring have a status code
     * starting with the specified prefix, with an option for soft assertions.
     *
     * @param requestUrlSubString The substring to match against API response URLs.
     * @param statusPrefix        The expected status code prefix (e.g., 2 for 200-level responses).
     * @param soft                {@code true} if the assertion should be a soft assertion,
     *                            allowing the test to continue even if it fails.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T validateResponseHaveStatus(final String requestUrlSubString, int statusPrefix,
                                        boolean soft) {
        List<ApiResponse> apiResponses = (List<ApiResponse>) storage.sub(StorageKeysUi.UI)
                .getByClass(StorageKeysUi.RESPONSES, Object.class);

        List<ApiResponse> filteredResponses = apiResponses.stream()
                .filter(apiResponse -> apiResponse.getUrl().contains(requestUrlSubString))
                .toList();

        boolean isPassed = filteredResponses.stream()
                .allMatch(apiResponse -> validateStatus(statusPrefix, apiResponse.getStatus()));

        AssertionResult<?> result = new AssertionResult<>(isPassed,
                String.format("Validating API requests containing: %s have correct status: %d",
                        requestUrlSubString, statusPrefix),
                true, isPassed, soft);

        List<AssertionResult<Object>> validationResults = List.of((AssertionResult<Object>) result);
        uiServiceFluent.validation(validationResults);
        return uiServiceFluent;
    }

    /**
     * Checks whether a given status code starts with the specified prefix.
     *
     * @param prefix The expected status code prefix.
     * @param status The actual status code.
     * @return {@code true} if the status code starts with the prefix, otherwise {@code false}.
     */
    private boolean validateStatus(int prefix, int status) {
        String prefixStr = String.valueOf(prefix);
        String statusStr = String.valueOf(status);
        return statusStr.startsWith(prefixStr);
    }

}
