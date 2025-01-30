package com.theairebellion.zeus.api.storage;

import com.theairebellion.zeus.api.annotations.mock.TestEnum;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataExtractorsApiTest {

    @Test
    void testResponseBodyExtraction() {
        // 1) create the data extractor
        var extractor = DataExtractorsApi.responseBodyExtraction(TestEnum.API_RESPONSE, "some.json.path");

        // 2) mock response
        Response resp = mock(Response.class);
        when(resp.body()).thenReturn(mock(io.restassured.response.ResponseBody.class));
        // or we can do a real JsonPath if we want
        // let's do partial stubbing:
        when(resp.body().jsonPath()).thenReturn(new JsonPath("{\"some\":{\"json\":{\"path\":\"extractedValue\"}}}"));

        // 3) do extraction
        Object extracted = extractor.extract(resp);
        assertEquals("extractedValue", extracted);
    }

    @Test
    void testStatusExtraction() {
        var extractor = DataExtractorsApi.statusExtraction(TestEnum.API_RESPONSE);
        Response resp = mock(Response.class);
        when(resp.statusCode()).thenReturn(201);

        Object extracted = extractor.extract(resp);
        assertEquals(201, extracted);
    }
}