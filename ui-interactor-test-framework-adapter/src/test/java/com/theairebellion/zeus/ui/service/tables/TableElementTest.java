package com.theairebellion.zeus.ui.service.tables;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

@DisplayName("TableElement Tests")
class TableElementTest {

   TableElement<SampleEnum> defaultImpl;

   enum SampleEnum {
      USERS_TABLE
   }

   enum DummyEnum {
      TEST
   }

   @BeforeEach
   void setUp() {
      defaultImpl = new TableElement<>() {
         @Override
         public Class<String> rowsRepresentationClass() {
            return String.class;
         }

         @Override
         public SampleEnum enumImpl() {
            return SampleEnum.USERS_TABLE;
         }
      };
   }

   @Test
   @DisplayName("default before/after should not throw and be no-op")
   void defaultBeforeAndAfterShouldBeSafe() {
      SmartWebDriver driver = mock(SmartWebDriver.class);

      // This triggers the default 'before' and 'after' methods
      defaultImpl.before().accept(driver);
      defaultImpl.after().accept(driver);

      // You may verify driver is untouched (noop behavior)
      verifyNoInteractions(driver);
   }

   @Test
   @DisplayName("default should return default table type")
   void shouldReturnDefaultTableType() {
      // Anonymous class without overriding tableType()
      TableElement<DummyEnum> element = new TableElement<>() {
         @Override
         public Class<String> rowsRepresentationClass() {
            return String.class;
         }

         @Override
         public DummyEnum enumImpl() {
            return DummyEnum.TEST;
         }
      };

      TableComponentType type = element.tableType();

      // Asserting the default value
      assertThat(type).isSameAs(DefaultTableTypes.DEFAULT);
   }
}
