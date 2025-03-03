package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;

import java.util.List;

public interface UiTableValidator {

    <T> List<AssertionResult<T>> validateTable(Object object, Assertion<?>... assertions);


}
