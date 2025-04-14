package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.data.DataProvider;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a static data provider for test execution.
 *
 * <p>This annotation is used to define a class responsible for providing static test data.
 * The specified class must extend {@code DataProvider}, and its {@code testStaticData()} method
 * will be invoked to retrieve predefined data before the test execution.
 *
 * <p>The framework dynamically instantiates the data provider and retrieves the test data,
 * making it available within the test execution context.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TestStaticData {

   /**
    * Specifies the data provider class that supplies static test data.
    *
    * <p>The class must extend {@code DataProvider} and implement a method to return
    * predefined test data for use within the test.
    *
    * @return The class responsible for providing static test data.
    */
   Class<? extends DataProvider> value();

}
