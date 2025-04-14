package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.log.LogDb;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a database query with dynamic parameters.
 *
 * <p>This class allows the use of placeholders in SQL queries, which can be replaced
 * with actual values at runtime. It ensures safe and structured query modification.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class ParametrizedQuery implements DbQuery {

   private final DbQuery original;
   private final Map<String, Object> params = new HashMap<>();

   /**
    * Constructs a {@code ParametrizedQuery} based on an existing query.
    *
    * @param original The original {@link DbQuery} to parameterize.
    */
   public ParametrizedQuery(DbQuery original) {
      this.original = original;
   }

   /**
    * Returns the formatted query string with applied parameters.
    *
    * <p>Replaces placeholders in the format {@code {paramName}} with their actual values.
    *
    * @return The formatted SQL query.
    */
   @Override
   public String query() {
      String q = original.query();
      for (Map.Entry<String, Object> entry : params.entrySet()) {
         String placeholder = "{" + entry.getKey() + "}";
         q = q.replace(placeholder, entry.getValue() != null ? entry.getValue().toString() : "null");
      }
      return q;
   }

   /**
    * Retrieves the database configuration associated with this query.
    *
    * @return The {@link DatabaseConfiguration} for this query.
    */
   @Override
   public DatabaseConfiguration config() {
      return original.config();
   }

   /**
    * Retrieves the associated enumeration implementation for the query.
    *
    * @return The enum representing the query.
    */
   @Override
   public Enum<?> enumImpl() {
      return original.enumImpl();
   }

   /**
    * Adds a parameter to the query.
    *
    * <p>Creates a new instance with the updated parameters while preserving existing ones.
    *
    * @param name  The name of the placeholder parameter.
    * @param value The value to replace the placeholder.
    * @return A new {@code ParametrizedQuery} instance with the added parameter.
    */
   @Override
   public DbQuery withParam(String name, Object value) {
      LogDb.debug("Adding parameter '{}' with value '{}' to the query.", name, value);
      ParametrizedQuery copy = new ParametrizedQuery(this.original);
      copy.params.putAll(this.params);
      copy.params.put(name, value);
      return copy;
   }

}
