package com.theairebellion.zeus.api.annotations;

import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.authentication.Credentials;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthenticateViaApiAs {

    Class<? extends Credentials> credentials();

    Class<? extends BaseAuthenticationClient> type();

    boolean cacheCredentials() default false;


}
