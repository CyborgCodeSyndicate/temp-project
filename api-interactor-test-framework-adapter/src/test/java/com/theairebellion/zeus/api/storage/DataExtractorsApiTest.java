package com.theairebellion.zeus.api.storage;

import com.theairebellion.zeus.api.annotations.mock.TestEnum;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DataExtractorsApiTest {

    @Test
    void testResponseBodyExtraction() {
        var extractor = DataExtractorsApi.responseBodyExtraction(TestEnum.API_RESPONSE, "some.json.path");

        var resp = mock(Response.class);
        var body = mock(io.restassured.response.ResponseBody.class);
        when(resp.body()).thenReturn(body);
        when(body.jsonPath()).thenReturn(new JsonPath("{\"some\":{\"json\":{\"path\":\"extractedValue\"}}}"));

        assertEquals("extractedValue", extractor.extract(resp));
    }

    @Test
    void testStatusExtraction() {
        var extractor = DataExtractorsApi.statusExtraction(TestEnum.API_RESPONSE);
        var resp = mock(Response.class);
        when(resp.statusCode()).thenReturn(201);

        assertEquals(201, extractor.extract(resp));
    }
}