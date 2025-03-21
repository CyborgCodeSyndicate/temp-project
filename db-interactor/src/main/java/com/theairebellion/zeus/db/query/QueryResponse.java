package com.theairebellion.zeus.db.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Represents the response of a database query.
 * <p>
 * This class stores the result of a query execution, containing a list of rows where
 * each row is represented as a map of column names to their respective values.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Data
@AllArgsConstructor
public class QueryResponse {

    /**
     * The result rows of the executed query.
     * <p>
     * Each row is represented as a {@code Map} where the key is the column name
     * and the value is the corresponding data.
     * </p>
     */
    private List<Map<String, Object>> rows;
}
