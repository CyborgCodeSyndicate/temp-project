package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.data.DataProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TestStaticData {

    Class<? extends DataProvider> value();

}
