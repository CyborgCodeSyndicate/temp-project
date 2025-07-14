package com.theairebellion.zeus.db.config;

import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Driver;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DbTypeConverter Tests")
class DbTypeConverterTest {

    @Mock
    private DbConfig mockDbConfig;

    private final DbTypeConverter converter = new DbTypeConverter();


    private enum DummyDbType implements DbType {
        ALPHA;

        @Override
        public Driver driver() {
            return null;
        }

        @Override
        public String protocol() {
            return name().toLowerCase();
        }

        @Override
        public Enum<?> enumImpl() {
            return this;
        }
    }

    @Test
    @DisplayName("convert() returns the matching enum constant")
    void convertReturnsEnum() {
        try (MockedStatic<DbConfigHolder> dbConfigHolder = mockStatic(DbConfigHolder.class);
             MockedStatic<ReflectionUtil> refl = mockStatic(ReflectionUtil.class)) {

            // 1) Stub DbConfigHolder to return our mock config
            dbConfigHolder.when(DbConfigHolder::getDbConfig).thenReturn(mockDbConfig);
            when(mockDbConfig.projectPackage()).thenReturn("any.pkg");

            // 2) Stub ReflectionUtil to return exactly one implementing enum
            refl.when(() ->
                    ReflectionUtil.findEnumClassImplementationsOfInterface(
                            eq(DbType.class), eq("any.pkg"))
            ).thenReturn(List.of(DummyDbType.class));

            // Act
            DbType result = converter.convert(/*method*/ null, "ALPHA");

            // Assert
            assertSame(DummyDbType.ALPHA, result);
        }
    }

    @Test
    @DisplayName("convert() throws IndexOutOfBounds when no enums found")
    void convertThrowsWhenNoEnums() {
        try (MockedStatic<DbConfigHolder> dbConfigHolder = mockStatic(DbConfigHolder.class);
             MockedStatic<ReflectionUtil> refl = mockStatic(ReflectionUtil.class)) {

            dbConfigHolder.when(DbConfigHolder::getDbConfig).thenReturn(mockDbConfig);
            when(mockDbConfig.projectPackage()).thenReturn("none.pkg");

            refl.when(() ->
                    ReflectionUtil.findEnumClassImplementationsOfInterface(
                            eq(DbType.class), eq("none.pkg"))
            ).thenReturn(List.of());  // empty

            assertThrows(IndexOutOfBoundsException.class,
                    () -> converter.convert(null, "ANY"));
        }
    }

    @Test
    @DisplayName("convert() throws IllegalStateException when multiple enums found")
    void convertThrowsWhenMultipleEnums() {
        try (var dbConfigHolder = mockStatic(DbConfigHolder.class);
             var refl = mockStatic(ReflectionUtil.class)) {

            dbConfigHolder.when(DbConfigHolder::getDbConfig).thenReturn(mockDbConfig);
            when(mockDbConfig.projectPackage()).thenReturn("multi.pkg");

            refl.when(() ->
                    ReflectionUtil.findEnumClassImplementationsOfInterface(
                            eq(DbType.class), eq("multi.pkg"))
            ).thenReturn(List.of(DummyDbType.class, DummyDbType.class));

            IllegalStateException ex = assertThrows(
                    IllegalStateException.class,
                    () -> converter.convert(null, "ALPHA"),
                    "Should reject multiple enums"
            );
            assertTrue(ex.getMessage().contains("Only 1 is allowed"));
        }
    }

    @Test
    @DisplayName("convert() throws IllegalArgumentException on bad enum name")
    void convertThrowsOnInvalidName() {
        try (MockedStatic<DbConfigHolder> dbConfigHolder = mockStatic(DbConfigHolder.class);
             MockedStatic<ReflectionUtil> refl = mockStatic(ReflectionUtil.class)) {

            dbConfigHolder.when(DbConfigHolder::getDbConfig).thenReturn(mockDbConfig);
            when(mockDbConfig.projectPackage()).thenReturn("any.pkg");

            refl.when(() ->
                    ReflectionUtil.findEnumClassImplementationsOfInterface(
                            eq(DbType.class), eq("any.pkg"))
            ).thenReturn(List.of(DummyDbType.class));

            assertThrows(IllegalArgumentException.class,
                    () -> converter.convert(null, "GAMMA"));
        }
    }
}
