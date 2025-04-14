package com.theairebellion.zeus.api.annotations;

import com.theairebellion.zeus.api.annotations.mock.TestAuthClient;
import com.theairebellion.zeus.api.annotations.mock.TestCreds;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.authentication.Credentials;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticateViaApiAs Annotation Tests")
class AuthenticateViaApiAsAnnotationTest {

   @Nested
   @DisplayName("Annotation Metadata Tests")
   class AnnotationMetadataTests {

      @Test
      @DisplayName("Should have runtime retention policy")
      void shouldHaveRuntimeRetentionPolicy() {
         // Get the annotation class
         Class<AuthenticateViaApiAs> annotationClass = AuthenticateViaApiAs.class;

         // Verify retention policy
         Retention retention = annotationClass.getAnnotation(Retention.class);
         assertThat(retention).isNotNull();
         assertThat(retention.value()).isEqualTo(RetentionPolicy.RUNTIME);
      }

      @Test
      @DisplayName("Should target method elements only")
      void shouldTargetMethodElementsOnly() {
         // Get the annotation class
         Class<AuthenticateViaApiAs> annotationClass = AuthenticateViaApiAs.class;

         // Verify target
         Target target = annotationClass.getAnnotation(Target.class);
         assertThat(target).isNotNull();
         assertThat(target.value()).containsExactly(ElementType.METHOD);
      }
   }

   @Nested
   @DisplayName("Annotation Usage Tests")
   class AnnotationUsageTests {

      static Stream<Arguments> provideAnnotationConfigurations() {
         return Stream.of(
               Arguments.of("methodWithDefaultCaching", TestCreds.class, TestAuthClient.class, false),
               Arguments.of("methodWithExplicitCaching", TestCreds.class, TestAuthClient.class, true)
         );
      }

      @AuthenticateViaApiAs(credentials = TestCreds.class, type = TestAuthClient.class)
      void methodWithDefaultCaching() {
         // Method used for testing annotation configuration
      }

      @AuthenticateViaApiAs(credentials = TestCreds.class, type = TestAuthClient.class, cacheCredentials = true)
      void methodWithExplicitCaching() {
         // Method used for testing annotation configuration
      }

      @ParameterizedTest(name = "Method {0} should have credentials={1}, type={2}, cacheCredentials={3}")
      @MethodSource("provideAnnotationConfigurations")
      @DisplayName("Should correctly configure annotation attributes")
      void shouldCorrectlyConfigureAnnotationAttributes(String methodName,
                                                        Class<? extends Credentials> expectedCredentials,
                                                        Class<? extends BaseAuthenticationClient> expectedType,
                                                        boolean expectedCacheCredentials) throws NoSuchMethodException {
         // Get the method
         Method method = this.getClass().getDeclaredMethod(methodName);

         // Get annotation from method
         AuthenticateViaApiAs annotation = method.getAnnotation(AuthenticateViaApiAs.class);

         // Assertions using AssertJ
         assertThat(annotation).isNotNull();
         assertThat(annotation.credentials()).isEqualTo(expectedCredentials);
         assertThat(annotation.type()).isEqualTo(expectedType);
         assertThat(annotation.cacheCredentials()).isEqualTo(expectedCacheCredentials);
      }
   }
}