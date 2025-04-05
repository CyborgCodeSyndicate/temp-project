package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.parameters.mock.MockDataRipper;
import com.theairebellion.zeus.framework.parameters.mock.MockEnum;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataRipperTest {

    @Mock
    private SuperQuest mockSuperQuest;

    @Nested
    @DisplayName("DataRipper.eliminate() tests")
    class EliminateTests {
        @Test
        @DisplayName("Should execute the consumer with the provided quest")
        void testEliminate() {
            // Given
            AtomicBoolean executed = new AtomicBoolean(false);
            Consumer<SuperQuest> consumer = quest -> executed.set(true);
            DataRipper dataRipper = new MockDataRipper(consumer);

            // When
            dataRipper.eliminate().accept(mockSuperQuest);

            // Then
            assertTrue(executed.get(), "Consumer should have been executed");
        }
    }

    @Nested
    @DisplayName("DataRipper.enumImpl() tests")
    class EnumImplTests {
        @Test
        @DisplayName("Should return the correct enum value")
        void testEnumImpl() {
            // Given
            DataRipper dataRipper = new MockDataRipper(q -> {});

            // When
            Enum<?> enumValue = dataRipper.enumImpl();

            // Then
            assertNotNull(enumValue, "Returned enum should not be null");
            assertEquals(MockEnum.RIPPED, enumValue, "Returned enum should be MockEnum.RIPPED");
        }
    }
}