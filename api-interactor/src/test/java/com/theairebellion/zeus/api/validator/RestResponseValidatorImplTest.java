package com.theairebellion.zeus.api.validator;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.core.AssertionTarget;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import com.theairebellion.zeus.validator.exceptions.InvalidAssertionException;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestResponseValidator Implementation Tests")
class RestResponseValidatorImplTest {

    @Mock
    private Response responseMock;

    private RestResponseValidatorImpl validator;

    @BeforeEach
    void setup() {
        validator = new RestResponseValidatorImpl();
    }

    @Nested
    @DisplayName("Status Assertion Tests")
    class StatusAssertionTests {
        @Test
        @DisplayName("Validate response status code")
        void testValidateResponseStatus() {
            // Arrange
            Assertion statusAssertion = Assertion.builder()
                    .target(RestAssertionTarget.STATUS)
                    .key("AssertionKeyForStatus")
                    .type(AssertionTypes.IS)
                    .expected(200)
                    .build();

            when(responseMock.getStatusCode()).thenReturn(200);

            // Act & Assert
            assertNotNull(validator.validateResponse(responseMock, statusAssertion));
        }
    }

    @Nested
    @DisplayName("Body Assertion Tests")
    class BodyAssertionTests {
        @Test
        @DisplayName("Validate response body")
        void testValidateResponseBody() {
            // Arrange
            Assertion bodyAssertion = Assertion.builder()
                    .target(RestAssertionTarget.BODY)
                    .key("some.json.path")
                    .type(AssertionTypes.IS)
                    .expected("val")
                    .build();

            when(responseMock.jsonPath()).thenReturn(
                    new JsonPath("{\"some\":{\"json\":{\"path\":\"val\"}}}")
            );

            // Act & Assert
            List<AssertionResult<Object>> results = validator.validateResponse(responseMock, bodyAssertion);
            assertEquals(1, results.size(), "The size of the list of results is not correct");
            assertTrue(results.get(0).isPassed(), "Assertion should be pass");
        }

        @Test
        @DisplayName("Validate response body with null key")
        void testValidateResponseBodyNoKey() {
            // Arrange
            Assertion bodyAssertion = Assertion.builder()
                    .target(RestAssertionTarget.BODY)
                    .key(null)
                    .type(AssertionTypes.IS)
                    .expected("value")
                    .build();

            // Act & Assert
            assertThrows(InvalidAssertionException.class,
                    () -> validator.validateResponse(responseMock, bodyAssertion));
        }

        @Test
        @DisplayName("Validate response body with null path value")
        void testValidateResponseBodyNullPathValue() {
            // Arrange
            Assertion bodyAssertion = Assertion.builder()
                    .target(RestAssertionTarget.BODY)
                    .key("not.existing")
                    .type(AssertionTypes.IS)
                    .expected("someValue")
                    .build();

            when(responseMock.jsonPath()).thenReturn(new JsonPath("{}"));

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> validator.validateResponse(responseMock, bodyAssertion));
        }
    }

    @Nested
    @DisplayName("Header Assertion Tests")
    class HeaderAssertionTests {
        @Test
        @DisplayName("Validate response header")
        void testValidateResponseHeader() {
            // Arrange
            Assertion headerAssertion = Assertion.builder()
                    .target(RestAssertionTarget.HEADER)
                    .key("X-Something")
                    .type(AssertionTypes.IS)
                    .expected("Value")
                    .build();

            when(responseMock.getHeader("X-Something")).thenReturn("Value");

            // Act & Assert
            List<AssertionResult<Object>> results = validator.validateResponse(responseMock, headerAssertion);
            assertEquals(1, results.size(), "The size of the list of results is not correct");
            assertTrue(results.get(0).isPassed(), "Assertion should be pass");
        }

        @Test
        @DisplayName("Validate response header with null key")
        void testValidateResponseHeaderNoKey() {
            // Arrange
            Assertion headerAssertion = Assertion.builder()
                    .target(RestAssertionTarget.HEADER)
                    .key(null)
                    .type(AssertionTypes.IS)
                    .expected("value")
                    .build();

            // Act & Assert
            assertThrows(InvalidAssertionException.class,
                    () -> validator.validateResponse(responseMock, headerAssertion));
        }

        @Test
        @DisplayName("Validate response header with missing header")
        void testValidateResponseHeaderMissingHeader() {
            // Arrange
            Assertion headerAssertion = Assertion.builder()
                    .target(RestAssertionTarget.HEADER)
                    .key("X-NotThere")
                    .type(AssertionTypes.IS)
                    .expected("SomeValue")
                    .build();

            when(responseMock.getHeader("X-NotThere")).thenReturn(null);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> validator.validateResponse(responseMock, headerAssertion));
        }
    }

    @Nested
    @DisplayName("Invalid Target Assertion Tests")
    class InvalidTargetAssertionTests {
        @Test
        @DisplayName("Validate invalid assertion target throws exception")
        void testInvalidateAssertionTargetThrowsException() {
            // Arrange
            Assertion invalidAssertion = Assertion.builder()
                                            .target(AssertionTargetImpl.INVALID)
                                            .key("AssertionKeyForStatus")
                                            .type(AssertionTypes.IS)
                                            .expected(200)
                                            .build();

            assertThrows(InvalidAssertionException.class,
                () -> validator.validateResponse(responseMock, invalidAssertion));

        }


        private enum AssertionTargetImpl implements AssertionTarget{
            INVALID;


            @Override
            public Enum<?> target() {
                return this;
            }
        }

    }



}