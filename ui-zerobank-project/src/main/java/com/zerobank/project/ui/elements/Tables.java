package com.zerobank.project.ui.elements;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.tables.TableElement;
import com.zerobank.project.model.AllTransactionEntry;
import com.zerobank.project.model.CreditAccounts;
import com.zerobank.project.model.DetailedReport;
import com.zerobank.project.model.FilteredTransactionEntry;
import com.zerobank.project.model.OutFlow;
import com.zerobank.project.ui.functions.SharedUiFunctions;
import java.util.function.Consumer;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.service.tables.DefaultTableTypes.DEFAULT;
import static com.zerobank.project.ui.elements.TableTypes.SIMPLE;

public enum Tables implements TableElement<Tables> {

   CAMPAIGNS(AllTransactionEntry.class),
   FILTERED_TRANSACTIONS(FilteredTransactionEntry.class),
   ALL_TRANSACTIONS(AllTransactionEntry.class),
   CREDIT_ACCOUNTS(CreditAccounts.class),
   OUTFLOW(OutFlow.class, DEFAULT,
         driver -> SharedUiFunctions.waitForPresence(driver, By.id("report-1016"))),
   DETAILED_REPORT(DetailedReport.class, DEFAULT,
         driver -> SharedUiFunctions.waitForPresence(driver, By.id("detailedreport-1041"))),
   ORDERS(AllTransactionEntry.class, SIMPLE);


   private final Class<?> rowRepresentationClass;
   private final TableComponentType tableType;
   private final Consumer<SmartWebDriver> before;
   private final Consumer<SmartWebDriver> after;


   <T> Tables(final Class<T> rowRepresentationClass) {
      this(rowRepresentationClass, null, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }


   <T> Tables(final Class<T> rowRepresentationClass, TableComponentType tableType) {
      this(rowRepresentationClass, tableType, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }


   <T> Tables(final Class<T> rowRepresentationClass, TableComponentType tableType,
              Consumer<SmartWebDriver> before) {
      this(rowRepresentationClass, tableType, before, smartWebDriver -> {
      });
   }


   <T> Tables(final Class<T> rowRepresentationClass, TableComponentType tableType,
              Consumer<SmartWebDriver> before, Consumer<SmartWebDriver> after) {
      this.rowRepresentationClass = rowRepresentationClass;
      this.tableType = tableType;
      this.before = before;
      this.after = after;
   }


   @Override
   public <T extends TableComponentType> T tableType() {
      if (tableType != null) {
         return (T) tableType;
      } else {
         return TableElement.super.tableType();
      }
   }


   @Override
   public <T> Class<T> rowsRepresentationClass() {
      return (Class<T>) rowRepresentationClass;
   }


   @Override
   public Tables enumImpl() {
      return this;
   }


   @Override
   public Consumer<SmartWebDriver> before() {
      return before;
   }


   @Override
   public Consumer<SmartWebDriver> after() {
      return after;
   }

}
