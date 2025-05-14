package com.theairebellion.zeus.ui.testutil;

public class BaseUnitUITest {

   static {
      System.setProperty("project.package", "com.theairebellion.zeus");
      System.setProperty("ui.config.file", "ui-config");
      System.setProperty("wait.duration.in.seconds", "2");
      System.setProperty("accordion.default.type", "DUMMY_ACCORDION");
      System.setProperty("alert.default.type", "DUMMY_ALERT");
      System.setProperty("button.default.type", "DUMMY_BUTTON");
      System.setProperty("checkbox.default.type", "DUMMY_CHECKBOX");
      System.setProperty("input.default.type", "DUMMY_INPUT");
      System.setProperty("link.default.type", "DUMMY_LINK");
      System.setProperty("list.default.type", "DUMMY_LIST");
      System.setProperty("loader.default.type", "DUMMY_LOADER");
      System.setProperty("modal.default.type", "DUMMY_MODAL");
      System.setProperty("radio.default.type", "DUMMY_RADIO");
      System.setProperty("select.default.type", "DUMMY_SELECT");
      System.setProperty("tab.default.type", "DUMMY_TAB");
      System.setProperty("table.default.type", "DUMMY_TABLE");
      System.setProperty("toggle.default.type", "DUMMY_TOGGLE");
   }
}
