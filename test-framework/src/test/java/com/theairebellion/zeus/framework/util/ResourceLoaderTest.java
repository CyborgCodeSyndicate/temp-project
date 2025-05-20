package com.theairebellion.zeus.framework.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResourceLoaderTest {

    @Test
    @DisplayName("Should return file content when resource exists")
    void shouldReturnContentWhenResourceExists() {
        // Given
        String expectedContent = "<html><body>Hello</body></html>";
        InputStream mockStream = new ByteArrayInputStream(expectedContent.getBytes());

        try (MockedStatic<ResourceLoader> loaderMock = mockStatic(ResourceLoader.class, CALLS_REAL_METHODS)) {
            loaderMock.when(() -> ResourceLoader.getResourceAsStream("template.html")).thenReturn(mockStream);

            // When
            String result = ResourceLoader.loadResourceFile("template.html");

            // Then
            assertEquals(expectedContent, result);
        }
    }

    @Test
    @DisplayName("Should throw exception if file not found in resources")
    void shouldThrowWhenFileNotFound() {
        // Given
        try (MockedStatic<ResourceLoader> loaderMock = mockStatic(ResourceLoader.class, CALLS_REAL_METHODS)) {
            loaderMock.when(() -> ResourceLoader.getResourceAsStream("missing.html")).thenReturn(null);

            // When
            // Then
            RuntimeException ex = assertThrows(RuntimeException.class, () ->
                    ResourceLoader.loadResourceFile("missing.html")
            );

            assertTrue(ex.getMessage().contains("File not found"));
        }
    }

    @Test
    @DisplayName("Should throw exception if IO error occurs while reading")
    void shouldThrowWhenIOExceptionOccurs() {
        // Given
        InputStream faultyStream = mock(InputStream.class);
        try {
            when(faultyStream.readAllBytes()).thenThrow(new IOException("Simulated IO failure"));
        } catch (IOException ignored) {}

        try (MockedStatic<ResourceLoader> loaderMock = mockStatic(ResourceLoader.class, CALLS_REAL_METHODS)) {
            loaderMock.when(() -> ResourceLoader.getResourceAsStream("broken.html")).thenReturn(faultyStream);

            // When
            // Then
            RuntimeException ex = assertThrows(RuntimeException.class, () ->
                    ResourceLoader.loadResourceFile("broken.html")
            );

            assertTrue(ex.getMessage().contains("Failed to load file"));
        }
    }
}
