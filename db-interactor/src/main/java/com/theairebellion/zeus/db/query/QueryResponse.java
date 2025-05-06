package com.theairebellion.zeus.db.query;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents the response of a database query.
 *
 * <p>This class stores the result of a query execution, containing a list of rows where
 * each row is represented as a map of column names to their respective values.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
@ToString
@EqualsAndHashCode
public final class QueryResponse {

   /**
    * The result rows of the executed query.
    *
    * <p>Each row is represented as a {@code Map} where the key is the column name
    * and the value is the corresponding data.
    */
   private final List<Map<String, Object>> rows;


   public QueryResponse(final List<Map<String, Object>> rows) {
      if (rows == null) {
         throw new IllegalArgumentException("rows must not be null");
      }
      this.rows = Collections.unmodifiableList(rows);
   }

}
