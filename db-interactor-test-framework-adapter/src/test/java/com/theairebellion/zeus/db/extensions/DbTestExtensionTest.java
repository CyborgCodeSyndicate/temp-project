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
    void verifyAfterAllInvokesCloseConnections() {
        DbTestExtension extension = new DbTestExtension();
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        BaseDbConnectorService connectorService = mock(BaseDbConnectorService.class);

        when(applicationContext.getBean(BaseDbConnectorService.class)).thenReturn(connectorService);

        try (MockedStatic<SpringExtension> springExtensionMock = mockStatic(SpringExtension.class)) {
            springExtensionMock.when(() -> SpringExtension.getApplicationContext(extensionContext))
                    .thenReturn(applicationContext);

            extension.afterAll(extensionContext);

            verify(connectorService).closeConnections();
        }
    }
}