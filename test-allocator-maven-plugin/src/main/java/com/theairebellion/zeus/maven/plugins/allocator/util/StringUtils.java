package com.theairebellion.zeus.maven.plugins.allocator.util;

import java.util.HashSet;
import java.util.Set;

public final class StringUtils {

    private StringUtils() {
    }


    public static Set<String> parseCommaSeparated(String value) {
        Set<String> result = new HashSet<>();
        if (value == null || value.trim().isEmpty()) {
            return result;
        }

        String[] parts = value.split(",");
        for (String part : parts) {
            result.add(part.trim());
        }

        return result;
    }

}

