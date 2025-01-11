package com.theairebellion.zeus.db.validator;

import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;

import java.util.List;

public interface QueryResponseValidator {

    <T> List<AssertionResult<T>> validateQueryResponse(QueryResponse queryResponse, Assertion<?>... assertions);

}
