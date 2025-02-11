package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.api.annotations.mock.TestAuthClient;
import com.theairebellion.zeus.api.annotations.mock.TestEnum;
import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.api.service.fluent.mock.StorageDouble;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestServiceFluentTest {

    private RestService restServiceMock;
    private RestServiceFluent restFluent;
    private StorageDouble storageDouble;

    @BeforeEach
    void setUp() throws Exception {
        restServiceMock = mock(RestService.class);
        restFluent = new RestServiceFluent(restServiceMock);

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

    @Test
    void testRequest_WithoutBody() {
        var mockResponse = mock(Response.class);
        when(restServiceMock.request(any(Endpoint.class))).thenReturn(mockResponse);

        var endpoint = mock(Endpoint.class);
        doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();

        restFluent.request(endpoint);

        verify(restServiceMock).request(endpoint);
        assertEquals(mockResponse, storageDouble.subStorage.get(TestEnum.MOCK_ENDPOINT));
    }

    @Test
    void testRequest_WithBody() {
        var mockResponse = mock(Response.class);
        when(restServiceMock.request(any(Endpoint.class), any())).thenReturn(mockResponse);

        var endpoint = mock(Endpoint.class);
        doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();

        restFluent.request(endpoint, "someBody");

        verify(restServiceMock).request(endpoint, "someBody");
        assertEquals(mockResponse, storageDouble.subStorage.get(TestEnum.MOCK_ENDPOINT));
    }

    @Test
    void testValidateResponse() {
        var response = mock(Response.class);
        @SuppressWarnings("unchecked")
        var mockResults = mock(List.class);
        when(restServiceMock.validate(eq(response), any(Assertion[].class))).thenReturn(mockResults);

        var result = restFluent.validateResponse(response);
        assertSame(restFluent, result);
        verify(restServiceMock).validate(eq(response), any(Assertion[].class));
    }

    @Test
    void testRequestAndValidate_WithoutBody() {
        var mockResponse = mock(Response.class);
        when(restServiceMock.request(any(Endpoint.class))).thenReturn(mockResponse);
        var mockAssertionResults = new ArrayList<AssertionResult<Object>>();
        when(restServiceMock.validate(eq(mockResponse), any(Assertion[].class))).thenReturn(mockAssertionResults);

        var endpoint = mock(Endpoint.class);
        doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();

        var result = restFluent.requestAndValidate(endpoint);
        assertSame(restFluent, result);
        verify(restServiceMock).request(endpoint);
        verify(restServiceMock).validate(eq(mockResponse), any(Assertion[].class));
        verifyNoMoreInteractions(restServiceMock);

        assertEquals(mockResponse, storageDouble.subStorage.get(TestEnum.MOCK_ENDPOINT));
    }

    @Test
    void testRequestAndValidate_WithBody() {
        var response = mock(Response.class);
        when(restServiceMock.request(any(Endpoint.class), any())).thenReturn(response);
        when(restServiceMock.validate(eq(response), any())).thenReturn(new ArrayList<>());

        var endpoint = mock(Endpoint.class);
        doReturn(TestEnum.MOCK_ENDPOINT).when(endpoint).enumImpl();

        var result = restFluent.requestAndValidate(endpoint, "body", (Assertion<?>[]) null);
        assertSame(restFluent, result);
        verify(restServiceMock).request(endpoint, "body");
        verify(restServiceMock).validate(eq(response), any());
    }

    @Test
    void testAuthenticate() {
        restFluent.authenticate("user", "pass", TestAuthClient.class);
        verify(restServiceMock).authenticate("user", "pass", TestAuthClient.class);
    }

    @Test
    void testValidateRunnable() {
        var assertion = mock(Runnable.class);
        var result = restFluent.validate(assertion);
        assertSame(restFluent, result);
    }

    @Test
    void testValidateConsumerOfSoftAssertions() {
        var consumer = mock(Consumer.class);
        var result = restFluent.validate(consumer);
        assertSame(restFluent, result);
    }
}