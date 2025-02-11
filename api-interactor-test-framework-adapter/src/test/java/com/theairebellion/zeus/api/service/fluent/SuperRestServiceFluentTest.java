package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.api.service.RestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class SuperRestServiceFluentTest {

    private SuperRestServiceFluent superRestServiceFluent;
    private RestServiceFluent mockRestServiceFluent;
    private RestService mockRestService;

    @BeforeEach
    void setUp() {
        mockRestServiceFluent = mock(RestServiceFluent.class);
        mockRestService = mock(RestService.class);

        when(mockRestServiceFluent.getRestService()).thenReturn(mockRestService);

        superRestServiceFluent = new SuperRestServiceFluent(mockRestServiceFluent);
    }

    @Test
    void testConstructor_ShouldInitializeCorrectly() {
        assertSame(mockRestService, superRestServiceFluent.getRestService());
    }

    @Test
    void testGetRestService_ShouldReturnOriginalRestService() {
        RestService returnedService = superRestServiceFluent.getRestService();
        assertSame(mockRestService, returnedService);

        verify(mockRestServiceFluent, times(2)).getRestService();
    }
}