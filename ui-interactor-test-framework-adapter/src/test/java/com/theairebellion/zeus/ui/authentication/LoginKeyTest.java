package com.theairebellion.zeus.ui.authentication;

import com.theairebellion.zeus.ui.service.fluent.UiServiceFluent;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginKeyTest {

   private static final String USERNAME = "testUser";
   private static final String PASSWORD = "testPass";
   private static final Class<? extends BaseLoginClient> CLIENT_TYPE = TestLoginClient.class;

   @Test
   void shouldCreateLoginKeyWithCorrectValues() {
      // When
      LoginKey loginKey = new LoginKey(USERNAME, PASSWORD, CLIENT_TYPE);

      // Then
      assertEquals(USERNAME, loginKey.getUsername());
      assertEquals(PASSWORD, loginKey.getPassword());
      assertEquals(CLIENT_TYPE, loginKey.getType());
   }

   @Test
   void shouldBeEqualWhenAllFieldsMatch() {
      // Given
      LoginKey loginKey1 = new LoginKey(USERNAME, PASSWORD, CLIENT_TYPE);
      LoginKey loginKey2 = new LoginKey(USERNAME, PASSWORD, CLIENT_TYPE);

      // Then
      assertEquals(loginKey1, loginKey2);
      assertEquals(loginKey1.hashCode(), loginKey2.hashCode());
   }

   @Test
   void shouldNotBeEqualWhenUsernameDiffers() {
      // Given
      LoginKey loginKey1 = new LoginKey(USERNAME, PASSWORD, CLIENT_TYPE);
      LoginKey loginKey2 = new LoginKey("differentUser", PASSWORD, CLIENT_TYPE);

      // Then
      assertNotEquals(loginKey1, loginKey2);
      assertNotEquals(loginKey1.hashCode(), loginKey2.hashCode());
   }

   @Test
   void shouldNotBeEqualWhenPasswordDiffers() {
      // Given
      LoginKey loginKey1 = new LoginKey(USERNAME, PASSWORD, CLIENT_TYPE);
      LoginKey loginKey2 = new LoginKey(USERNAME, "differentPass", CLIENT_TYPE);

      // Then
      assertNotEquals(loginKey1, loginKey2);
      assertNotEquals(loginKey1.hashCode(), loginKey2.hashCode());
   }

   @Test
   void shouldNotBeEqualWhenTypeDiffers() {
      // Given
      LoginKey loginKey1 = new LoginKey(USERNAME, PASSWORD, CLIENT_TYPE);
      LoginKey loginKey2 = new LoginKey(USERNAME, PASSWORD, OtherLoginClient.class);

      // Then
      assertNotEquals(loginKey1, loginKey2);
      assertNotEquals(loginKey1.hashCode(), loginKey2.hashCode());
   }

   @Test
   void shouldHandleNullFieldsCorrectly() {
      // Given
      LoginKey loginKey1 = new LoginKey(null, PASSWORD, CLIENT_TYPE);
      LoginKey loginKey2 = new LoginKey(null, PASSWORD, CLIENT_TYPE);
      LoginKey loginKey3 = new LoginKey(USERNAME, null, CLIENT_TYPE);
      LoginKey loginKey4 = new LoginKey(USERNAME, PASSWORD, null);

      // Then
      assertEquals(loginKey1, loginKey2);
      assertNotEquals(loginKey1, loginKey3);
      assertNotEquals(loginKey3, loginKey4);
   }

   @Test
   void shouldGenerateToStringWithAllFields() {
      // Given
      LoginKey loginKey = new LoginKey(USERNAME, PASSWORD, CLIENT_TYPE);

      // When
      String toString = loginKey.toString();

      // Then
      assertTrue(toString.contains(USERNAME));
      assertTrue(toString.contains(PASSWORD));
      assertTrue(toString.contains(CLIENT_TYPE.getSimpleName()));
   }

   @Test
   void shouldSupportSetterMethods() {
      // Given
      LoginKey loginKey = new LoginKey(USERNAME, PASSWORD, CLIENT_TYPE);

      // When
      loginKey.setUsername("newUsername");
      loginKey.setPassword("newPassword");
      loginKey.setType(OtherLoginClient.class);

      // Then
      assertEquals("newUsername", loginKey.getUsername());
      assertEquals("newPassword", loginKey.getPassword());
      assertEquals(OtherLoginClient.class, loginKey.getType());
   }

   // Test classes for type comparison
   private static class TestLoginClient extends BaseLoginClient {
      @Override
      protected <T extends UiServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
         // Mock implementation
      }

      @Override
      protected By successfulLoginElementLocator() {
         return By.id("successElement");
      }
   }

   private static class OtherLoginClient extends BaseLoginClient {
      @Override
      protected <T extends UiServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
         // Mock implementation
      }

      @Override
      protected By successfulLoginElementLocator() {
         return By.id("otherElement");
      }
   }
}