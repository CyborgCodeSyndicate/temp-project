package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;


@DisplayName("Table Interface Default Method Tests")
@SuppressWarnings("all")
class TableTest extends BaseUnitUITest {

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private Table tableMock;

    @Mock private TableField<Object> mockTableField;

    private static final String[] SAMPLE_VALUES = {"val1", "val2"};
    private static final String SINGLE_VALUE = "val1";
    private static final List<String> SAMPLE_CRITERIA = List.of("test");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Stub abstract methods leniently as they might be called by defaults
        lenient().doNothing().when(tableMock).insertCellValue(anyInt(), any(), any(), anyInt(), any());
        lenient().doNothing().when(tableMock).insertCellValue(anyList(), any(), any(), anyInt(), any());
    }

    @Nested
    @DisplayName("InsertCellValue Default Method Tests")
    class InsertCellValueTests {
        @Test
        @DisplayName("insertCellValue(row, class, field, values...) delegates to overload with index 1")
        void insertCellValueWithRowDefaultIndex() {
            // Given - tableMock setup in @BeforeEach

            // When
            tableMock.insertCellValue(3, Object.class, mockTableField, SAMPLE_VALUES);

            // Then
            verify(tableMock).insertCellValue(3, Object.class, mockTableField, 1, SAMPLE_VALUES);
        }

        @Test
        @DisplayName("insertCellValue(criteria, class, field, values...) delegates to overload with index 1")
        void insertCellValueWithSearchCriteriaDefaultIndex() {
            // Given - tableMock setup in @BeforeEach

            // When
            tableMock.insertCellValue(SAMPLE_CRITERIA, Object.class, mockTableField, SINGLE_VALUE);

            // Then
            verify(tableMock).insertCellValue(SAMPLE_CRITERIA, Object.class, mockTableField, 1, SINGLE_VALUE);
        }
    }
}