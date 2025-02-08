package com.theairebellion.zeus.db.retry;

import com.theairebellion.zeus.db.query.DbQuery;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.framework.retry.RetryConditionImpl;

import java.util.function.Predicate;

public class RetryConditionDb {

    public static RetryCondition<Boolean> queryReturnsRows(DbQuery query) {
        return new RetryConditionImpl<>(
            service -> {
                DatabaseService databaseService = (DatabaseService) service;
                QueryResponse queryResponse = databaseService.query(query);
                return !queryResponse.getRows().isEmpty();
            }, Predicate.isEqual(true)
        );
    }


    public static RetryCondition<Object> queryReturnsValueForField(DbQuery query, String jsonPath, Object value) {
        return new RetryConditionImpl<>(
            service -> {
                DatabaseService databaseService = (DatabaseService) service;
                return databaseService.query(query, jsonPath, value.getClass());
            }, object -> object.equals(value)
        );
    }


}
