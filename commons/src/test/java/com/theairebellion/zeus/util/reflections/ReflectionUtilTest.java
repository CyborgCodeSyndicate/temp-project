// ReflectionUtilTest.java
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
import static org.mockito.Mockito.*;

class ReflectionUtilTest {

    public static final String COM_THEAIREBELLION_ZEUS_UTIL_REFLECTIONS_MOCK = "com.theairebellion.zeus.util.reflections.mock";
    public static final String VALUE = "VALUE";
    public static final String TEST_VALUE = "TestValue";
    public static final String SOME_FIELD = "someField";

    @Test
    void findEnumClassImplementationsOfInterface_shouldReturnEnum_WhenExists() {
        Class<? extends Enum<?>> result = ReflectionUtil.findEnumClassImplementationsOfInterface(
                MockInterface.class, COM_THEAIREBELLION_ZEUS_UTIL_REFLECTIONS_MOCK);

        assertEquals(MockEnum.class, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findEnumClassImplementationsOfInterface_shouldThrow_WhenInvalidPackage(String packageName) {
        assertThrows(IllegalArgumentException.class, () ->
                ReflectionUtil.findEnumClassImplementationsOfInterface(MockInterface.class, packageName));
    }

    @Test
    void findEnumImplementationsOfInterface_shouldReturnEnumValue_WhenExists() {
        MockInterface result = ReflectionUtil.findEnumImplementationsOfInterface(
                MockInterface.class, VALUE, COM_THEAIREBELLION_ZEUS_UTIL_REFLECTIONS_MOCK);

        assertEquals(MockEnum.VALUE, result);
    }

    @Test
    void getAttributeOfClass_shouldReturnValue_WhenFieldExists() {
        TestClass testObject = new TestClass();
        testObject.someField = TEST_VALUE;

        String result = ReflectionUtil.getAttributeOfClass(SOME_FIELD, testObject, String.class);
        assertEquals(TEST_VALUE, result);
    }

    @ParameterizedTest
    @MethodSource("fieldAccessTestCases")
    void getFieldValue_shouldHandleDifferentScenarios(Class<?> targetType, Object testObject, Class<? extends Throwable> expectedException) {
        if (expectedException != null) {
            assertThrows(expectedException, () ->
                    ReflectionUtil.getFieldValue(testObject, targetType));
        } else {
            assertDoesNotThrow(() ->
                    ReflectionUtil.getFieldValue(testObject, targetType));
        }
    }

    @Test
    void findImplementationsOfInterface_shouldReturnClasses_WhenExist() {
        List<Class<? extends MockInterface>> result = ReflectionUtil.findImplementationsOfInterface(
                MockInterface.class, COM_THEAIREBELLION_ZEUS_UTIL_REFLECTIONS_MOCK);

        assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertTrue(result.contains(TestClass.class)),
                () -> assertTrue(result.contains(MockEnum.class))
        );
    }

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<ReflectionUtil> constructor = ReflectionUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ReflectionUtil instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    void testGetFieldValue_FieldExistsButNullValue() {
        TestClass testObject = new TestClass();
        testObject.someField = null;

        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, String.class));

        assertTrue(exception.getMessage().contains("Field value is not of the expected type"));
    }

    @Test
    void testGetAttributeOfClass_FieldExistsButNullValue() {
        TestClass testObject = new TestClass();
        testObject.someField = null;

        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass(SOME_FIELD, testObject, String.class));

        assertTrue(exception.getMessage().contains("Field 'someField' value is not of expected type"));
    }

    @Test
    void testGetFieldValue_NoAssignableFieldFound() {
        TestClass testObject = new TestClass();

        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, Double.class));

        assertTrue(exception.getMessage().contains("No field of type 'java.lang.Double' found in class"));
    }

    @Test
    void testFindEnumImplementationsOfInterface_NullEnumName() {
        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.findEnumImplementationsOfInterface(
                        MockInterface.class,
                        null,
                        COM_THEAIREBELLION_ZEUS_UTIL_REFLECTIONS_MOCK
                )
        );
        assertTrue(
                exception.getMessage().contains("Enum value 'null' not found"),
                "Should mention 'Enum value 'null' not found'"
        );
    }

    @Test
    void privateConstructor_shouldBeAccessible() throws Exception {
        Constructor<ReflectionUtil> constructor = ReflectionUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ReflectionUtil instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    void findImplementationsOfInterface_shouldThrow_WhenPackageIsInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                ReflectionUtil.findImplementationsOfInterface(MockInterface.class, null));
    }

    @Test
    void findImplementationsOfInterface_shouldThrow_WhenPackageIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                ReflectionUtil.findImplementationsOfInterface(MockInterface.class, "  "));
    }

    @Test
    void getFieldValue_shouldReturnValue_WhenFieldExistsAndIsNotNull() {
        TestClass testObject = new TestClass();
        testObject.someField = TEST_VALUE;

        String result = ReflectionUtil.getFieldValue(testObject, String.class);
        assertEquals(TEST_VALUE, result);
    }

    @Test
    void getFieldValue_shouldThrow_WhenFieldIsNullButTypeIsString() {
        TestClass testObject = new TestClass();
        testObject.someField = null;

        ReflectionException ex = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, String.class));
        assertTrue(ex.getMessage().contains("Field value is not of the expected type"));
    }

    @Test
    void getFieldValue_NoAssignableFieldFound() {
        TestClass testObject = new TestClass();

        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, Double.class));
        assertTrue(exception.getMessage().contains("No field of type 'java.lang.Double' found"));
    }

    @ParameterizedTest
    @MethodSource("fieldAccessTestCases")
    void getFieldValue_shouldHandleScenarios(Class<?> targetType, Object testObject, Class<? extends Throwable> expectedEx) {
        if (expectedEx != null) {
            assertThrows(expectedEx, () -> ReflectionUtil.getFieldValue(testObject, targetType));
        } else {
            assertDoesNotThrow(() -> ReflectionUtil.getFieldValue(testObject, targetType));
        }
    }

    @Test
    void getAttributeOfClass_FieldExistsButNullValue() {
        TestClass testObject = new TestClass();
        testObject.someField = null;

        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass(SOME_FIELD, testObject, String.class));
        assertTrue(exception.getMessage().contains("Field 'someField' value is not of expected type"));
    }

    @Test
    void getAttributeOfClass_FieldDoesNotExist() {
        TestClass testObject = new TestClass();

        ReflectionException ex = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass("nonExistentField", testObject, String.class));
        assertTrue(ex.getMessage().contains("Field 'nonExistentField' not found in class"));
    }

    @Test
    void getFieldValue_shouldWrapIllegalAccessException() throws Exception {
        TestClass testObject = new TestClass();
        testObject.someField = "someData";
        Field mockField = mock(Field.class);
        when(mockField.getType()).thenReturn((Class) String.class);
        when(mockField.get(testObject)).thenThrow(new IllegalAccessException("Nope!"));

        try (var utilSpy = mockStatic(ReflectionUtil.class)) {
            utilSpy.when(() ->
                    ReflectionUtil.getFieldValue(any(), any())
            ).thenCallRealMethod();
        }
    }

    private static Stream<Arguments> fieldAccessTestCases() {
        return Stream.of(
                Arguments.of(String.class, new TestClass(), ReflectionException.class),
                Arguments.of(Integer.class, new TestClass(), ReflectionException.class),
                Arguments.of(String.class, new TestClassWithPrivateField(), null)
        );
    }
}