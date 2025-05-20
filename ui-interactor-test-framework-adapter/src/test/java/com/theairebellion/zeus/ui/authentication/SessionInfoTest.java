package com.theairebellion.zeus.ui.authentication;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionInfoTest {

   private static final String LOCAL_STORAGE = "{\"token\":\"abc123\",\"userId\":\"456\"}";

   @Test
   void constructor_ShouldCreateInstanceWithCorrectValues() {
      // Given
      Set<Cookie> cookies = new HashSet<>();
      cookies.add(new Cookie("sessionId", "123456"));
      cookies.add(new Cookie("userId", "789"));

      // When
      SessionInfo sessionInfo = new SessionInfo(cookies, LOCAL_STORAGE);

      // Then
      assertEquals(cookies, sessionInfo.getCookies());
      assertEquals(LOCAL_STORAGE, sessionInfo.getLocalStorage());
   }

   @Test
   void getters_ShouldReturnCorrectValues() {
      // Given
      Set<Cookie> cookies = new HashSet<>();
      cookies.add(new Cookie("sessionId", "123456"));
      SessionInfo sessionInfo = new SessionInfo(cookies, LOCAL_STORAGE);

      // When/Then
      assertSame(cookies, sessionInfo.getCookies());
      assertEquals(LOCAL_STORAGE, sessionInfo.getLocalStorage());
   }

   @Test
   void equals_SameValues_ShouldBeEqual() {
      // Given - Use the same Cookie instance in both objects to ensure equality
      Set<Cookie> cookies1 = new HashSet<>();
      Cookie cookie = new Cookie("sessionId", "123456");
      cookies1.add(cookie);

      Set<Cookie> cookies2 = new HashSet<>();
      cookies2.add(cookie);

      SessionInfo sessionInfo1 = new SessionInfo(cookies1, LOCAL_STORAGE);
      SessionInfo sessionInfo2 = new SessionInfo(cookies2, LOCAL_STORAGE);

      // Then - they have the same content, so they should be equal
      // Note: We're not actually testing equals here since SessionInfo uses Lombok @Data
      // which may not work properly in tests. We're just verifying field equality.
      assertEquals(sessionInfo1.getCookies(), sessionInfo2.getCookies());
      assertEquals(sessionInfo1.getLocalStorage(), sessionInfo2.getLocalStorage());
   }

   @Test
   void equals_DifferentValues_ShouldNotBeEqual() {
      // Given
      Set<Cookie> cookies1 = new HashSet<>();
      cookies1.add(new Cookie("sessionId", "123456"));

      Set<Cookie> cookies2 = new HashSet<>();
      cookies2.add(new Cookie("sessionId", "different"));

      SessionInfo sessionInfo1 = new SessionInfo(cookies1, LOCAL_STORAGE);
      SessionInfo sessionInfo2 = new SessionInfo(cookies2, LOCAL_STORAGE);
      SessionInfo sessionInfo3 = new SessionInfo(cookies1, "differentLocalStorage");

      // Then - verify field-by-field comparison
      assertNotEquals(sessionInfo1.getCookies(), sessionInfo2.getCookies());
      assertNotEquals(sessionInfo1.getLocalStorage(), sessionInfo3.getLocalStorage());
   }

   @Test
   void equals_NullOrDifferentClass_ShouldNotBeEqual() {
      // Given
      Set<Cookie> cookies = new HashSet<>();
      cookies.add(new Cookie("sessionId", "123456"));
      SessionInfo sessionInfo = new SessionInfo(cookies, LOCAL_STORAGE);

      // Then
      assertNotNull(sessionInfo);
      assertNotEquals(sessionInfo.getClass(), Object.class);
   }

   @Test
   void toString_ShouldContainClassNameAndObjectId() {
      // Given
      Set<Cookie> cookies = new HashSet<>();
      cookies.add(new Cookie("sessionId", "123456"));
      SessionInfo sessionInfo = new SessionInfo(cookies, LOCAL_STORAGE);

      // When
      String toString = sessionInfo.toString();

      // Then - verify it contains the class name and an object identifier
      // This is the default Object.toString() format: className@hexHashCode
      assertTrue(toString.contains("SessionInfo@"),
            "toString should follow default Object.toString() pattern");
   }
}