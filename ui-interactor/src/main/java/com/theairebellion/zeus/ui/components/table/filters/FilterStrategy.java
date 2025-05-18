package com.theairebellion.zeus.ui.components.table.filters;

/**
 * Defines filtering strategies for table elements.
 *
 * <p>These strategies determine how table elements are filtered,
 * allowing selection and deselection actions.
 *
 * <p>Currently, there are no direct usages, but it is intended
 * to be used in conjunction with filtering functionalities
 * such as {@link CellFilterFunction}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum FilterStrategy {

   /**
    * Selects only the specified elements, deselecting others.
    */
   SELECT_ONLY,

   /**
    * Selects the specified elements without affecting others.
    */
   SELECT,

   /**
    * Selects all available elements.
    */
   SELECT_ALL,

   /**
    * Unselects the specified elements without affecting others.
    */
   UNSELECT,

   /**
    * Unselects all elements.
    */
   UNSELECT_ALL
}
