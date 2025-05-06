package com.theairebellion.zeus.db.extensions;

import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JUnit 5 extension for managing database connections in tests.
 *
 * <p>This extension ensures that all database connections are properly closed
 * after all tests have been executed. It integrates with Spring's application
 * context to retrieve and manage database connections.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Order(Integer.MAX_VALUE)
@Component
public class DbTestExtension implements AfterAllCallback {

   /**
    * Closes all active database connections after all test executions.
    *
    * <p>This method retrieves the {@link BaseDbConnectorService} from the Spring
    * application context and invokes its connection cleanup mechanism.
    *
    * @param context The JUnit extension context.
    */
   @Override
   public void afterAll(ExtensionContext context) {
      ApplicationContext appCtx = SpringExtension.getApplicationContext(context);
      BaseDbConnectorService baseDbConnectorService = appCtx.getBean(BaseDbConnectorService.class);
      baseDbConnectorService.closeConnections();
   }
}
