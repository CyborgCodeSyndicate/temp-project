package com.theairebellion.zeus.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Marks a test service as a Spring-managed component with a prototype scope.
 *
 * <p>This annotation is used to define test-related services that should be managed by Spring.
 * It combines {@code @Service}, {@code @Scope("prototype")}, and {@code @Lazy}, ensuring that
 * each test service instance is created on demand and is not shared across the test lifecycle.
 *
 * <p>The {@code value} attribute allows specifying the type of service (e.g., "API", "DB", "UI"),
 * which can be useful for categorization or retrieval in the test framework.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
@Scope("prototype")
@Lazy
public @interface TestService {

   /**
    * Specifies the type or category of the test service.
    *
    * <p>This value can be used for identification or configuration within the test framework.
    *
    * @return The name of the test service category.
    */
   String value();
}
