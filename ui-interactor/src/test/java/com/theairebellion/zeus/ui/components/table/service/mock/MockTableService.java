package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.service.TableService;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class MockTableService implements TableService {

    public TableComponentType lastComponentTypeUsed;
    public TableComponentType explicitComponentType;
    public Class<?> lastClazz;
    public TableField<?>[] lastFields;
    public Integer lastStart;
    public Integer lastEnd;
    public Integer lastRow;
    public List<String> lastSearchCriteria;
    public Object lastDataObject;
    public TableField<?> lastColumn;
    public FilterStrategy lastFilterStrategy;
    public String[] lastValues;
    public SortingStrategy lastSortingStrategy;
    public Object lastValidationTable;
    public Assertion[] lastAssertions;

    public List<?> returnList = Collections.emptyList();
    public Object returnObject = "mockRowObject";

    public MockTableService() {
        reset();
    }

    private void setLastType(TableComponentType type) {
        this.explicitComponentType = type;
        try {
            Class<?> mockEnumClass = Class.forName("com.theairebellion.zeus.ui.components.table.service.mock.MockTableComponentType");
            Enum<?> dummy = Enum.valueOf((Class<Enum>)mockEnumClass, "DUMMY_TABLE");
            if (dummy.equals(type)) {
                this.lastComponentTypeUsed = (TableComponentType) dummy;
            } else {
                this.lastComponentTypeUsed = null;
            }
        } catch (Exception e) {
            this.lastComponentTypeUsed = null;
        }
    }

    public void reset() {
        lastComponentTypeUsed = null;
        try {
            Class<?> mockEnumClass = Class.forName("com.theairebellion.zeus.ui.components.table.service.mock.MockTableComponentType");
            explicitComponentType = (TableComponentType) Enum.valueOf((Class<Enum>)mockEnumClass, "DUMMY_TABLE");
        } catch (Exception e) {
            explicitComponentType = null;
        }
        lastClazz = null;
        lastFields = null;
        lastStart = null;
        lastEnd = null;
        lastRow = null;
        lastSearchCriteria = null;
        lastDataObject = null;
        lastColumn = null;
        lastFilterStrategy = null;
        lastValues = null;
        lastSortingStrategy = null;
        lastValidationTable = null;
        lastAssertions = null;
        returnList = Collections.emptyList();
        returnObject = "mockRowObject";
    }


    @Override
    public List readTable(TableComponentType tct, Class clazz) {
        setLastType(tct);
        lastClazz = clazz;
        return returnList;
    }

    @Override
    public List readTable(TableComponentType tct, Class clazz, TableField... fields) {
        setLastType(tct);
        lastClazz = clazz;
        lastFields = fields;
        return returnList;
    }

    @Override
    public List readTable(TableComponentType tct, int start, int end, Class clazz) {
        setLastType(tct);
        lastStart = start;
        lastEnd = end;
        lastClazz = clazz;
        return returnList;
    }

    @Override
    public List readTable(TableComponentType tct, int start, int end, Class clazz, TableField... fields) {
        setLastType(tct);
        lastStart = start;
        lastEnd = end;
        lastClazz = clazz;
        lastFields = fields;
        return returnList;
    }

    @Override
    public Object readRow(TableComponentType tct, int row, Class clazz) {
        setLastType(tct);
        lastRow = row;
        lastClazz = clazz;
        return returnObject;
    }

    @Override
    public Object readRow(TableComponentType tct, List searchCriteria, Class clazz) {
        setLastType(tct);
        lastSearchCriteria = searchCriteria;
        lastClazz = clazz;
        return returnObject;
    }

    @Override
    public Object readRow(TableComponentType tct, int row, Class clazz, TableField... fields) {
        setLastType(tct);
        lastRow = row;
        lastClazz = clazz;
        lastFields = fields;
        return returnObject;
    }

    @Override
    public Object readRow(TableComponentType tct, List searchCriteria, Class clazz, TableField... fields) {
        setLastType(tct);
        lastSearchCriteria = searchCriteria;
        lastClazz = clazz;
        lastFields = fields;
        return returnObject;
    }

    @Override
    public void insertCellValue(TableComponentType tct, int row, Class tClass, Object data) {
        setLastType(tct);
        lastRow = row;
        lastClazz = tClass;
        lastDataObject = data;
    }

    @Override
    public void insertCellValue(TableComponentType tct, int row, Class tClass, TableField field, int index, String... value) {
        setLastType(tct);
        lastRow = row;
        lastClazz = tClass;
        lastColumn = field;
        lastValues = value;
    }

    @Override
    public void insertCellValue(TableComponentType tct, List searchCriteria, Class tClass, TableField field, int index, String... values) {
        setLastType(tct);
        lastSearchCriteria = searchCriteria;
        lastClazz = tClass;
        lastColumn = field;
        lastValues = values;
    }

    @Override
    public void insertCellValue(TableComponentType tct, List searchCriteria, Class tClass, Object data) {
        setLastType(tct);
        lastSearchCriteria = searchCriteria;
        lastClazz = tClass;
        lastDataObject = data;
    }

    @Override
    public void filterTable(TableComponentType tct, Class tclass, TableField column, FilterStrategy filterStrategy, String... values) {
        setLastType(tct);
        lastClazz = tclass;
        lastColumn = column;
        lastFilterStrategy = filterStrategy;
        lastValues = values;
    }

    @Override
    public void sortTable(TableComponentType tct, Class tclass, TableField column, SortingStrategy sortingStrategy) {
        setLastType(tct);
        lastClazz = tclass;
        lastColumn = column;
        lastSortingStrategy = sortingStrategy;
    }

    @Override
    public <T> List<AssertionResult<T>> validate(Object table, Assertion... assertions) {
        lastValidationTable = table;
        lastAssertions = assertions;
        return Collections.emptyList();
    }
}