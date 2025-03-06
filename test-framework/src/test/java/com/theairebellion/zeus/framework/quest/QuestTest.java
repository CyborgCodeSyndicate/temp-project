package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.mock.MockFluentService;
import com.theairebellion.zeus.framework.quest.mock.TestableQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.util.reflections.exceptions.ReflectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
class QuestTest {

    @InjectMocks
    private TestableQuest quest;

    private MockFluentService mockFluentService;

    @BeforeEach
    void setUp() {
        quest = new TestableQuest();
        mockFluentService = new MockFluentService();
        quest.exposeRegisterWorld(MockFluentService.class, mockFluentService);
    }

    @Nested
    @DisplayName("enters method tests")
    class EntersMethodTests {
        @Test
        @DisplayName("Should successfully enter existing world")
        void testEntersSuccess() {
            try (MockedStatic<LogTest> logMock = mockStatic(LogTest.class)) {
                // When
                MockFluentService result = quest.enters(MockFluentService.class);

                // Then
                assertSame(mockFluentService, result);
                logMock.verify(() -> LogTest.info(
                        "The quest has undertaken a journey through: 'MockWorld'"
                ));
            }
        }

        @Test
        @DisplayName("Should throw exception when world not initialized")
        void testEntersNoWorld() {
            // Given
            quest.exposeRemoveWorld(MockFluentService.class);

            // When/Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> quest.enters(MockFluentService.class)
            );
            assertEquals(
                    "World not initialized: " + MockFluentService.class.getName(),
                    exception.getMessage()
            );
        }

        @Test
        @DisplayName("Should handle world without TestService annotation")
        void testEntersWorldWithoutAnnotation() {
            // Given
            class NoAnnotationService extends FluentService {
            }
            quest.exposeRegisterWorld(NoAnnotationService.class, new NoAnnotationService());

            // When/Then
            try (MockedStatic<LogTest> logMock = mockStatic(LogTest.class)) {
                NoAnnotationService result = quest.enters(NoAnnotationService.class);

                assertNotNull(result);
                logMock.verify(() -> LogTest.info(
                        "The quest has undertaken a journey through: '" +
                                NoAnnotationService.class.getName() + "'"
                ));
            }
        }
    }

    @Nested
    @DisplayName("artifact method tests")
    class ArtifactMethodTests {
        @Test
        @DisplayName("Should successfully retrieve artifact")
        void testArtifactSuccess() {
            // When
            String value = quest.exposeArtifact(MockFluentService.class, String.class);

            // Then
            assertEquals("mockValue", value);
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should throw exception for null parameters")
        void testArtifactNullParameters(Class<?> nullParam) {
            // When/Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> quest.exposeArtifact(
                            nullParam == null ? null : MockFluentService.class,
                            nullParam == null ? String.class : null
                    )
            );
            assertEquals(
                    "Parameters worldType and artifactType must not be null.",
                    exception.getMessage()
            );
        }

        @Test
        @DisplayName("Should throw exception when world is not found")
        void testArtifactWorldNotFound() {
            // Given
            quest.exposeRemoveWorld(MockFluentService.class);

            // When/Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> quest.exposeArtifact(MockFluentService.class, String.class)
            );
            assertEquals(
                    "World not initialized: " + MockFluentService.class.getName(),
                    exception.getMessage()
            );
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when worldType is null")
        void testArtifactNullWorldType() {
            // When/Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> quest.exposeArtifact(null, String.class)
            );

            assertEquals(
                    "Parameters worldType and artifactType must not be null.",
                    exception.getMessage()
            );
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when artifactType is null")
        void testArtifactNullArtifactType() {
            // When/Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> quest.exposeArtifact(MockFluentService.class, null)
            );

            assertEquals(
                    "Parameters worldType and artifactType must not be null.",
                    exception.getMessage()
            );
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when no matching world is found for cast")
        void testArtifactNoMatchingWorld() {
            // Given
            quest.exposeRemoveWorld(MockFluentService.class);

            // Create a new service class to test
            class UnregisteredService extends FluentService {
                public String testField = "testValue";
            }

            // When/Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> quest.exposeArtifact(UnregisteredService.class, String.class)
            );

            assertEquals(
                    "World not initialized: " + UnregisteredService.class.getName(),
                    exception.getMessage()
            );
        }

        @Test
        @DisplayName("Should retrieve the most specific String field in inheritance hierarchy")
        void testFieldRetrievalOrder() {
            // Given
            class BaseMockService extends FluentService {
                public String baseField = "baseValue";
            }

            class MidMockService extends BaseMockService {
                public String midField = "midValue";
            }

            class SubMockService extends MidMockService {
                public String subField = "subValue";
            }

            // Register the subclass
            SubMockService subService = new SubMockService();
            quest.exposeRegisterWorld(SubMockService.class, subService);

            // When
            String retrievedField = quest.exposeArtifact(SubMockService.class, String.class);

            // Then
            assertEquals("subValue", retrievedField, "Should retrieve the most specific String field");
        }

        @Test
        @DisplayName("Should verify field type matching behavior")
        void testFieldTypeMatching() {
            // Given
            class ComplexService extends FluentService {
                public Number numberField = 42;
                public Integer integerField = 100;
                public String stringField = "testValue";
            }

            ComplexService service = new ComplexService();
            quest.exposeRegisterWorld(ComplexService.class, service);

            // When/Then - verify Number retrieval
            Number numberValue = quest.exposeArtifact(ComplexService.class, Number.class);
            assertEquals(42, numberValue, "Should retrieve first Number field");

            // When/Then - verify Integer retrieval
            Integer integerValue = quest.exposeArtifact(ComplexService.class, Integer.class);
            assertEquals(Integer.valueOf(100), integerValue, "Should retrieve Integer field");
        }

        @Test
        @DisplayName("Should handle multiple fields of same type")
        void testMultipleFieldsOfSameType() {
            // Given
            class MultiFieldService extends FluentService {
                public String firstStringField = "first";
                public String secondStringField = "second";
            }

            MultiFieldService service = new MultiFieldService();
            quest.exposeRegisterWorld(MultiFieldService.class, service);

            // When
            String retrievedField = quest.exposeArtifact(MultiFieldService.class, String.class);

            // Then
            assertEquals("first", retrievedField, "Should retrieve first String field in declaration order");
        }
    }

    @Nested
    @DisplayName("cast method tests")
    class CastMethodTests {
        @Test
        @DisplayName("Should successfully cast to world")
        void testCastSuccess() {
            // When
            MockFluentService result = quest.exposeCast(MockFluentService.class);

            // Then
            assertSame(mockFluentService, result);
        }

        @Test
        @DisplayName("Should throw exception when world not initialized")
        void testCastNoWorld() {
            // Given
            quest.exposeRemoveWorld(MockFluentService.class);

            // When/Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> quest.exposeCast(MockFluentService.class)
            );
            assertEquals(
                    "World not initialized: " + MockFluentService.class.getName(),
                    exception.getMessage()
            );
        }
    }

    @Nested
    @DisplayName("Complete method tests")
    class CompleteMethodTests {
        @Test
        @DisplayName("Should complete quest and clear quest holder")
        void testComplete() throws Exception {
            // Given
            CustomSoftAssertion softSpy = spy(new CustomSoftAssertion());
            Field softField = Quest.class.getDeclaredField("softAssertions");
            softField.setAccessible(true);
            softField.set(quest, softSpy);

            // When/Then
            try (MockedStatic<LogTest> logMock = mockStatic(LogTest.class);
                 MockedStatic<QuestHolder> holderMock = mockStatic(QuestHolder.class)) {

                quest.complete();

                // Verify interactions
                logMock.verify(() -> LogTest.info("The quest has reached his end"));
                holderMock.verify(QuestHolder::clear);
                verify(softSpy, times(1)).assertAll();
            }
        }
    }

    @Nested
    @DisplayName("Utility method tests")
    class UtilityMethodTests {
        @Test
        @DisplayName("Should return storage")
        void testGetStorage() {
            // When
            Storage storage = quest.getStorage();

            // Then
            assertNotNull(storage);
        }

        @Test
        @DisplayName("Should return soft assertions")
        void testGetSoftAssertions() {
            // When
            CustomSoftAssertion softAssertions = quest.getSoftAssertions();

            // Then
            assertNotNull(softAssertions);
        }
    }

    @Nested
    @DisplayName("World management tests")
    class WorldManagementTests {
        @Test
        @DisplayName("Should remove world")
        void testRemoveWorld() {
            // Given
            quest.exposeRemoveWorld(MockFluentService.class);

            // When/Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> quest.enters(MockFluentService.class)
            );
            assertEquals(
                    "World not initialized: " + MockFluentService.class.getName(),
                    exception.getMessage()
            );
        }
    }

    @Test
    @DisplayName("Should throw ReflectionException when no matching field type is found")
    void testArtifactNoMatchingFieldType() {
        // Given
        class NoMatchingFieldService extends FluentService {
            // Intentionally left without a String field
            private int someIntField = 42;
        }

        quest.exposeRegisterWorld(NoMatchingFieldService.class, new NoMatchingFieldService());

        // When/Then
        ReflectionException exception = assertThrows(
                ReflectionException.class,
                () -> quest.exposeArtifact(NoMatchingFieldService.class, String.class)
        );

        // Use contains instead of exact match to handle anonymous class naming
        assertTrue(
                exception.getMessage().contains("No field of type 'java.lang.String' found in class") &&
                        exception.getMessage().contains("NoMatchingFieldService"),
                "Unexpected exception message: " + exception.getMessage()
        );
    }

    @Test
    @DisplayName("Should throw ReflectionException when field value does not match expected type")
    void testArtifactFieldValueTypeMismatch() {
        // Given
        class TypeMismatchService extends FluentService {
            private Integer intField = 42;
        }

        quest.exposeRegisterWorld(TypeMismatchService.class, new TypeMismatchService());

        // When/Then
        ReflectionException exception = assertThrows(
                ReflectionException.class,
                () -> quest.exposeArtifact(TypeMismatchService.class, String.class)
        );

        // Verify exception message
        assertTrue(
                exception.getMessage().contains("No field of type 'java.lang.String' found in class") &&
                        exception.getMessage().contains("TypeMismatchService"),
                "Unexpected exception message: " + exception.getMessage()
        );
    }

    @Test
    @DisplayName("Should throw IllegalStateException when world is null")
    void testArtifactNullWorld() {
        // Given
        class NullWorldService extends FluentService {}

        TestableQuest spyQuest = spy(quest);
        spyQuest.exposeRegisterWorld(NullWorldService.class, null);

        // When/Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> spyQuest.exposeArtifact(NullWorldService.class, String.class)
        );

        assertEquals(
                "Could not retrieve an instance of the specified worldType: " + NullWorldService.class.getName(),
                exception.getMessage()
        );
    }
}