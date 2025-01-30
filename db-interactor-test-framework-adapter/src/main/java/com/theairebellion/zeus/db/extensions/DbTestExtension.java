package com.theairebellion.zeus.db.extensions;

import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Order(Integer.MAX_VALUE)
@Component
public class DbTestExtension implements AfterAllCallback {


    @Override
    public void afterAll(ExtensionContext context) {
        ApplicationContext appCtx = SpringExtension.getApplicationContext(context);
        BaseDbConnectorService baseDbConnectorService = appCtx.getBean(BaseDbConnectorService.class);
        baseDbConnectorService.closeConnections();
    }

}
