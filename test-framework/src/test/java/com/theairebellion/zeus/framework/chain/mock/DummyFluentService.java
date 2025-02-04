package com.theairebellion.zeus.framework.chain.mock;

import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.AssertionResult;

import java.util.List;

public class DummyFluentService extends FluentService {

    public void callValidation(List<AssertionResult<Object>> results) {
        validation(results);
    }
}
