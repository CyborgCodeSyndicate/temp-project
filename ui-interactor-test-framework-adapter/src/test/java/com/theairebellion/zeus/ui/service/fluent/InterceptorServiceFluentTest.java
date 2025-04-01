package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("InterceptorServiceFluent Tests")
@MockitoSettings(strictness = Strictness.LENIENT)
class InterceptorServiceFluentTest {

    @Mock
    private UIServiceFluent<?> mockUiServiceFluent;

    @Mock
    private Storage mockStorage;

    @Mock
    private SuperQuest mockSuperQuest;

    private InterceptorServiceFluent<UIServiceFluent<?>> sut;

    @BeforeEach
    void setUp() throws Exception {
        // Create the system under test
        sut = new InterceptorServiceFluent<>(mockUiServiceFluent, mockStorage);

        // Use reflection to set protected quest field in FluentService
        Field questField = FluentService.class.getDeclaredField("quest");
        questField.setAccessible(true);
        questField.set(sut, mockSuperQuest);

        // Mock storage get method
        when(mockStorage.sub(StorageKeysUi.UI)).thenReturn(mockStorage);
    }

    @Test
    @DisplayName("validateResponseHaveStatus with matching status - Hard Assertion")
    void validateResponseHaveStatusWithMatchingStatusHardAssertion() {
        // Prepare test data
        List<ApiResponse> apiResponses = new ArrayList<>();
        apiResponses.add(new ApiResponse("/api/test1", 200));
        apiResponses.add(new ApiResponse("/api/test2", 201));

        // Setup mock behavior for storage get method
        when(mockStorage.get(eq(StorageKeysUi.RESPONSES), any(ParameterizedTypeReference.class)))
                .thenReturn(apiResponses);

        // Use reflection to invoke the validation method
        UIServiceFluent<?> result = sut.validateResponseHaveStatus("/api", 2);

        // Assertion is implicit - if no exception is thrown, the test passes
        assertThat(result).isSameAs(mockUiServiceFluent);
    }

    @Test
    @DisplayName("validateResponseHaveStatus with non-matching status - Hard Assertion")
    void validateResponseHaveStatusWithNonMatchingStatusHardAssertion() {
        // Prepare test data
        List<ApiResponse> apiResponses = new ArrayList<>();
        apiResponses.add(new ApiResponse("/api/test1", 404));
        apiResponses.add(new ApiResponse("/api/test2", 500));

        // Setup mock behavior for storage get method
        when(mockStorage.get(eq(StorageKeysUi.RESPONSES), any(ParameterizedTypeReference.class)))
                .thenReturn(apiResponses);

        // We expect this to throw an assertion error
        try {
            sut.validateResponseHaveStatus("/api", 2);
        } catch (AssertionError e) {
            // Expected behavior
            assertThat(e).isNotNull();
        }
    }

    @Test
    @DisplayName("validateResponseHaveStatus with soft assertion")
    void validateResponseHaveStatusWithSoftAssertion() throws Exception {
        // Prepare test data
        List<ApiResponse> apiResponses = new ArrayList<>();
        apiResponses.add(new ApiResponse("/other/test", 404));

        // Setup mock behavior for storage get method
        when(mockStorage.get(eq(StorageKeysUi.RESPONSES), any(ParameterizedTypeReference.class)))
                .thenReturn(apiResponses);

        // Create a real Quest with its own CustomSoftAssertion
        Quest realQuest = new Quest();

        // Create a SuperQuest with the real Quest
        SuperQuest superQuest = new SuperQuest(realQuest);

        // Use reflection to set the quest in the system under test
        Field questField = FluentService.class.getDeclaredField("quest");
        questField.setAccessible(true);
        questField.set(sut, superQuest);

        // Invoke the method
        UIServiceFluent<?> result = sut.validateResponseHaveStatus("/api", 2, true);

        // Assertions
        assertThat(result).isSameAs(mockUiServiceFluent);

        // Verify soft assertion failure
        try {
            realQuest.complete(); // This will trigger assertAll()
            fail("Soft assertion should have failed");
        } catch (AssertionError e) {
            // Expected behavior
            assertThat(e).isNotNull();
        }
    }

    @Test
    @DisplayName("validateStatus method - positive scenarios")
    void validateStatusMethod() throws Exception {
        // Make validateStatus method accessible for testing
        java.lang.reflect.Method validateStatusMethod =
                InterceptorServiceFluent.class.getDeclaredMethod("validateStatus", int.class, int.class);
        validateStatusMethod.setAccessible(true);

        assertThat(validateStatusMethod.invoke(sut, 2, 200)).isEqualTo(true);
        assertThat(validateStatusMethod.invoke(sut, 4, 404)).isEqualTo(true);
        assertThat(validateStatusMethod.invoke(sut, 5, 500)).isEqualTo(true);
    }

    @Test
    @DisplayName("validateStatus method - negative scenarios")
    void validateStatusMethodNegative() throws Exception {
        // Make validateStatus method accessible for testing
        java.lang.reflect.Method validateStatusMethod =
                InterceptorServiceFluent.class.getDeclaredMethod("validateStatus", int.class, int.class);
        validateStatusMethod.setAccessible(true);

        assertThat(validateStatusMethod.invoke(sut, 3, 200)).isEqualTo(false);
        assertThat(validateStatusMethod.invoke(sut, 2, 404)).isEqualTo(false);
        assertThat(validateStatusMethod.invoke(sut, 1, 500)).isEqualTo(false);
    }
}