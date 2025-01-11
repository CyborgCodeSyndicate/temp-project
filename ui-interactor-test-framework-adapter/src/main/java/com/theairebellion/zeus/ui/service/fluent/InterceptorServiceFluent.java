package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.extensions.StorageKeysUi;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.assertj.core.api.SoftAssertions;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.function.Consumer;

public class InterceptorServiceFluent extends FluentService {

    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public InterceptorServiceFluent(UIServiceFluent uiServiceFluent, Storage storage) {
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public <T> UIServiceFluent validateResponseHaveStatus(final String requestUrlSubString, int statusPrefix) {
        return validateResponseHaveStatus(requestUrlSubString, statusPrefix, false);
    }


    public <T> UIServiceFluent validateResponseHaveStatus(final String requestUrlSubString, int statusPrefix,
                                                          boolean soft) {
        List<ApiResponse> apiResponses = storage.sub(StorageKeysUi.UI)
                                             .get(StorageKeysUi.RESPONSES, new ParameterizedTypeReference<>() {
                                             });

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
        validation(validationResults);
        return uiServiceFluent;
    }


    private boolean validateStatus(int prefix, int status) {
        String prefixStr = String.valueOf(prefix);
        String statusStr = String.valueOf(status);
        return statusStr.startsWith(prefixStr);
    }


    public UIServiceFluent validate(Runnable assertion) {
        return (UIServiceFluent) super.validate(assertion);
    }


    public UIServiceFluent validate(Consumer<SoftAssertions> assertion) {
        return (UIServiceFluent) super.validate(assertion);
    }


}
