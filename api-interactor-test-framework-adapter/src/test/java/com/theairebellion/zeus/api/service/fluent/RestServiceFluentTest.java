package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.api.annotations.mock.TestAuthClient;
import com.theairebellion.zeus.api.annotations.mock.TestEnum;
import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

/**
 * Example test for RestServiceFluent.
 */
class RestServiceFluentTest {

    private RestService restServiceMock;
    private RestServiceFluent restFluent;

    @BeforeEach
    void setUp() throws Exception {
        restServiceMock = mock(RestService.class);

        // 2) create the class under test
        restFluent = new RestServiceFluent(restServiceMock);

        // 3) reflect to set 'quest' in RestServiceFluent's superclass (FluentService)
        Quest realQuest = new Quest(); // a real Quest instance

        Field questField = RestServiceFluent.class
                .getSuperclass() // class FluentService
                .getDeclaredField("quest");
        questField.setAccessible(true);
        questField.set(restFluent, realQuest);

        // 4) Now realQuest has a private final field "storage". We replace it with StorageDouble.
        Field storageField = Quest.class.getDeclaredField("storage");
        storageField.setAccessible(true);
        storageField.set(realQuest, new StorageDouble());
    }

    @Test
    void testRequest_WithoutBody() {
        Response mockResponse = mock(Response.class);
        when(restServiceMock.request(any(Endpoint.class))).thenReturn(mockResponse);

        Endpoint endpoint = mock(Endpoint.class);
        // cast needed since endpoint.enumImpl() returns Enum<?>
        doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();

        // ACT
        restFluent.request(endpoint);

        // ASSERT
        verify(restServiceMock).request(endpoint);

        // Check that the storage in quest got the result
        StorageDouble storage = (StorageDouble) getStorageFromFluent();
        assertEquals(mockResponse, storage.subStorage.get(TestEnum.MOCK_ENDPOINT),
                "Should have stored the mockResponse under the enum key");
    }

    @Test
    void testRequest_WithBody() {
        Response mockResponse = mock(Response.class);
        when(restServiceMock.request(any(Endpoint.class), any())).thenReturn(mockResponse);

        Endpoint endpoint = mock(Endpoint.class);
        doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();

        restFluent.request(endpoint, "someBody");

        verify(restServiceMock).request(endpoint, "someBody");

        StorageDouble storage = (StorageDouble) getStorageFromFluent();
        assertEquals(mockResponse, storage.subStorage.get(TestEnum.MOCK_ENDPOINT));
    }

    @Test
    void testValidateResponse() {
        // mock the response
        Response response = mock(Response.class);
        // mock the assertion results
        @SuppressWarnings("unchecked")
        List<AssertionResult<Object>> mockResults = mock(List.class);
        when(restServiceMock.validate(eq(response), any(Assertion[].class))).thenReturn(mockResults);

        RestServiceFluent result = restFluent.validateResponse(response);
        assertSame(restFluent, result);
        verify(restServiceMock).validate(eq(response), any(Assertion[].class));
    }

    @Test
    void testRequestAndValidate_WithoutBody() {
        Response mockResponse = mock(Response.class);
        when(restServiceMock.request(any(Endpoint.class))).thenReturn(mockResponse);

        List<AssertionResult<Object>> mockAssertionResults = new ArrayList<>();
        when(restServiceMock.validate(eq(mockResponse), any(Assertion[].class))).thenReturn(mockAssertionResults);

        Endpoint endpoint = mock(Endpoint.class);
        doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();

        // Run the method under test
        RestServiceFluent result = restFluent.requestAndValidate(endpoint);

        // Verify interactions
        assertSame(restFluent, result);
        verify(restServiceMock).request(endpoint);
        verify(restServiceMock).validate(eq(mockResponse), any(Assertion[].class));
        verifyNoMoreInteractions(restServiceMock);

        // Verify data stored in storage
        StorageDouble storage = (StorageDouble) getStorageFromFluent();
        assertEquals(mockResponse, storage.subStorage.get(TestEnum.MOCK_ENDPOINT));
    }

    @Test
    void testRequestAndValidate_WithBody() {
        Response response = mock(Response.class);
        when(restServiceMock.request(any(Endpoint.class), any())).thenReturn(response);
        when(restServiceMock.validate(eq(response), any())).thenReturn(new ArrayList<>());

        Endpoint endpoint = mock(Endpoint.class);
        doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();

        RestServiceFluent result = restFluent.requestAndValidate(endpoint, "body", (Assertion<?>[]) null);
        assertSame(restFluent, result);
        verify(restServiceMock).request(endpoint, "body");
        verify(restServiceMock).validate(eq(response), any());
    }

    @Test
    void testAuthenticate() {
        // call
        restFluent.authenticate("user", "pass", TestAuthClient.class);
        // verify
        verify(restServiceMock).authenticate("user", "pass", TestAuthClient.class);
    }

    @Test
    void testValidateRunnable() {
        Runnable assertion = mock(Runnable.class);
        RestServiceFluent result = restFluent.validate(assertion);
        assertSame(restFluent, result);
    }

    @Test
    void testValidateConsumerOfSoftAssertions() {
        var consumer = mock(Consumer.class);
        RestServiceFluent result = restFluent.validate(consumer);
        assertSame(restFluent, result);
    }

    private Storage getStorageFromFluent() {
        try {
            // reflect again: get the 'quest' from restFluent
            Field questField = RestServiceFluent.class
                    .getSuperclass()
                    .getDeclaredField("quest");
            questField.setAccessible(true);
            Quest q = (Quest) questField.get(restFluent);

            // from the real Quest, reflect "storage" again
            Field storageField = Quest.class.getDeclaredField("storage");
            storageField.setAccessible(true);
            return (Storage) storageField.get(q);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // A minimal storage that just stores sub(API).put() calls in a map
    private static class StorageDouble extends Storage {
        // store sub calls in a map
        java.util.Map<Object,Object> subStorage = new java.util.HashMap<>();

        @Override
        public Storage sub(Enum<?> key) {
            return this; // always return same sub
        }
        @Override
        public <T> void put(Enum<?> key, T value) {
            subStorage.put(key, value);
        }
    }

}
