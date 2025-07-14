package com.theairebellion.zeus.ui.service.tables.mock;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.service.TableService;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import java.util.List;
import lombok.Setter;

@Setter
public class MockTableService implements TableService {

   private List<?> fakeReadTableResult;
   private Object fakeReadRowResult;
   private List<AssertionResult<Object>> fakeValidateResults;

   @Override
   public <T> List<T> readTable(TableComponentType tableComponentType, Class<T> clazz) {
      return (List<T>) fakeReadTableResult;
   }

   @Override
   public <T> List<T> readTable(TableComponentType tableComponentType, Class<T> clazz, TableField<T>... fields) {
      return (List<T>) fakeReadTableResult;
   }

   @Override
   public <T> List<T> readTable(TableComponentType tableComponentType, int start, int end, Class<T> clazz) {
      return (List<T>) fakeReadTableResult;
   }

   @Override
   public <T> List<T> readTable(TableComponentType tableComponentType, int start, int end, Class<T> clazz, TableField<T>... fields) {
      return (List<T>) fakeReadTableResult;
   }

   @Override
   public <T> T readRow(TableComponentType tableComponentType, int row, Class<T> clazz) {
      return (T) fakeReadRowResult;
   }

   @Override
   public <T> T readRow(TableComponentType tableComponentType, List<String> searchCriteria, Class<T> clazz) {
      return (T) fakeReadRowResult;
   }

   @Override
   public <T> T readRow(TableComponentType tableComponentType, int row, Class<T> clazz, TableField<T>... fields) {
      return (T) fakeReadRowResult;
   }

   @Override
   public <T> T readRow(TableComponentType tableComponentType, List<String> searchCriteria, Class<T> clazz, TableField<T>... fields) {
      return (T) fakeReadRowResult;
   }

   @Override
   public <T> void insertCellValue(TableComponentType tableComponentType, int row, Class<T> classType, T data) {
   }

   @Override
   public <T> void insertCellValue(TableComponentType tableComponentType, int row, Class<T> classType, TableField<T> field, int index, String... value) {
   }

   @Override
   public <T> void insertCellValue(TableComponentType tableComponentType, List<String> searchCriteria, Class<T> classType, TableField<T> field, int index, String... values) {
   }

   @Override
   public <T> void insertCellValue(TableComponentType tableComponentType, List<String> searchCriteria, Class<T> classType, T data) {
   }

   @Override
   public <T> void filterTable(TableComponentType tableComponentType, Class<T> tClass, TableField<T> column, FilterStrategy filterStrategy, String... values) {
   }

   @Override
   public <T> void sortTable(TableComponentType tableComponentType, Class<T> tClass, TableField<T> column, SortingStrategy sortingStrategy) {
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> List<AssertionResult<T>> validate(Object table, Assertion... assertions) {
      return (List<AssertionResult<T>>) (Object) fakeValidateResults;
   }
}