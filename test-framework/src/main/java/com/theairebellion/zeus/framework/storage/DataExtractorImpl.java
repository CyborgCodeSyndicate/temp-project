package com.theairebellion.zeus.framework.storage;

import java.util.function.Function;

/**
 * A concrete implementation of {@link DataExtractor} for extracting typed data from a raw object.
 *
 * <p>This class allows for flexible data extraction by applying a custom function to
 * the retrieved storage object. It optionally supports a sub-key for nested or
 * segmented data structures.
 * <br><br>
 * Generic Type {@code T}: the type of data to be extracted from the raw object.
 *
 * <p>You can construct this class either with a sub-key or without it, depending on
 * how nested your data structure is. The {@code extractionLogic} function defines
 * exactly how the raw object is converted into the typed result {@code T}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class DataExtractorImpl<T> implements DataExtractor<T> {

   private Enum<?> subKey;
   private final Enum<?> key;
   private final Function<Object, T> extractionLogic;

   /**
    * Constructs a new {@code DataExtractorImpl} with a sub-key, primary key, and extraction logic.
    *
    * @param subKey          an optional sub-key used for nested data extraction
    * @param key             the primary key identifying the data to be extracted
    * @param extractionLogic a function that transforms the raw object into a typed result
    */
   public DataExtractorImpl(Enum<?> subKey,
                            Enum<?> key,
                            Function<Object, T> extractionLogic) {
      this.subKey = subKey;
      this.key = key;
      this.extractionLogic = extractionLogic;
   }

   /**
    * Constructs a new {@code DataExtractorImpl} without a sub-key, using only a primary key.
    *
    * @param key             the primary key identifying the data to be extracted
    * @param extractionLogic a function that transforms the raw object into a typed result
    */
   public DataExtractorImpl(
         Enum<?> key,
         Function<Object, T> extractionLogic) {
      this.key = key;
      this.extractionLogic = extractionLogic;
   }

   /**
    * Retrieves the optional sub-key used for nested data extraction.
    *
    * @return an {@link Enum} representing the sub-key, or {@code null} if not used
    */
   @Override
   public Enum<?> getSubKey() {
      return subKey;
   }

   /**
    * Retrieves the primary key identifying the data to be extracted.
    *
    * @return an {@link Enum} representing the primary key
    */
   @Override
   public Enum<?> getKey() {
      return key;
   }

   /**
    * Extracts and transforms the provided raw object into the target type {@code T}.
    *
    * @param rawObject the raw object retrieved from storage
    * @return the extracted data of type {@code T}
    */
   @Override
   public T extract(Object rawObject) {
      return extractionLogic.apply(rawObject);
   }

}
