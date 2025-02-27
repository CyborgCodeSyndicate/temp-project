package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.framework.extension.Craftsman;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceLoader {

    public static String loadHtmlTemplate(String templateName) {
        try (InputStream inputStream = Craftsman.class.getClassLoader().getResourceAsStream(templateName)) {
            if (inputStream == null) {
                throw new RuntimeException("Template file not found: " + templateName);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load HTML template: " + templateName, e);
        }
    }


}