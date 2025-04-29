package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.parameters.mock.MockDataForge;
import com.theairebellion.zeus.framework.parameters.mock.MockEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataForgeTest {

    private static final String TEST_DATA = "testData";

    @Nested
    @DisplayName("DataForge.dataCreator() tests")
    class DataCreatorTests {
        @Test
        @DisplayName("Should create Late object with expected data")
        void testDataCreator() {
            // Given
            DataForge<?> dataForge = new MockDataForge<>(TEST_DATA, MockEnum.INSTANCE);

            // When
            Late<Object> late = dataForge.dataCreator();
            Object result = late.join();

            // Then
            assertNotNull(late, "Returned Late object should not be null");
            assertEquals(TEST_DATA, result, "Late.join() should return the expected data");
        }
    }

    @Nested
    @DisplayName("DataForge.enumImpl() tests")
    class EnumImplTests {
        @Test
        @DisplayName("Should return the enum value provided in constructor")
        void testEnumImpl() {
            // Given
            DataForge<?> dataForge = new MockDataForge<>("data", MockEnum.INSTANCE);

            // When
            Enum<?> enumValue = dataForge.enumImpl();

            // Then
            assertNotNull(enumValue, "Returned enum should not be null");
            assertEquals(MockEnum.INSTANCE, enumValue, "Returned enum should match the one provided in constructor");
        }
    }
}