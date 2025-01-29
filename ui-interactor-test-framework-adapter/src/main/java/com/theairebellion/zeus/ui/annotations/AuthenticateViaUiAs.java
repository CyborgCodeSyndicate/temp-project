package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.authentication.LoginCredentials;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthenticateViaUiAs {

    Class<? extends LoginCredentials> credentials();

    Class<? extends BaseLoginClient> type();

    boolean cacheCredentials() default false;

}
