package com.theairebellion.zeus.ui.validator;

import java.util.HashSet;
import java.util.List;

public class TableAssertionFunctions {


    public static boolean valuesPresentInAllRows(Object actual, Object expected) {
        List<List<String>> rowValue = (List<List<String>>) actual;
        List<String> rowIndicators = (List<String>) expected;
        return rowValue.stream().allMatch(list -> new HashSet<>(list).containsAll(rowIndicators));
    }

}
