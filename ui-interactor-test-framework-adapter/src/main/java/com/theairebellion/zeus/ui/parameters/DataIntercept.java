package com.theairebellion.zeus.ui.parameters;

/**
 * Defines a data interception contract for UI-driven API interactions.
 *
 * <p>Implementations specify which endpoint responses to intercept by providing
 * a substring match, along with an enum constant to identify the intercept flow.
 *
 * @param <T> the enum type used to identify specific intercept implementations
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface DataIntercept<T extends Enum<T>> {

   /**
    * Returns the substring used to match the target endpoint for interception.
    *
    * <p>This substring is used to filter API calls whose URL contains it.
    *
    * @return the endpoint substring to intercept
    */
   String getEndpointSubString();

   /**
    * Returns the enum constant identifying this data intercept implementation.
    *
    * <p>The returned enum value is used to look up the corresponding interception logic.
    *
    * @return the enum representing this intercept
    */
   T enumImpl();

}
