package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.api.service.RestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SuperRestServiceFluent Tests")
class SuperRestServiceFluentTest {

   @Mock
   private RestServiceFluent mockRestServiceFluent;

   @Mock
   private RestService mockRestService;

   @Test
   @DisplayName("Constructor should initialize correctly and get RestService from original")
   void constructorShouldInitializeCorrectlyAndGetRestServiceFromOriginal() {
      // Arrange
      when(mockRestServiceFluent.getRestService()).thenReturn(mockRestService);

      // Act
      SuperRestServiceFluent superRestServiceFluent = new SuperRestServiceFluent(mockRestServiceFluent);

      // Assert
      assertThat(superRestServiceFluent.getRestService()).isSameAs(mockRestService);
   }

   @Test
   @DisplayName("GetRestService should delegate to original RestServiceFluent")
   void getRestServiceShouldDelegateToOriginalRestServiceFluent() {
      // Arrange
      when(mockRestServiceFluent.getRestService()).thenReturn(mockRestService);
      SuperRestServiceFluent superRestServiceFluent = new SuperRestServiceFluent(mockRestServiceFluent);
      clearInvocations(mockRestServiceFluent);

      // Act - Reset the mock to verify exactly one call
      when(mockRestServiceFluent.getRestService()).thenReturn(mockRestService);
      RestService result = superRestServiceFluent.getRestService();

      // Assert
      assertThat(result).isSameAs(mockRestService);
      verify(mockRestServiceFluent, times(1)).getRestService();
   }
}