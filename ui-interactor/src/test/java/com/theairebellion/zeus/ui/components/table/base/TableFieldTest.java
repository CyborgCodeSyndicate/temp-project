package com.theairebellion.zeus.ui.components.table.base;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.base.mock.Mock;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableFieldTest extends BaseUnitUITest {

   @Test
   public void testInvoke() throws IllegalAccessException, InvocationTargetException {
      TableField<Mock> field = TableField.of(Mock::setValue);
      Mock mock = new Mock();
      field.invoke(mock, "test");
      assertEquals("test", mock.getValue());
   }
}