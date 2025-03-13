package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.extensions.StorageKeysUi;
import com.theairebellion.zeus.validator.core.AssertionResult;

import java.util.List;

public class InterceptorServiceFluent<T extends UIServiceFluent<?>> {

    private final T uiServiceFluent;
    private final Storage storage;


    public InterceptorServiceFluent(T uiServiceFluent, Storage storage) {
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public T validateResponseHaveStatus(final String requestUrlSubString, int statusPrefix) {
        return validateResponseHaveStatus(requestUrlSubString, statusPrefix, false);
    }


    public T validateResponseHaveStatus(final String requestUrlSubString, int statusPrefix,
                                        boolean soft) {
        List<ApiResponse> apiResponses = (List<ApiResponse>) storage.sub(StorageKeysUi.UI)
                .getByClass(StorageKeysUi.RESPONSES, Object.class);

        List<ApiResponse> filteredResponses = apiResponses.stream()
                .filter(
                        apiResponse -> apiResponse.getUrl().contains(requestUrlSubString))
                .toList();

        boolean isPassed = filteredResponses.stream()
                .allMatch(apiResponse -> validateStatus(statusPrefix, apiResponse.getStatus()));

        AssertionResult<?> result = new AssertionResult<>(isPassed,
                String.format("Validating api requests containing: %s has correct status: %d", requestUrlSubString,
                        statusPrefix), true, isPassed, soft);
        List<AssertionResult<Object>> validationResults = List.of((AssertionResult<Object>) result);
        uiServiceFluent.validation(validationResults);
        return uiServiceFluent;
    }


    private boolean validateStatus(int prefix, int status) {
        String prefixStr = String.valueOf(prefix);
        String statusStr = String.valueOf(status);
        return statusStr.startsWith(prefixStr);
    }

}
