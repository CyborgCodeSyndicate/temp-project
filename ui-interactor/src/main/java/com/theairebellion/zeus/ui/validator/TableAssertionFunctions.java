package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableAssertionFunctions {

    public static boolean validateTableNotEmpty(Object actual, Object expected) {
        if (!(actual instanceof List)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        return !table.isEmpty();
    }

    public static boolean validateTableRowCount(Object actual, Object expected) {
        if (!(actual instanceof List) || !(expected instanceof Integer)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        int expectedRowCount = (Integer) expected;

        return table.size() == expectedRowCount;
    }

    public static boolean validateTableColumnCount(Object actual, Object expected) {
        if (!(actual instanceof List) || !(expected instanceof Integer)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        int expectedColumnCount = (Integer) expected;

        return table.stream().allMatch(row -> row instanceof List<?> && ((List<?>) row).size() == expectedColumnCount);
    }

    public static boolean validateAllRowsContainValues(Object actual, Object expected) {
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

    public static boolean valuesPresentInAllRows(Object actual, Object expected) {
        if (!(actual instanceof List<?> && expected instanceof List<?>)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        List<?> rowIndicators = (List<?>) expected;

        if (table.isEmpty() || rowIndicators.isEmpty()) {
            return false;
        }

        for (Object row : table) {
            if (!(row instanceof List<?>)) {
                return false;
            }
            List<?> rowList = (List<?>) row;
            if (!rowList.containsAll(rowIndicators)) {
                return false;
            }
        }

        return true;
    }

    public static boolean validateTableContainsRow(Object actual, Object expected) {
        if (!(actual instanceof List<?> && expected instanceof List<?>)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        List<?> expectedRow = (List<?>) expected;

        if (table.isEmpty() || expectedRow.isEmpty()) {
            return false;
        }

        for (Object row : table) {
            if (!(row instanceof List<?>)) {
                return false;
            }
            if (row.equals(expectedRow)) {
                return true;
            }
        }

        return false;
    }

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

    public static boolean validateUniqueRows(Object actual, Object expected) {
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

        return uniqueRows.size() == table.size();
    }

    public static boolean validateNoEmptyCells(Object actual, Object expected) {
        if (!(actual instanceof List<?>)) {
            return false;
        }

        List<?> table = (List<?>) actual;

        if (table.isEmpty()) {
            return false;
        }

        for (Object row : table) {
            if (!(row instanceof List<?>)) {
                return false;
            }

            List<?> rowList = (List<?>) row;
            if (rowList.isEmpty() || rowList.stream().anyMatch(cell -> !(cell instanceof String) || ((String) cell).trim().isEmpty())) {
                return false;
            }
        }

        return true;
    }

    public static boolean validateColumnValueUniqueness(Object actual, Object expected) {
        if (!(actual instanceof List<?>) || !(expected instanceof Integer)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        int columnIndex = (Integer) expected;

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

    public static boolean validateRowNotEmpty(Object actual, Object expected) {
        if (!(actual instanceof List<?>)) {
            return false;
        }

        List<?> row = (List<?>) actual;

        if (row.isEmpty()) {
            return false;
        }

        return row.stream().allMatch(cell -> cell instanceof String && !((String) cell).trim().isEmpty());
    }

    public static boolean validateRowContainsValues(Object actual, Object expected) {
        if (!(actual instanceof List<?>) || !(expected instanceof List<?>)) {
            return false;
        }

        List<?> row = (List<?>) actual;
        List<?> expectedValues = (List<?>) expected;

        if (row.isEmpty() || expectedValues.isEmpty()) {
            return false;
        }

        if (!row.stream().allMatch(cell -> cell instanceof String) ||
                !expectedValues.stream().allMatch(value -> value instanceof String)) {
            return false;
        }

        return new HashSet<>(row).containsAll(expectedValues);
    }

    public static boolean validateRowDataMatchesExpected(Object actual, Object expected) {
        if (!(actual instanceof List<?>) || !(expected instanceof List<?>)) {
            return false;
        }

        List<?> row = (List<?>) actual;
        List<?> expectedRow = (List<?>) expected;

        if (row.isEmpty() || expectedRow.isEmpty()) {
            return false;
        }

        if (!row.stream().allMatch(cell -> cell instanceof String) ||
                !expectedRow.stream().allMatch(cell -> cell instanceof String)) {
            return false;
        }

        return row.equals(expectedRow);
    }

    public static boolean validateAllCellsEnabled(Object actual, Object expected) {
        if (!(actual instanceof List<?>)) {
            return false;
        }

        List<?> table = (List<?>) actual;

        return table.stream().allMatch(row ->
                row instanceof List<?> && ((List<?>) row).stream().allMatch(cell ->
                        cell instanceof SmartWebElement && ((SmartWebElement) cell).isEnabled()
                )
        );
    }

    public static boolean validateCellHasAttribute(Object actual, Object expected) {
        if (!(actual instanceof SmartWebElement) || !(expected instanceof Map<?, ?>)) {
            return false;
        }

        SmartWebElement element = (SmartWebElement) actual;
        Map<String, String> expectedAttributes = (Map<String, String>) expected;

        return expectedAttributes.entrySet().stream()
                .allMatch(entry -> entry.getValue().equals(element.getAttribute(entry.getKey())));
    }

    public static boolean validateCellIsClickable(Object actual, Object expected) {
        if (!(actual instanceof SmartWebElement)) {
            return false;
        }

        SmartWebElement element = (SmartWebElement) actual;
        return element.isDisplayed() && element.isEnabled();
    }

    public static boolean validateAllCellsAreClickable(Object actual, Object expected) {
        if (!(actual instanceof List<?>)) {
            return false;
        }

        List<?> table = (List<?>) actual;

        return table.stream().allMatch(row ->
                row instanceof List<?> && ((List<?>) row).stream().allMatch(cell ->
                        cell instanceof SmartWebElement && ((SmartWebElement) cell).isDisplayed()
                                && ((SmartWebElement) cell).isEnabled()
                )
        );
    }

}
