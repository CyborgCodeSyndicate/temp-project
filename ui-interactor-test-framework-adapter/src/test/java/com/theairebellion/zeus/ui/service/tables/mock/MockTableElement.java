package com.theairebellion.zeus.ui.service.tables.mock;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.tables.DefaultTableTypes;
import com.theairebellion.zeus.ui.service.tables.TableElement;
import java.util.function.Consumer;

public class MockTableElement implements TableElement<DefaultTableTypes> {

   @Override
   public <T> Class<T> rowsRepresentationClass() {
      return (Class<T>) MockRowClass.class;
   }

   @Override
   public DefaultTableTypes enumImpl() {
      return DefaultTableTypes.DEFAULT;
   }

   @Override
   public Consumer<SmartWebDriver> before() {
      return driver -> {
      };
   }

   @Override
   public Consumer<SmartWebDriver> after() {
      return driver -> {
      };
   }
}