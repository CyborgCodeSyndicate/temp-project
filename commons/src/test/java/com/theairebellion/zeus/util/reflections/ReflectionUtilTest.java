package com.theairebellion.zeus.util.reflections;

import com.theairebellion.zeus.util.reflections.exceptions.ReflectionException;
import com.theairebellion.zeus.util.reflections.mock.MockEnum;
import com.theairebellion.zeus.util.reflections.mock.MockInterface;
import com.theairebellion.zeus.util.reflections.mock.TestClass;
import com.theairebellion.zeus.util.reflections.mock.TestClassWithPrivateField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

class ReflectionUtilTest {

    private static final String MOCK_PACKAGE = "com.theairebellion.zeus.util.reflections.mock";
    private static final String VALUE_NAME = "VALUE";
    private static final String SOME_FIELD = "someField";
    private static final String TEST_VALUE = "TestValue";

    @Test
    void testFindEnumClassImplementationsOfInterface_shouldReturnEnum() {
        Class<? extends Enum<?>> result =
                ReflectionUtil.findEnumClassImplementationsOfInterface(MockInterface.class, MOCK_PACKAGE);
        assertEquals(MockEnum.class, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testFindEnumClassImplementationsOfInterface_shouldThrowOnInvalidPackage(String pkg) {
        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.findEnumClassImplementationsOfInterface(MockInterface.class, pkg));
    }

    @Test
    void testFindEnumImplementationsOfInterface_shouldReturnEnumValue() {
        MockInterface result = ReflectionUtil.findEnumImplementationsOfInterface(
                MockInterface.class, VALUE_NAME, MOCK_PACKAGE);
        assertEquals(MockEnum.VALUE, result);
    }

    @Test
    void testFindEnumImplementationsOfInterface_nullEnumName() {
        ReflectionException ex = assertThrows(ReflectionException.class,
                () -> ReflectionUtil.findEnumImplementationsOfInterface(MockInterface.class, null, MOCK_PACKAGE));
        assertTrue(ex.getMessage().contains("Enum value 'null' not found"));
    }

    @Test
    void testFindImplementationsOfInterface_shouldReturnClasses() {
        List<Class<? extends MockInterface>> classes =
                ReflectionUtil.findImplementationsOfInterface(MockInterface.class, MOCK_PACKAGE);
        assertAll(
                () -> assertFalse(classes.isEmpty()),
                () -> assertTrue(classes.contains(TestClass.class)),
                () -> assertTrue(classes.contains(MockEnum.class))
        );
    }

    @Test
    void testFindImplementationsOfInterface_shouldThrowOnNullOrEmptyPackage() {
        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.findImplementationsOfInterface(MockInterface.class, null));
        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.findImplementationsOfInterface(MockInterface.class, "  "));
    }

    @Test
    void testGetFieldValue_shouldReturnValueIfFieldMatches() {
        TestClass testObject = new TestClass();
        testObject.someField = TEST_VALUE;
        String result = ReflectionUtil.getFieldValue(testObject, String.class);
        assertEquals(TEST_VALUE, result);
    }

    @Test
    void testGetFieldValue_fieldExistsButNullValue() {
        TestClass testObject = new TestClass();
        testObject.someField = null;
        ReflectionException ex = assertThrows(ReflectionException.class,
                () -> ReflectionUtil.getFieldValue(testObject, String.class));
        assertTrue(ex.getMessage().contains("Field value is not of the expected type"));
    }

    @Test
    void testGetFieldValue_noAssignableFieldFound() {
        TestClass testObject = new TestClass();
        ReflectionException ex = assertThrows(ReflectionException.class,
                () -> ReflectionUtil.getFieldValue(testObject, Double.class));
        assertTrue(ex.getMessage().contains("No field of type 'java.lang.Double' found"));
    }

    @Test
    void testGetAttributeOfClass_shouldReturnValueIfFieldExists() {
        TestClass testObject = new TestClass();
        testObject.someField = TEST_VALUE;
        String result = ReflectionUtil.getAttributeOfClass(SOME_FIELD, testObject, String.class);
        assertEquals(TEST_VALUE, result);
    }

    @Test
    void testGetAttributeOfClass_fieldIsNull() {
        TestClass testObject = new TestClass();
        testObject.someField = null;
        ReflectionException ex = assertThrows(ReflectionException.class,
                () -> ReflectionUtil.getAttributeOfClass(SOME_FIELD, testObject, String.class));
        assertTrue(ex.getMessage().contains("Field 'someField' value is not of expected type"));
    }

    @Test
    void testGetAttributeOfClass_fieldDoesNotExist() {
        TestClass testObject = new TestClass();
        ReflectionException ex = assertThrows(ReflectionException.class,
                () -> ReflectionUtil.getAttributeOfClass("nonExistentField", testObject, String.class));
        assertTrue(ex.getMessage().contains("Field 'nonExistentField' not found in class"));
    }

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<ReflectionUtil> ctor = ReflectionUtil.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        ReflectionUtil instance = ctor.newInstance();
        assertNotNull(instance);
    }

    @Test
    void testGetFieldValue_shouldReadPrivateField() {
        TestClassWithPrivateField obj = new TestClassWithPrivateField();
        String result = ReflectionUtil.getFieldValue(obj, String.class);
        assertEquals("secret", result);
    }

    @ParameterizedTest
    @MethodSource("fieldAccessScenarios")
    void testGetFieldValue_variousScenarios(Class<?> type, Object instance, Class<? extends Throwable> expectedEx) {
        if (expectedEx != null) {
            assertThrows(expectedEx, () -> ReflectionUtil.getFieldValue(instance, type));
        } else {
            assertDoesNotThrow(() -> ReflectionUtil.getFieldValue(instance, type));
        }
    }

    @Test
    void testValidateInputsCoverage() {

        assertThrows(ReflectionException.class,
                () -> ReflectionUtil.findEnumImplementationsOfInterface(MockInterface.class, "", MOCK_PACKAGE));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.findEnumImplementationsOfInterface(MockInterface.class, "VALUE", ""));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.findEnumClassImplementationsOfInterface(null, "somePackage"));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.findEnumClassImplementationsOfInterface(MockInterface.class, ""));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.findImplementationsOfInterface(null, "packagePrefix"));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.findImplementationsOfInterface(MockInterface.class, ""));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.getFieldValue(null, String.class));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.getFieldValue("someObj", null));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.getAttributeOfClass(null, new Object(), String.class));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.getAttributeOfClass("", new Object(), String.class));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.getAttributeOfClass("someField", null, String.class));

        assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtil.getAttributeOfClass("someField", new Object(), null));
    }

    @Test
    void testFindEnumClassImplementationsOfInterface_noResults() {
        ReflectionException ex = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.findEnumClassImplementationsOfInterface(
                        MockInterface.class,
                        "com.some.fake.package.that.does.not.exist"
                )
        );
        assertTrue(ex.getMessage().contains("No Enum implementing interface"));
    }

    @Test
    void testFindClassThatExtendsClass_noSubclass() {
        @SuppressWarnings("unchecked")
        Class<Integer> result = (Class<Integer>) ReflectionUtil.findClassThatExtendsClass(Integer.class, "java.lang");
        assertNull(result);
    }

    @Test
    void testFindClassThatExtendsClass_withSubclass() {
        class Parent {}
        class Child extends Parent {}
        String packageName = this.getClass().getPackageName();
        Class<? extends Parent> result = ReflectionUtil.findClassThatExtendsClass(Parent.class, packageName);
        assertNotNull(result);
        assertTrue(Child.class.isAssignableFrom(result));
    }

    @Test
    void testGetAttributeOfClass_illegalAccess() {
        assertThrows(ReflectionException.class, () -> {
            ReflectionUtil.getAttributeOfClass("nonExistentField", new Object(), String.class);
        });
    }

    @Test
    void testGetFieldValue_illegalAccess() {
        assertThrows(ReflectionException.class, () -> {
            ReflectionUtil.getFieldValue(new Object(), String.class);
        });
    }

    static Stream<Arguments> fieldAccessScenarios() {
        return Stream.of(
                Arguments.of(String.class, new TestClass(), ReflectionException.class),
                Arguments.of(Integer.class, new TestClass(), ReflectionException.class),
                Arguments.of(String.class, new TestClassWithPrivateField(), null)
        );
    }
}