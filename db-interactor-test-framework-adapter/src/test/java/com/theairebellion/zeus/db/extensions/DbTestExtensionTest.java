package com.theairebellion.zeus.db.extensions;

import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

public class DbTestExtensionTest {

    @Test
    void testAfterAllCallsCloseConnections() {
        DbTestExtension extension = new DbTestExtension();
        ExtensionContext context = mock(ExtensionContext.class);
        ApplicationContext appCtx = mock(ApplicationContext.class);
        BaseDbConnectorService connector = mock(BaseDbConnectorService.class);
        when(appCtx.getBean(BaseDbConnectorService.class)).thenReturn(connector);

        try (MockedStatic<SpringExtension> springMock = mockStatic(SpringExtension.class)) {
            springMock.when(() -> SpringExtension.getApplicationContext(context)).thenReturn(appCtx);
            extension.afterAll(context);
            verify(connector).closeConnections();
        }
    }
}