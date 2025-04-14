package com.theairebellion.zeus.ui.enums;

import lombok.Getter;

/**
 * Enum representing various UI features available in the framework.
 *
 * <p>Each feature corresponds to a specific UI component or functionality within the test automation framework.
 * These features help in organizing and accessing different UI-related services dynamically.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
public enum Features {

   /**
    * Represents input field interactions in the UI.
    */
   INPUT_FIELDS("inputField"),

   /**
    * Represents table interactions in the UI.
    */
   TABLE("table"),

   /**
    * Represents button field interactions in the UI.
    */
   BUTTON_FIELDS("buttonField"),

   /**
    * Represents radio button field interactions in the UI.
    */
   RADIO_FIELDS("radioField"),

   /**
    * Represents checkbox field interactions in the UI.
    */
   CHECKBOX_FIELDS("checkboxField"),

   /**
    * Represents toggle switch field interactions in the UI.
    */
   TOGGLE_FIELDS("toggleField"),

   /**
    * Represents select dropdown interactions in the UI.
    */
   SELECT_FIELDS("selectField"),

   /**
    * Represents list field interactions in the UI.
    */
   LIST_FIELDS("listField"),

   /**
    * Represents loader field interactions in the UI.
    */
   LOADER_FIELDS("loaderField"),

   /**
    * Represents hyperlink interactions in the UI.
    */
   LINK_FIELDS("linkField"),

   /**
    * Represents alert pop-up interactions in the UI.
    */
   ALERT_FIELDS("alertField"),

   /**
    * Represents tab navigation interactions in the UI.
    */
   TAB_FIELDS("tabField"),

   /**
    * Represents request interception functionality.
    */
   REQUESTS_INTERCEPTOR("interceptor"),

   /**
    * Represents validation mechanisms within the framework.
    */
   VALIDATION("validation"),

   /**
    * Represents navigation actions in the UI.
    */
   NAVIGATION("navigation"),

   /**
    * Represents data insertion functionality for UI components.
    */
   DATA_INSERTION("insertionService");

   /**
    * The associated field name for the feature.
    */
   private final String fieldName;

   /**
    * Constructs a UI feature with the specified field name.
    *
    * @param fieldName The corresponding field name for the feature.
    */
   Features(final String fieldName) {
      this.fieldName = fieldName;
   }
}
