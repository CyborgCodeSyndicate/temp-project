package com.theairebellion.zeus.db.extensions;

import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DbTestExtensionTest {

   @Mock
   private ExtensionContext mockExtensionContext;

   @Mock
   private ApplicationContext mockApplicationContext;

   @Mock
   private BaseDbConnectorService mockConnectorService;

   @Test
   @DisplayName("afterAll should close all database connections")
   void afterAllShouldCloseAllConnections() {
      // Given
      DbTestExtension extension = new DbTestExtension();

      when(mockApplicationContext.getBean(BaseDbConnectorService.class)).thenReturn(mockConnectorService);

      // When
      try (MockedStatic<SpringExtension> springExtensionMock = mockStatic(SpringExtension.class)) {
         springExtensionMock.when(() -> SpringExtension.getApplicationContext(mockExtensionContext))
               .thenReturn(mockApplicationContext);

         extension.afterAll(mockExtensionContext);

         // Then
         verify(mockApplicationContext).getBean(BaseDbConnectorService.class);
         verify(mockConnectorService).closeConnections();
      }
   }

   @Test
   @DisplayName("DbTestExtension should have proper annotations")
   void testDbTestExtensionAnnotations() {
      // Given
      Class<DbTestExtension> extensionClass = DbTestExtension.class;

      // When
      Order orderAnnotation = extensionClass.getAnnotation(Order.class);
      Component componentAnnotation = extensionClass.getAnnotation(Component.class);

      // Then
      assertThat(orderAnnotation)
            .as("@Order annotation should be present")
            .isNotNull();
      assertThat(orderAnnotation.value())
            .as("@Order should have Integer.MAX_VALUE")
            .isEqualTo(Integer.MAX_VALUE);

      assertThat(componentAnnotation)
            .as("@Component annotation should be present")
            .isNotNull();
   }
}