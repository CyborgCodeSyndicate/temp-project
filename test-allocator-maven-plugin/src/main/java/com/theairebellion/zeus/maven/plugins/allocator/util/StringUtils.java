package com.theairebellion.zeus.maven.plugins.allocator.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for string-related operations.
 *
 * <p>Provides helper methods to process and manipulate strings.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public final class StringUtils {

   /**
    * Private constructor to prevent instantiation of utility class.
    */
   private StringUtils() {
   }

   /**
    * Parses a comma-separated string into a set of trimmed strings.
    *
    * <p>This method splits the input string by commas, trims each element, and returns
    * a set of unique values. Empty or null input results in an empty set.
    *
    * @param value The comma-separated string to parse.
    * @return A {@link Set} of trimmed strings, or an empty set if input is null or empty.
    */
   public static Set<String> parseCommaSeparated(String value) {
      Set<String> result = new HashSet<>();
      if (value == null || value.trim().isEmpty()) {
         return result;
      }

      String[] parts = value.split(",");
      for (String part : parts) {
         result.add(part.trim());
      }

      return result;
   }

}
