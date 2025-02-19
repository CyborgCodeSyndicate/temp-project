package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

import java.util.*;
import java.util.stream.Collectors;

public class TableAssertionFunctions {

    public static boolean validateTableNotEmpty(Object actual, Object expected) {
        if (!(actual instanceof List)) {
            return false;
        }
        List<?> table = (List<?>) actual;
        boolean notEmpty = !table.isEmpty();

        if (!(expected instanceof Boolean)) {
            return false;
        }

        return notEmpty == (boolean) expected;
    }

    public static boolean validateTableRowCount(Object actual, Object expected) {
        if (!(actual instanceof List) || !(expected instanceof Integer)) {
            return false;
        }

        List<?> table = (List<?>) actual;
        int expectedRowCount = (Integer) expected;

        return table.size() == expectedRowCount;
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
        boolean unique = uniqueRows.size() == table.size();
        if (!(expected instanceof Boolean)) {
            return false;
        }
        return unique == (boolean) expected;
    }

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

        return noEmptyCells == (boolean) expected;
    }

    public static boolean validateColumnValueUniqueness(Object actual, Object expected) {
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

        boolean rowNotEmpty = !row.isEmpty() && row.stream()
                .anyMatch(cell -> cell instanceof String && !((String) cell).trim().isEmpty());

        if (!(expected instanceof Boolean)) {
            return false;
        }

        return rowNotEmpty == (boolean) expected;
    }

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

        return allCellsEnabled == (boolean) expected;
    }

    public static boolean validateAllCellsAreClickable(Object actual, Object expected) {
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
