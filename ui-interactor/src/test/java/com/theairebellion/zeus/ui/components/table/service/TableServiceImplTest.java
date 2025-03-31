package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.validator.UiTableValidator;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
class TableServiceImplTest extends BaseUnitUITest {

    private TestTableServiceImpl testService;

    @Mock
    private SmartWebDriver smartWebDriver;

    @Mock
    private TableServiceRegistry tableServiceRegistry;

    @Mock
    private UiTableValidator uiTableValidator;

    @Mock
    private static Table tableMock;

    private final DummyTableComponentType dummyComponentType = DummyTableComponentType.DUMMY;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testService = new TestTableServiceImpl(smartWebDriver, tableServiceRegistry, uiTableValidator, tableMock);
    }

    @Nested
    class ReadTableTests {
        @Test
        void testReadTableNoFields() {
            List<String> result = List.of("row1", "row2");
            when(tableMock.readTable(String.class)).thenReturn(result);
            List<String> tableResult = testService.readTable(dummyComponentType, String.class);
            assertEquals(result, tableResult);
            verify(tableMock).readTable(String.class);
        }

        @Test
        void testReadTableWithFields() {
            List<String> result = List.of("row1", "row2");
            TableField<String> field = createDummyTableField();
            when(tableMock.readTable(eq(String.class), any())).thenReturn(result);
            List<String> tableResult = testService.readTable(dummyComponentType, String.class, field);
            assertEquals(result, tableResult);
            verify(tableMock).readTable(eq(String.class), any());
        }

        @Test
        void testReadTableRangeNoFields() {
            List<String> result = List.of("row1", "row2");
            when(tableMock.readTable(1, 2, String.class)).thenReturn(result);
            List<String> tableResult = testService.readTable(dummyComponentType, 1, 2, String.class);
            assertEquals(result, tableResult);
            verify(tableMock).readTable(1, 2, String.class);
        }

        @Test
        void testReadTableRangeWithFields() {
            List<String> result = List.of("row1", "row2");
            TableField<String> field = createDummyTableField();
            when(tableMock.readTable(1, 2, String.class, field)).thenReturn(result);
            List<String> tableResult = testService.readTable(dummyComponentType, 1, 2, String.class, field);
            assertEquals(result, tableResult);
            verify(tableMock).readTable(1, 2, String.class, field);
        }
    }

    @Nested
    class ReadRowTests {
        @Test
        void testReadRowByIndex() {
            String row = "row";
            when(tableMock.readRow(2, String.class)).thenReturn(row);
            String result = testService.readRow(dummyComponentType, 2, String.class);
            assertEquals(row, result);
            verify(tableMock).readRow(2, String.class);
        }

        @Test
        void testReadRowByCriteria() {
            String row = "row";
            List<String> criteria = List.of("search");
            when(tableMock.readRow(criteria, String.class)).thenReturn(row);
            String result = testService.readRow(dummyComponentType, criteria, String.class);
            assertEquals(row, result);
            verify(tableMock).readRow(criteria, String.class);
        }

        @Test
        void testReadRowByIndexWithFields() {
            String row = "row";
            TableField<String> field = createDummyTableField();
            when(tableMock.readRow(2, String.class, field)).thenReturn(row);
            String result = testService.readRow(dummyComponentType, 2, String.class, field);
            assertEquals(row, result);
            verify(tableMock).readRow(2, String.class, field);
        }

        @Test
        void testReadRowByCriteriaWithFields() {
            String row = "row";
            List<String> criteria = List.of("search");
            TableField<String> field = createDummyTableField();
            when(tableMock.readRow(criteria, String.class, field)).thenReturn(row);
            String result = testService.readRow(dummyComponentType, criteria, String.class, field);
            assertEquals(row, result);
            verify(tableMock).readRow(criteria, String.class, field);
        }
    }

    @Nested
    class InsertCellValueTests {
        @Test
        void testInsertCellValueDataByIndex() {
            testService.insertCellValue(dummyComponentType, 2, String.class, "data");
            verify(tableMock).insertCellValue(2, String.class, "data");
        }

        @Test
        void testInsertCellValueFieldByIndex() {
            TableField<String> field = createDummyTableField();
            testService.insertCellValue(dummyComponentType, 2, String.class, field, 1, "val");
            verify(tableMock).insertCellValue(2, String.class, field, 1, "val");
        }

        @Test
        void testInsertCellValueDataByCriteria() {
            List<String> criteria = List.of("search");
            testService.insertCellValue(dummyComponentType, criteria, String.class, "data");
            verify(tableMock).insertCellValue(criteria, String.class, "data");
        }

        @Test
        void testInsertCellValueFieldByCriteria() {
            List<String> criteria = List.of("search");
            TableField<String> field = createDummyTableField();
            testService.insertCellValue(dummyComponentType, criteria, String.class, field, 1, "val");
            verify(tableMock).insertCellValue(criteria, String.class, field, 1, "val");
        }
    }

    @Nested
    class TableOperationTests {
        @Test
        void testFilterTable() {
            TableField<String> field = createDummyTableField();
            testService.filterTable(dummyComponentType, String.class, field, FilterStrategy.SELECT, "val");
            verify(tableMock).filterTable(String.class, field, FilterStrategy.SELECT, "val");
        }

        @Test
        void testSortTable() {
            TableField<String> field = createDummyTableField();
            testService.sortTable(dummyComponentType, String.class, field, SortingStrategy.ASC);
            verify(tableMock).sortTable(String.class, field, SortingStrategy.ASC);
        }
    }

    @Nested
    class ValidationTests {
        @Test
        void testValidate_WithValidInput() {
            Assertion assertion1 = mock(Assertion.class);
            Assertion assertion2 = mock(Assertion.class);

            // Create expected results with explicit type casting
            @SuppressWarnings("unchecked")
            List<AssertionResult<String>> expectedResults = (List<AssertionResult<String>>)
                    (List<?>) List.of(
                            mock(AssertionResult.class),
                            mock(AssertionResult.class)
                    );

            // Mock the validator to return expected results
            // Use .thenAnswer to handle generic type complexity
            when(uiTableValidator.validateTable(
                    eq("table object"),
                    eq(assertion1),
                    eq(assertion2)
            )).thenAnswer(invocation -> expectedResults);

            // Execute the validate method
            List<AssertionResult<String>> results = testService.validate("table object", assertion1, assertion2);

            // Verify the validator was called with the correct parameters
            verify(uiTableValidator).validateTable(
                    eq("table object"),
                    eq(assertion1),
                    eq(assertion2)
            );

            // Verify the results match what we expected
            assertEquals(expectedResults, results);
        }

        @Test
        void testValidate_WithNullTable() {
            // Create mock assertions
            Assertion assertion = mock(Assertion.class);

            // Execute and verify exception thrown for null table
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> testService.validate(null, assertion)
            );

            // Verify the exception message
            assertEquals("Table cannot be null for validation.", ex.getMessage());
        }

        @Test
        void testValidate_WithNullAssertions() {
            // Execute and verify exception thrown for null assertions
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> testService.validate("table object", (Assertion[]) null)
            );

            // Verify the exception message
            assertEquals("At least one assertion must be provided.", ex.getMessage());
        }

        @Test
        void testValidate_WithEmptyAssertions() {
            // Execute and verify exception thrown for empty assertions array
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> testService.validate("table object", new Assertion[0])
            );

            // Verify the exception message
            assertEquals("At least one assertion must be provided.", ex.getMessage());
        }
    }

    @Nested
    class ComponentCreationTests {
        @Test
        void testCreateComponent() {
            // Create a new instance of TableServiceImpl that calls the real createComponent method
            TableServiceImpl realTableService = new TableServiceImpl(smartWebDriver, tableServiceRegistry, uiTableValidator) {
                // Expose the protected createComponent method for testing
                @Override
                public Table createComponent(TableComponentType componentType) {
                    return super.createComponent(componentType);
                }
            };

            // Mock static ComponentFactory
            try (MockedStatic<ComponentFactory> componentFactoryMock = mockStatic(ComponentFactory.class)) {
                // Create mock TableImpl
                TableImpl mockTableImpl = mock(TableImpl.class);

                // Set up the mock to return our mockTableImpl when getTableComponent is called
                componentFactoryMock.when(() -> ComponentFactory.getTableComponent(any(TableComponentType.class), any(SmartWebDriver.class)))
                        .thenReturn(mockTableImpl);

                // Call createComponent
                Table result = realTableService.createComponent(dummyComponentType);

                // Verify that the factory was called with the right parameters
                componentFactoryMock.verify(() -> ComponentFactory.getTableComponent(dummyComponentType, smartWebDriver));

                // Verify the service registry was set on the table
                verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);

                // Verify the result is our mock table
                assertSame(mockTableImpl, result);
            }
        }
    }

    // Helper method to create a dummy TableField
    private <T> TableField<T> createDummyTableField() {
        return (instance, obj) -> {};
    }

    // Existing enums and inner classes remain the same
    enum DummyTableComponentType implements TableComponentType {
        DUMMY;

        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    static class TestTableServiceImpl extends TableServiceImpl {
        TestTableServiceImpl(SmartWebDriver driver, TableServiceRegistry registry, UiTableValidator uiTableValidator, Table tableMock) {
            super(driver, registry, uiTableValidator);
        }

        @Override
        protected Table getOrCreateComponent(TableComponentType componentType) {
            return tableMock;
        }
    }
}