package com.theairebellion.zeus.db.config;

import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.Converter;

import java.lang.reflect.Method;
import java.util.List;

import static com.theairebellion.zeus.db.config.DbConfigHolder.getDbConfig;

/**
 * Converter implementation for {@link DbType} when loading properties via OWNER.
 * <p>
 * Reads the configured {@link DbConfig#getDbConfig() DbConfig},
 * uses reflection to locate the single enum in the project that implements {@link DbType},
 * and returns the corresponding enum constant for the given string input.
 * </p>
 * <p>
 * If more than one {@code enum} implementing {@link DbType} is found,
 * this converter will fail fast with an {@link IllegalStateException}.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class DbTypeConverter implements Converter<DbType> {

    /**
     * Convert a {@code String} property into a {@link DbType} enum instance.
     *
     * @param method the {@link Method} annotated with the {@code @Key} for which this conversion applies
     * @param input  the raw String value from configuration (e.g. "POSTGRES")
     * @return the matching {@link DbType} enum constant
     * @throws IllegalStateException if zero or more than one enum implementing {@link DbType} is found
     * @throws IllegalArgumentException if the given {@code input} does not match any constant in the enum
     */
    @Override
    public DbType convert(Method method, String input) {
        DbConfig dbConfig = getDbConfig();

        List<Class<? extends Enum<?>>> enumClassImplementationsOfInterface = ReflectionUtil.findEnumClassImplementationsOfInterface(
            DbType.class, dbConfig.projectPackage()
        );

        if (enumClassImplementationsOfInterface.size() > 1) {
            throw new IllegalStateException(
                "There is more than one enum for representing different types of databases. Only 1 is allowed");
        }

        Class<? extends Enum> enumClass = enumClassImplementationsOfInterface.get(0);

        final Enum<?> enumValue = Enum.valueOf(enumClass, input);
        return (DbType) enumValue;

    }
}