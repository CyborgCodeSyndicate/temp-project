package com.theairebellion.zeus.framework.storage;

/**
 * Defines a contract for extracting typed data from a raw object retrieved from storage.
 *
 * <p>Implementations of this interface specify how to transform a stored object into a
 * typed result, allowing the framework to handle various data formats and structures
 * in a consistent manner.
 *
 * <p>The extraction process can optionally involve a sub-key for nested or hierarchical data,
 * enabling more granular data retrieval when necessary.
 *
 * @param <T> The type of the data to be extracted.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface DataExtractor<T> {

   /**
    * Retrieves the optional sub-key used for nested data extraction.
    *
    * <p>If this method returns {@code null}, no sub-key is used in the extraction process.
    *
    * @return An {@link Enum} representing the sub-key, or {@code null} if not applicable.
    */
   Enum<?> getSubKey();

   /**
    * Retrieves the primary key identifying the data to be extracted.
    *
    * <p>This key corresponds to a logical identifier within the storage system.
    *
    * @return An {@link Enum} representing the primary key.
    */
   Enum<?> getKey();

   /**
    * Extracts and transforms the provided raw object into the target type.
    *
    * <p>This method applies the extraction logic to convert the stored object into a typed result of type {@code T}.
    *
    * @param rawObject The raw object retrieved from storage.
    * @return The extracted data of type {@code T}.
    */
   T extract(Object rawObject);

}
