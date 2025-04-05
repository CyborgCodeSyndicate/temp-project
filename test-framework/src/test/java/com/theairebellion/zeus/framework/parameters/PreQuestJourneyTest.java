package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.parameters.mock.MockEnum;
import com.theairebellion.zeus.framework.parameters.mock.MockPreQuestJourney;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PreQuestJourneyTest {

    private static final String ARG_1 = "arg1";
    private static final String ARG_2 = "arg2";

    @Mock
    private SuperQuest mockSuperQuest;

    @Nested
    @DisplayName("PreQuestJourney.journey() tests")
    class JourneyTests {
        @Test
        @DisplayName("Should execute the BiConsumer with quest and args")
        void testJourneyExecution() {
            // Given
            AtomicBoolean executed = new AtomicBoolean(false);
            BiConsumer<SuperQuest, Object[]> consumer = (quest, args) -> executed.set(true);
            PreQuestJourney preQuestJourney = new MockPreQuestJourney(consumer);
            Object[] testArgs = new Object[]{ARG_1, ARG_2};

            // When
            preQuestJourney.journey().accept(mockSuperQuest, testArgs);

            // Then
            assertTrue(executed.get(), "BiConsumer should have been executed");
        }

        @Test
        @DisplayName("Should pass the correct arguments to the BiConsumer")
        void testJourneyWithCorrectArgs() {
            // Given
            AtomicReference<Object[]> capturedArgs = new AtomicReference<>();
            AtomicReference<SuperQuest> capturedQuest = new AtomicReference<>();

            BiConsumer<SuperQuest, Object[]> consumer = (quest, args) -> {
                capturedQuest.set(quest);
                capturedArgs.set(args);
            };

            PreQuestJourney preQuestJourney = new MockPreQuestJourney(consumer);
            Object[] testArgs = new Object[]{ARG_1, ARG_2};

            // When
            preQuestJourney.journey().accept(mockSuperQuest, testArgs);

            // Then
            assertSame(mockSuperQuest, capturedQuest.get(), "The correct SuperQuest should be passed to the consumer");
            assertSame(testArgs, capturedArgs.get(), "The correct args array should be passed to the consumer");
            assertEquals(ARG_1, capturedArgs.get()[0], "First argument should match");
            assertEquals(ARG_2, capturedArgs.get()[1], "Second argument should match");
        }
    }

    @Nested
    @DisplayName("PreQuestJourney.enumImpl() tests")
    class EnumImplTests {
        @Test
        @DisplayName("Should return the correct enum value")
        void testEnumImpl() {
            // Given
            BiConsumer<SuperQuest, Object[]> consumer = (quest, args) -> {};
            PreQuestJourney preQuestJourney = new MockPreQuestJourney(consumer);

            // When
            Enum<?> result = preQuestJourney.enumImpl();

            // Then
            assertEquals(MockEnum.INSTANCE, result, "Should return MockEnum.INSTANCE");
        }
    }
}