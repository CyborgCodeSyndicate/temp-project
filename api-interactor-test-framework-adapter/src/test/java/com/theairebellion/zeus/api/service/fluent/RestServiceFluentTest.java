package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.api.mock.TestAuthClient;
import com.theairebellion.zeus.api.mock.TestEnum;
import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.api.mock.StorageDouble;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.util.reflections.RetryUtils;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestServiceFluent Tests")
class RestServiceFluentTest {

    @Mock
    private RestService restService;

    private RestServiceFluent restFluent;

    @Mock
    private Endpoint endpoint;

    @Mock
    private Response response;

    private StorageDouble storageDouble;

    @BeforeEach
    void setUp() throws Exception {
        // Create the RestServiceFluent manually to avoid Mockito issues
        restFluent = new RestServiceFluent(restService);

        // Set up Quest and storage
        var realQuest = new Quest();
        var realSuperQuest = new SuperQuest(realQuest);
        storageDouble = new StorageDouble();

        var questField = RestServiceFluent.class.getSuperclass().getDeclaredField("quest");
        questField.setAccessible(true);
        questField.set(restFluent, realSuperQuest);

        var storageField = Quest.class.getDeclaredField("storage");
        storageField.setAccessible(true);
        storageField.set(realQuest, storageDouble);
    }

    @Nested
    @DisplayName("Request Methods")
    class RequestMethodTests {

        @Test
        @DisplayName("Request without body should store response in storage")
        void requestWithoutBodyShouldStoreResponseInStorage() {
            // Arrange
            doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();
            when(restService.request(endpoint)).thenReturn(response);

            // Act
            var result = restFluent.request(endpoint);

            // Assert
            assertThat(result).isSameAs(restFluent);
            verify(restService).request(endpoint);
            assertThat(storageDouble.subStorage).containsEntry(TestEnum.MOCK_ENDPOINT, response);
        }

        @Test
        @DisplayName("Request with body should store response in storage")
        void requestWithBodyShouldStoreResponseInStorage() {
            // Arrange
            doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();
            var body = "someBody";
            when(restService.request(endpoint, body)).thenReturn(response);

            // Act
            var result = restFluent.request(endpoint, body);

            // Assert
            assertThat(result).isSameAs(restFluent);
            verify(restService).request(endpoint, body);
            assertThat(storageDouble.subStorage).containsEntry(TestEnum.MOCK_ENDPOINT, response);
        }
    }

    @Nested
    @DisplayName("Validation Methods")
    class ValidationMethodTests {

        @Test
        @DisplayName("ValidateResponse should call validate on RestService")
        void validateResponseShouldCallValidateOnRestService() {
            // Arrange
            List<AssertionResult<Object>> mockResults = new ArrayList<>();
            when(restService.validate(eq(response), any(Assertion[].class))).thenReturn(mockResults);

            // Act
            var result = restFluent.validateResponse(response);

            // Assert
            assertThat(result).isSameAs(restFluent);
            verify(restService).validate(eq(response), any(Assertion[].class));
        }

        @Test
        @DisplayName("ValidateResponse with specific assertions should call validate")
        void validateResponseWithSpecificAssertions() {
            // Arrange
            List<AssertionResult<Object>> mockResults = new ArrayList<>();
            Assertion[] assertions = new Assertion[0];
            when(restService.validate(response, assertions)).thenReturn(mockResults);

            // Act
            var result = restFluent.validateResponse(response, assertions);

            // Assert
            assertThat(result).isSameAs(restFluent);
            verify(restService).validate(response, assertions);
        }

        @Test
        @DisplayName("Validate with Runnable should return same instance")
        void validateWithRunnableShouldReturnSameInstance() {
            // Arrange
            var assertion = mock(Runnable.class);

            // Act
            var result = restFluent.validate(assertion);

            // Assert
            assertThat(result).isSameAs(restFluent);
        }

        @SuppressWarnings("unchecked")
        @Test
        @DisplayName("Validate with Consumer should return same instance")
        void validateWithConsumerShouldReturnSameInstance() {
            // Arrange
            Consumer<SoftAssertions> consumer = mock(Consumer.class);

            // Act
            var result = restFluent.validate(consumer);

            // Assert
            assertThat(result).isSameAs(restFluent);
        }
    }

    @Nested
    @DisplayName("Request and Validate Methods")
    class RequestAndValidateMethodTests {

        @Test
        @DisplayName("RequestAndValidate without body should call request and validate")
        void requestAndValidateWithoutBodyShouldCallRequestAndValidate() {
            // Arrange
            doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();
            List<AssertionResult<Object>> mockResults = new ArrayList<>();
            when(restService.request(endpoint)).thenReturn(response);
            when(restService.validate(eq(response), any(Assertion[].class))).thenReturn(mockResults);

            // Act
            var result = restFluent.requestAndValidate(endpoint);

            // Assert
            assertThat(result).isSameAs(restFluent);
            verify(restService).request(endpoint);
            verify(restService).validate(eq(response), any(Assertion[].class));
            assertThat(storageDouble.subStorage).containsEntry(TestEnum.MOCK_ENDPOINT, response);
        }

        @Test
        @DisplayName("RequestAndValidate with body should call request and validate")
        void requestAndValidateWithBodyShouldCallRequestAndValidate() {
            // Arrange
            doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();
            var body = "body";
            List<AssertionResult<Object>> mockResults = new ArrayList<>();
            when(restService.request(endpoint, body)).thenReturn(response);
            when(restService.validate(eq(response), any())).thenReturn(mockResults);

            // Act
            var result = restFluent.requestAndValidate(endpoint, body, (Assertion[]) null);

            // Assert
            assertThat(result).isSameAs(restFluent);
            verify(restService).request(endpoint, body);
            verify(restService).validate(eq(response), any());
            assertThat(storageDouble.subStorage).containsEntry(TestEnum.MOCK_ENDPOINT, response);
        }

        @Test
        @DisplayName("RequestAndValidate with specific assertions should use them")
        void requestAndValidateWithSpecificAssertions() {
            // Arrange
            doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();
            List<AssertionResult<Object>> mockResults = new ArrayList<>();
            Assertion[] assertions = new Assertion[0];

            when(restService.request(endpoint)).thenReturn(response);
            when(restService.validate(response, assertions)).thenReturn(mockResults);

            // Act
            var result = restFluent.requestAndValidate(endpoint, assertions);

            // Assert
            assertThat(result).isSameAs(restFluent);
            verify(restService).request(endpoint);
            verify(restService).validate(response, assertions);
            assertThat(storageDouble.subStorage).containsEntry(TestEnum.MOCK_ENDPOINT, response);
        }
    }

    @Nested
    @DisplayName("Authentication Methods")
    class AuthenticationTests {

        @Test
        @DisplayName("Authenticate should call authenticate on RestService")
        void authenticateShouldCallAuthenticateOnRestService() {
            // Act
            var result = restFluent.authenticate("user", "pass", TestAuthClient.class);

            // Assert
            assertThat(result).isSameAs(restFluent);
            verify(restService).authenticate("user", "pass", TestAuthClient.class);
        }
    }

    @Nested
    @DisplayName("Getter Methods")
    class GetterTests {

        @Test
        @DisplayName("GetRestService should return the RestService instance")
        void getRestServiceShouldReturnRestServiceInstance() {
            // Act
            var result = restFluent.getRestService();

            // Assert
            assertThat(result).isSameAs(restService);
        }
    }

    @Nested
    @DisplayName("Retry Methods")
    class RetryTests {

        @Test
        @DisplayName("RetryUntil should delegate to RetryUtils")
        void retryUntilShouldDelegateToRetryUtils() {
            // Arrange
            @SuppressWarnings("unchecked")
            RetryCondition<Integer> retryCondition = mock(RetryCondition.class);
            @SuppressWarnings("unchecked")
            Predicate<Integer> condition = mock(Predicate.class);
            lenient().when(retryCondition.condition()).thenReturn(condition);

            Duration maxWait = Duration.ofSeconds(5);
            Duration retryInterval = Duration.ofMillis(500);

            try (MockedStatic<RetryUtils> retryUtils = mockStatic(RetryUtils.class)) {
                // Act
                var result = restFluent.retryUntil(retryCondition, maxWait, retryInterval);

                // Assert
                assertThat(result).isSameAs(restFluent);
                retryUtils.verify(() -> RetryUtils.retryUntil(
                        eq(maxWait),
                        eq(retryInterval),
                        any(),
                        eq(condition)
                ));
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("ValidateResponse with null assertions should not fail")
        void validateResponseWithNullAssertionsShouldNotFail() {
            // Arrange
            List<AssertionResult<Object>> mockResults = new ArrayList<>();
            when(restService.validate(eq(response), any())).thenReturn(mockResults);

            // Act
            var result = restFluent.validateResponse(response, (Assertion[]) null);

            // Assert
            assertThat(result).isSameAs(restFluent);
            verify(restService).validate(eq(response), any());
        }

        @Test
        @DisplayName("RequestAndValidate with null body should handle null properly")
        void requestAndValidateWithNullBodyShouldHandleNullProperly() {
            // Arrange
            doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();
            List<AssertionResult<Object>> mockResults = new ArrayList<>();
            when(restService.request(endpoint, null)).thenReturn(response);
            when(restService.validate(eq(response), any())).thenReturn(mockResults);

            // Act
            var result = restFluent.requestAndValidate(endpoint, null, (Assertion[]) null);

            // Assert
            assertThat(result).isSameAs(restFluent);
            verify(restService).request(endpoint, null);
            verify(restService).validate(eq(response), any());
        }
    }
}