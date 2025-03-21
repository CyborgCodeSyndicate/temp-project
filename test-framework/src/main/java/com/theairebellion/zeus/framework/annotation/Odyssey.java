package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.extension.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation for managing the test execution lifecycle.
 * <p>
 * This annotation applies a set of predefined JUnit 5 extensions to test classes and methods,
 * ensuring that the necessary setup, logging, and cleanup mechanisms are consistently applied.
 * </p>
 *
 * <p>
 * By using {@code @Odyssey}, test classes automatically inherit these capabilities,
 * creating a standardized test execution environment without requiring manual extension management.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@ExtendWith({Oracle.class, Prologue.class, Epilogue.class, Craftsman.class, RipperMan.class, Initiator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Odyssey {

}
