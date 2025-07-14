package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import com.theairebellion.zeus.validator.core.AssertionResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InterceptorServiceFluentTest {

   private Storage storage;
   private UiServiceFluent<?> uiServiceFluent;
   private InterceptorServiceFluent<UiServiceFluent<?>> sut;
   private static final int TEST_STATUS = 200;
   private static final String TEST_METHOD = "GET";

   @BeforeEach
   void setUp() {
      storage = mock(Storage.class);
      uiServiceFluent = mock(UiServiceFluent.class);
      sut = new InterceptorServiceFluent<>(uiServiceFluent, storage);
   }

   @Test
   void validateResponseHaveStatusShouldValidateWithMatchingStatusPrefix() {
      // Given
      ApiResponse response1 = new ApiResponse("https://example.com/api/test1", TEST_METHOD, TEST_STATUS);
      ApiResponse response2 = new ApiResponse("https://example.com/api/test2", TEST_METHOD, TEST_STATUS);
      List<ApiResponse> responses = List.of(response1, response2);

      // Mock storage
      Storage subStorage = mock(Storage.class);
      when(storage.sub(StorageKeysUi.UI)).thenReturn(subStorage);
      when(subStorage.getByClass(StorageKeysUi.RESPONSES, Object.class)).thenReturn(responses);

      // When
      UiServiceFluent<?> result = sut.validateResponseHaveStatus("/api/", 2);

      // Then
      verify(uiServiceFluent).validation(argThat(results ->
            results.stream().allMatch(AssertionResult::isPassed)
      ));
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   void validateResponseHaveStatusShouldSupportSoftAssertionTrue() {
      // Given
      ApiResponse response = new ApiResponse("https://example.com/api/soft", TEST_METHOD, TEST_STATUS);
      List<ApiResponse> responses = List.of(response);

      Storage subStorage = mock(Storage.class);
      when(storage.sub(StorageKeysUi.UI)).thenReturn(subStorage);
      when(subStorage.getByClass(StorageKeysUi.RESPONSES, Object.class)).thenReturn(responses);

      // When
      UiServiceFluent<?> result = sut.validateResponseHaveStatus("/api/", 2, true);

      // Then
      verify(uiServiceFluent).validation(argThat(results ->
            results.stream().allMatch(AssertionResult::isSoft)
      ));
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   void validateResponseHaveStatusShouldHandleNoMatchingUrls() {
      // Given
      ApiResponse response = new ApiResponse("https://example.com/other", TEST_METHOD, TEST_STATUS);
      List<ApiResponse> responses = List.of(response);

      Storage subStorage = mock(Storage.class);
      when(storage.sub(StorageKeysUi.UI)).thenReturn(subStorage);
      when(subStorage.getByClass(StorageKeysUi.RESPONSES, Object.class)).thenReturn(responses);

      // When
      UiServiceFluent<?> result = sut.validateResponseHaveStatus("/api/", 2, false);

      // Then
      // No matches => allMatch returns true by default (on empty stream)
      verify(uiServiceFluent).validation(argThat(results ->
            results.stream().allMatch(AssertionResult::isPassed)
      ));
      assertThat(result).isSameAs(uiServiceFluent);
   }
}
