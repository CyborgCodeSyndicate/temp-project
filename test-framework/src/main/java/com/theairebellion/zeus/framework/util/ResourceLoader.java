package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.framework.extension.Craftsman;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for loading resource files such as HTML templates.
 * <p>
 * This class provides a method to load an HTML template from the classpath
 * and return its content as a string.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class ResourceLoader {

    /**
     * Loads an HTML template from the classpath and returns its content as a string.
     * <p>
     * The template file should be located within the project's resources directory.
     * If the file is not found or cannot be read, a {@link RuntimeException} is thrown.
     * </p>
     *
     * @param filePath the name or path of the template file within the classpath
     * @return the content of the HTML template as a string
     * @throws RuntimeException if the template file is not found or cannot be read
     */
    public static String loadResourceFile(String filePath) {
        try (InputStream inputStream = Craftsman.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("File not found in resources: " + filePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file from resources: " + filePath, e);
        }
    }
}
