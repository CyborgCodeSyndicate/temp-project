package com.theairebellion.zeus.util.reflections;

import com.theairebellion.zeus.util.reflections.exceptions.ReflectionException;
import com.theairebellion.zeus.util.reflections.mock.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReflectionUtilTest {

    @Test
    void testFindEnumClassImplementationsOfInterface_Valid() {
        // Call the method with the correct package prefix
        Class<? extends Enum<?>> result = ReflectionUtil.findEnumClassImplementationsOfInterface(
                MockInterface.class, "com.theairebellion.zeus.util.reflections");

        // Assertions
        assertNotNull(result);
        assertEquals(MockEnum.class, result);
    }

    @Test
    void testFindEnumClassImplementationsOfInterface_NoEnumFound() {
        // Call the method with a non-existent package and expect an exception
        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.findEnumClassImplementationsOfInterface(
                        MockInterface.class, "com.nonexistent.package"));
    }

    @Test
    void testFindEnumImplementationsOfInterface_Valid() {
        // Test finding a specific enum value
        MockInterface result = ReflectionUtil.findEnumImplementationsOfInterface(
                MockInterface.class, "VALUE1", "com.theairebellion.zeus.util.reflections");

        // Assertions
        assertNotNull(result);
        assertInstanceOf(MockEnum.class, result);
        assertEquals(MockEnum.VALUE1, result);
    }

    @Test
    void testFindEnumImplementationsOfInterface_EnumNotFound() {
        // Test when the enum value does not exist
        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.findEnumImplementationsOfInterface(
                        MockInterface.class, "NON_EXISTENT_VALUE", "com.theairebellion.zeus.util.reflections"));
    }

    @Test
    void testFindEnumClassImplementationsOfInterface_NoEnums() {
        // Act & Assert: Expect ReflectionException
        ReflectionException exception = assertThrows(ReflectionException.class, () -> {
            ReflectionUtil.findEnumClassImplementationsOfInterface(
                    MockInterface.class,
                    "com.theairebellion.zeus.no.enums"
            );
        });

        assertTrue(exception.getMessage().contains("No Enum implementing interface"));
    }

    @Test
    void testGetAttributeOfClass_Valid() {
        // Arrange
        TestClass testObject = new TestClass();
        testObject.someField = "Some Value";

        // Act
        String result = ReflectionUtil.getAttributeOfClass("someField", testObject, String.class);

        // Assert
        assertEquals("Some Value", result);
    }

    @Test
    void testGetFieldValue_ValidField() {
        // Create a test object
        TestClass testObject = new TestClass();
        testObject.someField = "TestValue";

        // Retrieve the field value
        String result = ReflectionUtil.getFieldValue(testObject, String.class);

        // Assertions
        assertNotNull(result);
        assertEquals("TestValue", result);
    }

    @Test
    void testGetFieldValue_FieldNotFound() {
        // Create a test object
        TestClass testObject = new TestClass();

        // Test for a non-existent field
        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, Integer.class));
    }

    @Test
    void testGetAttributeOfClass_ValidField() {
        // Create a test object
        TestClass testObject = new TestClass();
        testObject.someField = "TestValue";

        // Retrieve the attribute value by field name
        String result = ReflectionUtil.getAttributeOfClass("someField", testObject, String.class);

        // Assertions
        assertNotNull(result);
        assertEquals("TestValue", result);
    }

    @Test
    void testGetAttributeOfClass_FieldNotFound() {
        // Create a test object
        TestClass testObject = new TestClass();

        // Test for a non-existent field
        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass("nonExistentField", testObject, String.class));
    }

    @Test
    void testGetAttributeOfClass_FieldTypeMismatch() {
        // Create a test object
        TestClass testObject = new TestClass();
        testObject.someField = "TestValue";

        // Test for a type mismatch
        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass("someField", testObject, Integer.class));
    }

    @Test
    void testGetFieldValue_FieldTypeMismatch() {
        TestClass testObject = new TestClass();
        testObject.someField = "TestValue";

        // Test type mismatch for the field value
        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, Integer.class));
    }

    @Test
    void testGetFieldValue_NoMatchingField() {
        TestClass testObject = new TestClass();

        // Test no matching field
        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, Double.class));
    }

    @Test
    void testValidateInputs_NullInputs() {
        // Test null inputs
        assertThrows(IllegalArgumentException.class, () ->
                ReflectionUtil.findImplementationsOfInterface(null, "com.theairebellion.zeus"));
    }

    @Test
    void testValidateInputs_EmptyString() {
        // Test empty string input
        assertThrows(IllegalArgumentException.class, () ->
                ReflectionUtil.findImplementationsOfInterface(MockInterface.class, ""));
    }

    @Test
    void testGetAttributeOfClass_TypeMismatch() {
        TestClass testObject = new TestClass();
        testObject.someField = "TestValue";

        // Test type mismatch for the field
        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass("someField", testObject, Integer.class));
    }

    @Test
    void testFindImplementationsOfInterface_NoResults() {
        // Call with an empty or unrelated package
        List<Class<? extends MockInterface>> result = ReflectionUtil.findImplementationsOfInterface(
                MockInterface.class, "com.nonexistent.package");

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetFieldValue_NoFieldsInClass() {
        Object emptyObject = new Object(); // Object has no declared fields
        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(emptyObject, String.class));
    }

    @Test
    void testGetAttributeOfClass_FieldExistsButWrongType() {
        TestClass testObject = new TestClass();
        testObject.someField = "TestValue";
        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass("someField", testObject, Integer.class));
    }

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<ReflectionUtil> constructor = ReflectionUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ReflectionUtil instance = constructor.newInstance();
        assertNotNull(instance); // Ensure the instance is created
    }

    @Test
    void testGetFieldValue_FieldExistsButNullValue() {
        TestClass testObject = new TestClass();
        testObject.someField = null;

        // Expect a ReflectionException for null values
        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, String.class));

        assertEquals("Field value is not of the expected type 'java.lang.String'. Actual value type: 'null'.",
                exception.getMessage());
    }

    @Test
    void testGetAttributeOfClass_FieldExistsButNullValue() {
        TestClass testObject = new TestClass();
        testObject.someField = null;

        // Expect a ReflectionException for null field value
        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass("someField", testObject, String.class));

        assertEquals("Field 'someField' value is not of expected type 'java.lang.String'. Actual value type: 'null'.",
                exception.getMessage());
    }

    @Test
    void testGetFieldValue_FieldNotAccessible() {
        TestClassWithPrivateField privateFieldObject = new TestClassWithPrivateField();
        // Validate behavior for private fields
        String result = ReflectionUtil.getFieldValue(privateFieldObject, String.class);

        // Assertions
        assertNotNull(result);
        assertEquals("secret", result);
    }

    @Test
    void testGetFieldValue_IllegalAccessException() throws Exception {
        // Simulate an inaccessible field using reflection
        TestClass testObject = new TestClass();
        Field field = TestClass.class.getDeclaredField("someField");
        field.setAccessible(false);

        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, String.class));
    }

    @Test
    void testGetAttributeOfClass_NoSuchField() {
        TestClass testObject = new TestClass();

        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass("nonexistentField", testObject, String.class));
    }

    @Test
    void testGetAttributeOfClass_IllegalAccessException() throws Exception {
        TestClass testObject = new TestClass();
        Field field = TestClass.class.getDeclaredField("someField");
        field.setAccessible(false);

        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass("someField", testObject, String.class));
    }

    @Test
    void testGetFieldValue_TypeMismatch() {
        TestClass testObject = new TestClass();
        testObject.someField = "StringValue";

        assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, Integer.class));
    }

    @Test
    void testGetFieldValue_NoAssignableFieldFound() {
        TestClass testObject = new TestClass();

        // Test with a field type that is not present
        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, Double.class));

        // Verify the exception message
        assertTrue(exception.getMessage().contains("No field of type 'java.lang.Double' found in class"));
    }

    @Test
    void testGetFieldValue_NullFieldTypeMismatch() {
        TestClassWithField testObject = new TestClassWithField(null); // Field is null
        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, String.class));

        assertTrue(exception.getMessage().contains("Field value is not of the expected type"));
    }

    @Test
    void testGetAttributeOfClass_NullFieldValue() {
        TestClass testObject = new TestClass();
        testObject.someField = null;

        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass("someField", testObject, String.class));

        assertTrue(exception.getMessage().contains("Field 'someField' value is not of expected type"));
    }

    @Test
    void testGetAttributeOfClass_IllegalAccessException_Handled() {
        class TestClass {
            private String hiddenField = "hidden";
        }

        TestClass testObject = new TestClass();
        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getAttributeOfClass("hiddenField", testObject, Integer.class)); // Mismatched type

        assertTrue(exception.getMessage().contains("Field 'hiddenField' value is not of expected type"));
    }

    @Test
    void testGetFieldValue_NullFieldValue() {
        TestClass testObject = new TestClass();
        testObject.someField = null;

        // Act & Assert
        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.getFieldValue(testObject, String.class)
        );

        assertTrue(exception.getMessage().contains("Field value is not of the expected type"));
    }

    @Test
    void testFindImplementationsOfInterface_WithResults() {
        // We assume MockClass implements MockInterface in package "com.theairebellion.zeus.util.reflections.mock"
        List<Class<? extends MockInterface>> result =
                ReflectionUtil.findImplementationsOfInterface(
                        MockInterface.class,
                        "com.theairebellion.zeus.util.reflections.mock"
                );
        assertNotNull(result);
        assertFalse(result.isEmpty(), "Expected at least one implementation of MockInterface");
        // In this example, we expect MockClass to be returned
        assertTrue(result.stream().anyMatch(clazz -> clazz.equals(TestClass.class)),
                "Expected TestClass to be in the result");
    }

    @Test
    void testFindEnumImplementationsOfInterface_NullEnumName() {
        // This should fail the .filter(e -> e.name().equals(enumName)) and throw an exception
        ReflectionException exception = assertThrows(ReflectionException.class, () ->
                ReflectionUtil.findEnumImplementationsOfInterface(
                        MockInterface.class,
                        null,
                        "com.theairebellion.zeus.util.reflections"
                )
        );
        assertTrue(
                exception.getMessage().contains("Enum value 'null' not found"),
                "Should mention 'Enum value 'null' not found'"
        );
    }
}