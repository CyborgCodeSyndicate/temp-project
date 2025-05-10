package com.zerobank.project.ui.elements;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.selenium.ListUiElement;
import com.zerobank.project.ui.types.ListFieldTypes;
import org.openqa.selenium.By;

public enum ListFields implements ListUiElement {

   NAVIGATION_TABS(By.className("nav-tabs"), ListFieldTypes.BOOTSTRAP_LIST_TYPE),
   PAY_BILLS_TABS(By.className("ui-tabs-nav"), ListFieldTypes.BOOTSTRAP_LIST_TYPE),
   ACCOUNT_ACTIVITY_TABS(By.className("ui-tabs-nav"), ListFieldTypes.BOOTSTRAP_LIST_TYPE);

   private final By locator;
   private final ItemListComponentType componentType;


   ListFields(final By locator, final ItemListComponentType componentType) {
      this.locator = locator;
      this.componentType = componentType;
   }


   @Override
   public By locator() {
      return locator;
   }


   @Override
   public <T extends ComponentType> T componentType() {
      return (T) componentType;
   }


   @Override
   public Enum<?> enumImpl() {
      return this;
   }

}
