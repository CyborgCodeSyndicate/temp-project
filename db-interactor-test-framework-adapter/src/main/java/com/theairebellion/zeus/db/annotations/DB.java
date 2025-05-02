package com.theairebellion.zeus.db.annotations;

import com.theairebellion.zeus.db.extensions.DbHookExtension;
import com.theairebellion.zeus.db.extensions.DbTestExtension;
import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Marks a test class as a database-related test.
 *
 * <p>This annotation integrates database testing functionality into test classes.
 * It ensures database connections are properly managed by applying the
 * {@link DbTestExtension}, which automatically closes connections after test execution.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ExtendWith({DbTestExtension.class, DbHookExtension.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@FrameworkAdapter(basePackages = {"com.theairebellion.zeus.db"})
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public @interface DB {
}
