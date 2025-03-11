package com.theairebellion.zeus.db.config;

import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.Converter;

import java.lang.reflect.Method;

import static com.theairebellion.zeus.db.config.DbConfigHolder.getDbConfig;

public class DbTypeConverter implements Converter<DbType> {


    @Override
    public DbType convert(Method method, String input) {
        DbConfig dbConfig = getDbConfig();
        final Class<? extends Enum> enumClass = ReflectionUtil.findEnumClassImplementationsOfInterface(
                DbType.class, dbConfig.projectPackage()
        );
        final Enum<?> enumValue = Enum.valueOf(enumClass, input);
        return (DbType) enumValue;

    }
}