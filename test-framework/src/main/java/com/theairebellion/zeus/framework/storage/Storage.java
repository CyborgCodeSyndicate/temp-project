package com.theairebellion.zeus.framework.storage;

import com.theairebellion.zeus.framework.parameters.Late;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.ParameterizedTypeReference;

import static com.theairebellion.zeus.framework.config.FrameworkConfigHolder.getFrameworkConfig;

/**
 * Manages temporary storage of test data within a single test execution.
 *
 * <p>This class provides a thread-safe mechanism for storing, retrieving, and organizing test data
 * using enum keys. Data can be stored at multiple levels, including a hierarchical sub-storage,
 * enabling structured data management during test execution.
 *
 * <p>The storage supports retrieval of data by key, by index, or by type, and it also supports
 * deferred data creation through the {@code Late} interface. Additionally, it offers methods
 * for extracting all matching values based on type.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class Storage {

   private final Map<Enum<?>, LinkedList<Object>> data = new ConcurrentHashMap<>();
   private static Enum<?> defaultStorageEnum;

   /**
    * Stores a value under the specified key.
    *
    * @param key   The enum key identifying the data.
    * @param value The data value to store.
    * @param <T>   The type of the data.
    */
   public <T> void put(Enum<?> key, T value) {
      data.computeIfAbsent(key, k -> new LinkedList<>()).add(value);
   }

   /**
    * Retrieves the latest stored value associated with the given key.
    *
    * @param key   The enum key identifying the data.
    * @param clazz The expected class of the data.
    * @param <T>   The type of the data.
    * @return The most recent value of type {@code T}, or {@code null} if not found.
    */
   public <T> T get(Enum<?> key, Class<T> clazz) {
      return getLatestValue(key, clazz, null);
   }

   /**
    * Retrieves the latest stored value using a type reference.
    *
    * @param key           The enum key identifying the data.
    * @param typeReference The parameterized type reference for the data.
    * @param <T>           The type of the data.
    * @return The most recent value of type {@code T}, or {@code null} if not found.
    */
   public <T> T get(Enum<?> key, ParameterizedTypeReference<T> typeReference) {
      return getLatestValue(key, null, typeReference);
   }

   /**
    * Retrieves a stored value by index using a {@code DataExtractor} and casts it to the expected type.
    *
    * @param extractor The data extractor defining how to extract the value.
    * @param clazz     The expected class of the extracted data.
    * @param index     The index (1-based) of the value to retrieve.
    * @param <T>       The type of the data.
    * @return The extracted value of type {@code T}.
    */
   public <T> T get(DataExtractor<T> extractor, Class<T> clazz, int index) {
      Object result = (extractor.getSubKey() != null)
            ? sub(extractor.getSubKey()).getByIndex(extractor.getKey(), index, Object.class)
            : getByIndex(extractor.getKey(), index, Object.class);

      return clazz.cast(extractor.extract(result));
   }

   /**
    * Retrieves a stored value using a {@code DataExtractor} without specifying an index.
    *
    * @param extractor The data extractor defining how to extract the value.
    * @param clazz     The expected class of the extracted data.
    * @param <T>       The type of the data.
    * @return The extracted value of type {@code T}.
    */
   public <T> T get(DataExtractor<T> extractor, Class<T> clazz) {
      Object result = (extractor.getSubKey() != null)
            ? sub(extractor.getSubKey()).get(extractor.getKey(), Object.class)
            : get(extractor.getKey(), Object.class);

      return clazz.cast(extractor.extract(result));
   }

   /**
    * Retrieves a value by its index from the stored data for the specified key.
    *
    * @param key   The enum key identifying the data.
    * @param index The index (1-based) to retrieve.
    * @param clazz The expected class of the value.
    * @param <T>   The type of the value.
    * @return The value at the specified index, or {@code null} if not found.
    */
   public <T> T getByIndex(Enum<?> key, int index, Class<T> clazz) {
      return getValueByIndex(key, index, clazz, null);
   }

   /**
    * Retrieves a value by its index using a type reference.
    *
    * @param key           The enum key identifying the data.
    * @param index         The index (1-based) to retrieve.
    * @param typeReference The parameterized type reference for the value.
    * @param <T>           The type of the value.
    * @return The value at the specified index, or {@code null} if not found.
    */
   public <T> T getByIndex(Enum<?> key, int index, ParameterizedTypeReference<T> typeReference) {
      return getValueByIndex(key, index, null, typeReference);
   }

   /**
    * Retrieves the latest stored value by filtering based on class.
    *
    * @param key   The enum key identifying the data.
    * @param clazz The expected class of the value.
    * @param <T>   The type of the value.
    * @return The most recent value of type {@code T}, or {@code null} if not found.
    */
   public <T> T getByClass(Enum<?> key, Class<T> clazz) {
      return findByClass(key, clazz, null);
   }

   /**
    * Retrieves the latest stored value by filtering based on a type reference.
    *
    * @param key           The enum key identifying the data.
    * @param typeReference The parameterized type reference for the value.
    * @param <T>           The type of the value.
    * @return The most recent value of type {@code T}, or {@code null} if not found.
    */
   public <T> T getByClass(Enum<?> key, ParameterizedTypeReference<T> typeReference) {
      return findByClass(key, null, typeReference);
   }

   /**
    * Retrieves all stored values that match the specified class.
    *
    * @param key   The enum key identifying the data.
    * @param clazz The expected class of the values.
    * @param <T>   The type of the values.
    * @return A list of all values of type {@code T} associated with the key.
    */
   public <T> List<T> getAllByClass(Enum<?> key, Class<T> clazz) {
      return findAllByClass(key, clazz, null);
   }

   /**
    * Retrieves all stored values that match the specified type reference.
    *
    * @param key           The enum key identifying the data.
    * @param typeReference The parameterized type reference for the values.
    * @param <T>           The type of the values.
    * @return A list of all values of type {@code T} associated with the key.
    */
   public <T> List<T> getAllByClass(Enum<?> key, ParameterizedTypeReference<T> typeReference) {
      return findAllByClass(key, null, typeReference);
   }

   /**
    * Retrieves a sub-storage instance associated with the given sub-key.
    *
    * <p>If no sub-storage exists for the given key, a new sub-storage is created and registered.
    *
    * @param subKey The enum key for the sub-storage.
    * @return The {@code Storage} instance corresponding to the sub-key.
    * @throws IllegalStateException if the key is already used for a non-storage value.
    */
   @SuppressFBWarnings(
         value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
         justification = "This write is intentional for initializing defaultStorageEnum under controlled conditions."
   )
   @SuppressWarnings("java:S2696")
   public Storage sub(Enum<?> subKey) {

      List<Object> values = data.get(subKey);
      if (values == null || values.isEmpty()) {
         if (defaultStorageEnum == null) {
            String defaultStorage = getFrameworkConfig().defaultStorage();
            if (subKey.name().equals(defaultStorage)) {
               defaultStorageEnum = subKey;
            }
         }
         Storage newSub = new Storage();
         data.put(subKey, new LinkedList<>(Collections.singletonList(newSub)));
         return newSub;
      }

      Object existingLatest = values.get(values.size() - 1);
      if (existingLatest instanceof Storage result) {
         return result;
      }

      throw new IllegalStateException(
            "Key " + subKey + " is already used for a non-storage value: " + existingLatest);
   }

   /**
    * Retrieves the default sub-storage.
    *
    * @return The default {@code Storage} instance.
    * @throws IllegalStateException if no default storage is initialized.
    */
   public Storage sub() {
      if (defaultStorageEnum == null) {
         throw new IllegalStateException("There is no default storage initialized");
      }
      return sub(defaultStorageEnum);
   }

   /**
    * Resolves any stored deferred values (instances of {@link Late}) by replacing them
    * with their actual evaluated objects.
    */
   public void joinLateArguments() {
      data.replaceAll((key, objects) -> {
         LinkedList<Object> updatedObjects = new LinkedList<>();
         for (Object o : objects) {
            if (o instanceof Late<?>) {
               try {
                  updatedObjects.add(((Late<?>) o).join());
               } catch (Exception ignored) {
                  //ignore failure
               }
            } else {
               updatedObjects.add(o);
            }
         }
         return updatedObjects;
      });
   }

   /**
    * Retrieves a value stored by a hook (e.g., DbHook or ApiHook) from the global storage map.
    *
    * <p>Looks up the HOOKS map under {@link StorageKeysTest#HOOKS} and returns the entry
    * associated with the given key object, cast to the specified type.</p>
    *
    * @param value the hook key under which data was stored
    * @param clazz the expected class of the stored value
    * @param <T>   the type of the returned object
    * @return the hook-stored value cast to {@code T}, or {@code null} if not present
    */
   public <T> T getHookData(Object value, Class<T> clazz) {
      Map<Object, Object> values = get(StorageKeysTest.HOOKS, Map.class);
      if (values == null || values.get(value) == null) {
         return null;
      }
      return clazz.cast(values.get(value));
   }

   /**
    * Returns a deep copy of all raw storage entries.
    *
    * <p>This method creates and returns a new map where each enum key is
    * mapped to a fresh list containing the same elements as the internal storage.
    * Modifications to the returned map or its lists will not affect the original storage.</p>
    *
    * @return a snapshot of the entire storage contents
    */
   @SuppressWarnings("java:S1452")
   public Map<Enum<?>, List<Object>> getData() {
      Map<Enum<?>, List<Object>> copy = new HashMap<>();
      for (Map.Entry<Enum<?>, LinkedList<Object>> entry : data.entrySet()) {
         copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
      }
      return copy;
   }

   /**
    * Attempts to cast the provided value to the specified class, returning null if the cast is not possible.
    *
    * @param value The value to be cast.
    * @param clazz The target class for casting.
    * @param <T>   The type to cast to.
    * @return The cast value of type {@code T}, or {@code null} if the value is not an instance of {@code clazz}.
    */
   @SuppressWarnings("unchecked")
   private <T> T castOrNull(Object value, Class<T> clazz) {
      return (clazz.isInstance(value)) ? (T) value : null;
   }

   /**
    * Attempts to cast the provided value to the specified type reference, returning null if the cast is not possible.
    *
    * @param value         The value to be cast.
    * @param typeReference The target parameterized type for casting.
    * @param <T>           The type to cast to.
    * @return The cast value of type {@code T}, or {@code null} if the value is not compatible.
    */
   @SuppressWarnings("unchecked")
   private <T> T castOrNullTypeRef(Object value, ParameterizedTypeReference<T> typeReference) {
      return (value != null && typeReference.getType().getTypeName().equals(value.getClass().getTypeName()))
            ? (T) value : null;
   }

   /**
    * Retrieves the latest value stored under the given key.
    *
    * @param key           The enum key identifying the data.
    * @param clazz         The expected class of the value, or {@code null} if using a type reference.
    * @param typeReference The parameterized type reference for the value, or {@code null} if using a class.
    * @param <T>           The type of the value.
    * @return The latest stored value cast to type {@code T}, or {@code null} if no value exists.
    */
   private <T> T getLatestValue(Enum<?> key, Class<T> clazz, ParameterizedTypeReference<T> typeReference) {
      List<Object> values = data.get(key);
      if (values == null || values.isEmpty()) {
         return null;
      }
      Object latest = values.get(values.size() - 1);
      return clazz != null ? castOrNull(latest, clazz) : castOrNullTypeRef(latest, typeReference);
   }

   /**
    * Retrieves a stored value by its index.
    *
    * @param key           The enum key identifying the data.
    * @param index         The 1-based index of the value to retrieve.
    * @param clazz         The expected class of the value, or {@code null} if using a type reference.
    * @param typeReference The parameterized type reference for the value, or {@code null} if using a class.
    * @param <T>           The type of the value.
    * @return The value at the specified index, or {@code null} if the index is invalid.
    */
   private <T> T getValueByIndex(Enum<?> key, int index, Class<T> clazz, ParameterizedTypeReference<T> typeReference) {
      List<Object> values = data.get(key);
      if (values == null || index < 1 || index > values.size()) {
         return null;
      }
      Object value = values.get(values.size() - index);
      return clazz != null ? castOrNull(value, clazz) : castOrNullTypeRef(value, typeReference);
   }

   /**
    * Finds the latest stored value that matches the specified class.
    *
    * @param key           The enum key identifying the data.
    * @param clazz         The expected class of the value, or {@code null} if using a type reference.
    * @param typeReference The parameterized type reference for the value, or {@code null} if using a class.
    * @param <T>           The type of the value.
    * @return The latest matching value, or {@code null} if none found.
    */
   private <T> T findByClass(Enum<?> key, Class<T> clazz, ParameterizedTypeReference<T> typeReference) {
      List<Object> values = data.get(key);
      if (values == null) {
         return null;
      }
      for (int i = values.size() - 1; i >= 0; i--) {
         Object value = values.get(i);
         T casted = clazz != null ? castOrNull(value, clazz) : castOrNullTypeRef(value, typeReference);
         if (casted != null) {
            return casted;
         }
      }
      return null;
   }

   /**
    * Retrieves all stored values that match the specified class.
    *
    * @param key           The enum key identifying the data.
    * @param clazz         The expected class of the values, or {@code null} if using a type reference.
    * @param typeReference The parameterized type reference for the values, or {@code null} if using a class.
    * @param <T>           The type of the values.
    * @return A list of all matching values, or an empty list if none found.
    */
   private <T> List<T> findAllByClass(Enum<?> key, Class<T> clazz, ParameterizedTypeReference<T> typeReference) {
      List<Object> values = data.get(key);
      List<T> result = new ArrayList<>();
      if (values != null) {
         for (Object value : values) {
            T casted = clazz != null ? castOrNull(value, clazz) : castOrNullTypeRef(value, typeReference);
            if (casted != null) {
               result.add(casted);
            }
         }
      }
      return result;
   }

}
