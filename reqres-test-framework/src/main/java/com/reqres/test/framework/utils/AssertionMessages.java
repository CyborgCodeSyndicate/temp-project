package com.reqres.test.framework.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AssertionMessages {

    public static final String USER_DATA_SIZE_INCORRECT = "User data size not correct!";
    public static final String FIRST_NAME_LENGTH_INCORRECT = "Name length incorrect!";

    public String userWithFirstNameNotFound(String firstName) {
        return String.format("User with first name '%s' not found", firstName);
    }

}