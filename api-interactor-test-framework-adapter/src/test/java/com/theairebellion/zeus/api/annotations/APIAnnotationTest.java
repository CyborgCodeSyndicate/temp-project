package com.theairebellion.zeus.api.annotations;

import com.theairebellion.zeus.api.extensions.ApiHookExtension;
import com.theairebellion.zeus.api.extensions.ApiTestExtension;
import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("API Annotation Tests")
class APIAnnotationTest {

    @Nested
    @DisplayName("Annotation Metadata Tests")
    class AnnotationMetadataTests {

        @Test
        @DisplayName("Should have runtime retention policy")
        void shouldHaveRuntimeRetentionPolicy() {
            // Get the annotation class
            Class<API> annotationClass = API.class;

            // Verify retention policy
            Retention retention = annotationClass.getAnnotation(Retention.class);
            assertThat(retention).isNotNull();
            assertThat(retention.value()).isEqualTo(RetentionPolicy.RUNTIME);
        }

        @Test
        @DisplayName("Should target type elements only")
        void shouldTargetTypeElementsOnly() {
            // Get the annotation class
            Class<API> annotationClass = API.class;

            // Verify target
            Target target = annotationClass.getAnnotation(Target.class);
            assertThat(target).isNotNull();
            assertThat(target.value()).containsExactly(ElementType.TYPE);
        }
    }

    @Nested
    @DisplayName("Framework Integration Tests")
    class FrameworkIntegrationTests {

        @Test
        @DisplayName("Should be configured with ApiTestExtension")
        void shouldBeConfiguredWithApiTestExtension() {
            // Verify ExtendWith
            ExtendWith extendWith = API.class.getAnnotation(ExtendWith.class);

            assertThat(extendWith).isNotNull();

            Class<?>[] extensions = extendWith.value();

            // Validate it includes both extensions
            List<Class<?>> extensionList = List.of(extensions);

            assertThat(extensionList)
                .contains(ApiTestExtension.class, ApiHookExtension.class)
                .withFailMessage("@API must include both ApiTestExtension and ApiHookExtension in @ExtendWith");
        }

        @Test
        @DisplayName("Should be configured as framework adapter with correct base packages")
        void shouldBeConfiguredAsFrameworkAdapterWithCorrectBasePackages() {
            // Get the annotation class
            Class<API> annotationClass = API.class;

            // Verify FrameworkAdapter
            FrameworkAdapter frameworkAdapter = annotationClass.getAnnotation(FrameworkAdapter.class);
            assertThat(frameworkAdapter).isNotNull();
            assertThat(frameworkAdapter.basePackages()).containsExactly("com.theairebellion.zeus.api");
        }
    }
}