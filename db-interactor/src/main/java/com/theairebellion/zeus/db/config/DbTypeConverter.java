package com.theairebellion.zeus.db.config;

import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.lang.reflect.Method;
import java.util.List;
import org.aeonbits.owner.Converter;

import static com.theairebellion.zeus.db.config.DbConfigHolder.getDbConfig;

public class DbTypeConverter implements Converter<DbType> {


   @Override
   public DbType convert(Method method, String input) {
      DbConfig dbConfig = getDbConfig();

      List<Class<? extends Enum<?>>> enumClassImplementationsOfInterface =
            ReflectionUtil.findEnumClassImplementationsOfInterface(DbType.class, dbConfig.projectPackage());

      if (enumClassImplementationsOfInterface.size() > 1) {
         throw new IllegalStateException(
               "There is more than one enum for representing different types of databases. Only 1 is allowed");
      }

      Class<? extends Enum> enumClass = enumClassImplementationsOfInterface.get(0);

      final Enum<?> enumValue = Enum.valueOf(enumClass, input);
      return (DbType) enumValue;

   }
}