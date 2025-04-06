package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.quest.mock.MockFluentService;
import com.theairebellion.zeus.framework.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SuperQuestTest {

    private Quest quest;
    private SuperQuest superQuest;
    private MockFluentService mockFluentService;

    @BeforeEach
    void setUp() {
        quest = new Quest();
        superQuest = new SuperQuest(quest);
        mockFluentService = new MockFluentService();
    }

    @Nested
    @DisplayName("Artifact retrieval tests")
    class ArtifactTests {
        @Test
        @DisplayName("Should retrieve artifact from world")
        void testArtifact() {
            // Given
            quest.registerWorld(MockFluentService.class, mockFluentService);

            // When
            String value = superQuest.artifact(MockFluentService.class, String.class);

            // Then
            assertEquals("mockValue", value, "Should retrieve the correct artifact value");
        }
    }

    @Nested
    @DisplayName("World management tests")
    class WorldManagementTests {
        @Test
        @DisplayName("Should cast to registered world")
        void testRegisterWorldAndCast() {
            // Given
            // Looking at the original test, we need to manually share the worlds map
            quest.registerWorld(MockFluentService.class, mockFluentService);
            try {
                // Get the worlds map from the quest
                Field worldsField = Quest.class.getDeclaredField("worlds");
                worldsField.setAccessible(true);
                Map<?, ?> worldsMap = (Map<?, ?>) worldsField.get(quest);

                // Set it on the superQuest
                worldsField.set(superQuest, worldsMap);

                // When
                MockFluentService casted = superQuest.cast(MockFluentService.class);

                // Then
                assertSame(mockFluentService, casted, "Cast should return the same instance");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                fail("Failed to access worlds field: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Should register and remove world correctly")
        void testRemoveWorld() {
            // Given
            superQuest.registerWorld(MockFluentService.class, mockFluentService);

            // When
            superQuest.removeWorld(MockFluentService.class);

            // Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> superQuest.cast(MockFluentService.class),
                    "Should throw exception when attempting to cast to removed world"
            );

            assertTrue(exception.getMessage().contains("World not initialized"),
                    "Exception message should indicate world not initialized");
        }
    }

    @Nested
    @DisplayName("Utility method tests")
    class UtilityMethodTests {
        @Test
        @DisplayName("Should access original Quest's storage")
        void testGetStorage() {
            // When
            Storage storageFromQuest = quest.getStorage();
            Storage storageFromSuperQuest = superQuest.getStorage();

            // Then
            assertSame(storageFromQuest, storageFromSuperQuest,
                    "Storage from SuperQuest should be the same as from Quest");
        }

        @Test
        @DisplayName("Should access original Quest's soft assertions")
        void testGetSoftAssertions() {
            // When
            CustomSoftAssertion assertionsFromQuest = quest.getSoftAssertions();
            CustomSoftAssertion assertionsFromSuperQuest = superQuest.getSoftAssertions();

            // Then
            assertSame(assertionsFromQuest, assertionsFromSuperQuest,
                    "SoftAssertions from SuperQuest should be the same as from Quest");
        }
    }

    @Test
    @DisplayName("Should get the original Quest instance")
    void testGetOriginal() {
        // When/Then
        assertSame(quest, superQuest.getOriginal(),
                "getOriginal should return the Quest instance provided in constructor");
    }

    @Nested
    @DisplayName("Delegate method tests")
    class DelegateMethodTests {
        @Spy
        private Quest spyQuest;

        @BeforeEach
        void setUpSpy() {
            superQuest = new SuperQuest(spyQuest);
        }

        @Test
        @DisplayName("Should delegate complete method to original Quest")
        void testCompleteDelegate() {
            // When
            superQuest.complete();

            // Then
            verify(spyQuest).complete();
        }

        @Test
        @DisplayName("Should delegate enters method to original Quest")
        void testEntersDelegate() {
            // Given
            spyQuest.registerWorld(MockFluentService.class, mockFluentService);

            // When
            superQuest.enters(MockFluentService.class);

            // Then
            verify(spyQuest).enters(MockFluentService.class);
        }
    }
}