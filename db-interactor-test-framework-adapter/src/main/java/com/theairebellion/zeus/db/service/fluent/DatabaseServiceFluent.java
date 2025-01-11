package com.theairebellion.zeus.db.service.fluent;

import com.theairebellion.zeus.db.query.DbQuery;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.base.ClassLevelHook;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

import static com.theairebellion.zeus.db.storage.StorageKeysDb.DB;

@WorldName("DB")
@Service
@Scope("prototype")
@Lazy
public class DatabaseServiceFluent extends FluentService implements ClassLevelHook {

    private final DatabaseService databaseService;


    @Autowired
    public DatabaseServiceFluent(final DatabaseService databaseService) {
        this.databaseService = databaseService;
    }


    public <T> DatabaseServiceFluent query(DbQuery query) {
        QueryResponse queryResponse = databaseService.query(query);
        quest.getStorage().sub(DB).put(query.enumImpl(), queryResponse);
        return this;
    }


    public <T> DatabaseServiceFluent query(DbQuery query, String jsonPath, Class<T> resultType) {
        T queryResult = databaseService.query(query, jsonPath, resultType);
        quest.getStorage().sub(DB).put(query.enumImpl(), queryResult);
        return this;
    }


    public DatabaseServiceFluent validate(QueryResponse queryResponse, Assertion<?>... assertions) {
        List<AssertionResult<Object>> assertionResults = databaseService.validate(queryResponse, assertions);
        validation(assertionResults);
        return this;
    }


    public DatabaseServiceFluent queryAndValidate(DbQuery query, Assertion<?>... assertions) {
        QueryResponse queryResponse = databaseService.query(query);
        quest.getStorage().sub(DB).put(query.enumImpl(), queryResponse);
        return validate(queryResponse, assertions);
    }


    public DatabaseServiceFluent validate(Runnable assertion) {
        return (DatabaseServiceFluent) super.validate(assertion);
    }


    public DatabaseServiceFluent validate(Consumer<SoftAssertions> assertion) {
        return (DatabaseServiceFluent) super.validate(assertion);
    }


}
