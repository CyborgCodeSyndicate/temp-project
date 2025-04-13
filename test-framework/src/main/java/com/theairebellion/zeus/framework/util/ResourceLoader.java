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
        try (InputStream inputStream = getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("File not found in resources: " + filePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file from resources: " + filePath, e);
        }
    }

    /**
     * Retrieves a resource file as an {@link InputStream} from the classpath.
     * <p>
     * This method uses the class loader of the {@link com.theairebellion.zeus.framework.extension.Craftsman}
     * class to locate and open the specified file from the project's resources directory.
     * It returns {@code null} if the file is not found.
     * </p>
     *
     * @param filePath the name or path of the resource file within the classpath
     * @return an {@link InputStream} of the resource file, or {@code null} if the file does not exist
     */
    static InputStream getResourceAsStream(String filePath) {
        return Craftsman.class.getClassLoader().getResourceAsStream(filePath);
    }
}
