package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Provides a set of validation functions for asserting table data integrity.
 * <p>
 * This class includes various validation methods to check the state of table elements,
 * such as row counts, uniqueness, presence of values, and cell properties (e.g., enabled, clickable).
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class TableAssertionFunctions {

    /**
     * Validates if a table is not empty.
     *
     * @param actual   The table data (expected to be a {@code List}).
     * @param expected The expected result (a {@code Boolean}).
     * @return {@code true} if the table is not empty and matches the expected state; otherwise, {@code false}.
     */
    public static boolean validateTableNotEmpty(Object actual, Object expected) {
        if (!(actual instanceof List)) {
            return false;
        }
        List<?> table = (List<?>) actual;
        boolean notEmpty = !table.isEmpty();

        if (!(expected instanceof Boolean)) {
            return false;
        }

        return notEmpty == (Boolean) expected;
    }

    /**
     * Validates the row count of a table.
     *
     * @param actual   The table data (expected to be a {@code List}).
     * @param expected The expected row count (an {@code Integer}).
     * @return {@code true} if the table has the expected number of rows; otherwise, {@code false}.
     */
    public static boolean validateTableRowCount(Object actual, Object expected) {
        if (!(actual instanceof List) || !(expected instanceof Integer)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        int expectedRowCount = (Integer) expected;

        return table.size() == expectedRowCount;
    }

    /**
     * Validates if every row in the table contains the expected values.
     *
     * @param actual   The table data (expected to be a {@code List} of rows).
     * @param expected The expected values (a {@code List} of items that should be present in each row).
     * @return {@code true} if every row contains all the expected values; otherwise, {@code false}.
     */
    public static boolean validateEveryRowContainsValues(Object actual, Object expected) {
        if (!(actual instanceof List) || !(expected instanceof List)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        List<?> expectedValues = (List<?>) expected;

        if (table.isEmpty() || expectedValues.isEmpty()) {
            return false;
        }

        return table.stream()
                .allMatch(row -> row instanceof List<?> && new HashSet<>((List<?>) row).containsAll(expectedValues));
    }

    /**
     * Validates that a specific row is not present in the table.
     *
     * @param actual   The table data (expected to be a {@code List} of rows).
     * @param expected The row that should not be present (a {@code List} representing the row).
     * @return {@code true} if the row is absent; otherwise, {@code false}.
     */
    public static boolean validateTableDoesNotContainRow(Object actual, Object expected) {
        if (!(actual instanceof List<?> && expected instanceof List<?>)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        List<?> forbiddenRow = (List<?>) expected;

        if (table.isEmpty() || forbiddenRow.isEmpty()) {
            return false;
        }

        for (Object row : table) {
            if (!(row instanceof List<?>)) {
                return false;
            }
            if (row.equals(forbiddenRow)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates that all rows in the table are unique.
     *
     * @param actual   The table data (expected to be a {@code List} of rows).
     * @param expected The expected uniqueness state (a {@code Boolean}).
     * @return {@code true} if all rows are unique and match the expected uniqueness state; otherwise, {@code false}.
     */
    public static boolean validateAllRowsAreUnique(Object actual, Object expected) {
        if (!(actual instanceof List<?>)) {
            return false;
        }
        List<?> table = (List<?>) actual;
        if (table.isEmpty()) {
            return false;
        }
        Set<List<?>> uniqueRows = new HashSet<>();
        for (Object row : table) {
            if (!(row instanceof List<?>)) {
                return false;
            }
            uniqueRows.add((List<?>) row);
        }
        boolean unique = uniqueRows.size() == table.size();
        if (!(expected instanceof Boolean)) {
            return false;
        }
        return unique == (Boolean) expected;
    }

    /**
     * Validates that there are no empty cells in the table.
     *
     * @param actual   The table data (expected to be a {@code List} of rows).
     * @param expected The expected state (a {@code Boolean}).
     * @return {@code true} if there are no empty cells and matches the expected state; otherwise, {@code false}.
     */
    public static boolean validateNoEmptyCells(Object actual, Object expected) {
        if (!(actual instanceof List<?>)) {
            return false;
        }

        List<?> table = (List<?>) actual;

        if (table.isEmpty()) {
            return false;
        }

        boolean noEmptyCells = true;

        for (Object row : table) {
            if (!(row instanceof List<?>)) {
                noEmptyCells = false;
                break;
            }

            List<?> rowList = (List<?>) row;
            if (rowList.isEmpty() || rowList.stream().anyMatch(cell ->
                    !(cell instanceof String) || ((String) cell).trim().isEmpty())) {
                noEmptyCells = false;
                break;
            }
        }

        if (!(expected instanceof Boolean)) {
            return false;
        }

        return noEmptyCells == (Boolean) expected;
    }

    /**
     * Validates that a column contains only unique values.
     *
     * @param actual   The table data (expected to be a {@code List} of rows).
     * @param expected The column index (an {@code Integer}, 1-based index).
     * @return {@code true} if all values in the specified column are unique; otherwise, {@code false}.
     */
    public static boolean validateColumnValuesAreUnique(Object actual, Object expected) {
        if (!(actual instanceof List<?>) || !(expected instanceof Integer)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        int userColumnIndex = (Integer) expected;
        int columnIndex = userColumnIndex - 1;

        if (table.isEmpty() || columnIndex < 0) {
            return false;
        }

        Set<String> uniqueValues = new HashSet<>();

        for (Object row : table) {
            if (!(row instanceof List<?>)) {
                return false;
            }

            List<?> rowList = (List<?>) row;
            if (rowList.size() <= columnIndex || !(rowList.get(columnIndex) instanceof String)) {
                return false;
            }

            if (!uniqueValues.add((String) rowList.get(columnIndex))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates that the actual table data matches the expected table data.
     *
     * @param actual   The table data (expected to be a {@code List} of rows).
     * @param expected The expected table data (a {@code List} of rows).
     * @return {@code true} if both tables match exactly; otherwise, {@code false}.
     */
    public static boolean validateTableDataMatchesExpected(Object actual, Object expected) {
        if (!(actual instanceof List<?>) || !(expected instanceof List<?>)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        List<?> expectedTable = (List<?>) expected;

        if (table.isEmpty() || expectedTable.isEmpty()) {
            return false;
        }

        for (Object row : table) {
            if (!(row instanceof List<?>)) {
                return false;
            }
        }

        for (Object row : expectedTable) {
            if (!(row instanceof List<?>)) {
                return false;
            }
        }

        return table.equals(expectedTable);
    }

    /**
     * Validates whether a row is not empty.
     * <p>
     * A row is considered non-empty if it contains at least one non-blank string value.
     * </p>
     *
     * @param actual   The row data (expected to be a {@code List} of objects representing table cells).
     * @param expected The expected state (a {@code Boolean}, where {@code true} means the row should be non-empty).
     * @return {@code true} if the row is non-empty and matches the expected state; otherwise, {@code false}.
     */
    public static boolean validateRowNotEmpty(Object actual, Object expected) {
        if (!(actual instanceof List<?>)) {
            return false;
        }
        List<?> row = (List<?>) actual;

        boolean rowNotEmpty = !row.isEmpty() && row.stream()
                .anyMatch(cell -> cell instanceof String && !((String) cell).trim().isEmpty());

        if (!(expected instanceof Boolean)) {
            return false;
        }

        return rowNotEmpty == (Boolean) expected;
    }

    /**
     * Validates whether a row contains specific expected values.
     * <p>
     * The validation checks if all expected values are present within the row.
     * Comparison is performed in a case-insensitive manner, ignoring leading and trailing spaces.
     * </p>
     *
     * @param actual   The row data (expected to be a {@code List} of objects representing table cells).
     * @param expected The expected values (a {@code List} of objects that should be found in the row).
     * @return {@code true} if the row contains all the expected values; otherwise, {@code false}.
     */
    public static boolean validateRowContainsValues(Object actual, Object expected) {
        if (!(actual instanceof List<?>) || !(expected instanceof List<?>)) {
            return false;
        }

        List<?> rawRow = (List<?>) actual;
        List<?> rawExpected = (List<?>) expected;

        if (rawRow.isEmpty() || rawExpected.isEmpty()) {
            return false;
        }

        List<String> rowValues = rawRow.stream()
                .filter(Objects::nonNull)
                .map(cell -> String.valueOf(cell).trim().toLowerCase())
                .toList();

        List<String> expectedValues = rawExpected.stream()
                .filter(Objects::nonNull)
                .map(val -> String.valueOf(val).trim().toLowerCase())
                .toList();

        return new HashSet<>(rowValues).containsAll(expectedValues);
    }

    /**
     * Validates that all table cells are enabled.
     *
     * @param actual   The table data (expected to be a {@code List} of rows containing {@link SmartWebElement}).
     * @param expected The expected enabled state (a {@code Boolean}).
     * @return {@code true} if all cells are enabled and match the expected state; otherwise, {@code false}.
     */
    public static boolean validateAllCellsEnabled(Object actual, Object expected) {
        if (!(actual instanceof List<?>)) {
            return false;
        }
        List<?> table = (List<?>) actual;

        boolean allCellsEnabled = table.stream().allMatch(row ->
                row instanceof List<?> && ((List<?>) row).stream().allMatch(cell ->
                        cell instanceof SmartWebElement && ((SmartWebElement) cell).isEnabled()
                )
        );

        if (!(expected instanceof Boolean)) {
            return false;
        }

        return allCellsEnabled == (Boolean) expected;
    }

    /**
     * Validates whether all cells in the table are clickable.
     * <p>
     * A cell is considered clickable if it is a {@link SmartWebElement}, is displayed, and is enabled.
     * This method ensures that every row in the table contains only clickable elements.
     * </p>
     *
     * @param actual   The table data (expected to be a {@code List} of lists, where each inner list represents a row).
     * @param expected The expected state (a {@code Boolean}, where {@code true} means all cells should be clickable).
     * @return {@code true} if all cells in the table are clickable and match the expected state; otherwise, {@code false}.
     */
    public static boolean validateAllCellsClickable(Object actual, Object expected) {
        if (!(actual instanceof List<?>)) {
            return false;
        }
        List<?> table = (List<?>) actual;
        boolean allClickable = table.stream().allMatch(row ->
                row instanceof List<?> && ((List<?>) row).stream().allMatch(cell ->
                        cell instanceof SmartWebElement &&
                                ((SmartWebElement) cell).isDisplayed() &&
                                ((SmartWebElement) cell).isEnabled()
                )
        );
        if (!(expected instanceof Boolean)) {
            return false;
        }
        return allClickable == (Boolean) expected;
    }

}
