package com.theairebellion.zeus.framework.data;

import java.util.Map;

/**
 * Defines a contract for providing static test data.
 *
 * <p>Implementing classes must define a method that returns a predefined
 * set of key-value pairs to be used as test data during execution.
 *
 * <p>This interface is primarily used with annotations that specify static
 * data providers, allowing the framework to dynamically load and inject
 * test data before test execution.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface DataProvider {

   /**
    * Provides a set of predefined static test data.
    *
    * <p>The returned map contains key-value pairs representing test data
    * that can be accessed during test execution.
    *
    * @return A map containing static test data.
    */
   Map<String, Object> testStaticData();

}
