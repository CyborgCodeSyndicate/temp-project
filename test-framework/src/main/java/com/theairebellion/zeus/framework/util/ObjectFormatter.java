package com.theairebellion.zeus.framework.util;


import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ObjectFormatter {

    private static final ConcurrentHashMap<Class<?>, Field[]> FIELDS_CACHE = new ConcurrentHashMap<>();

    protected String formatArgumentValue(Object argument, Set<Object> visited) {
        if (argument == null) return "null";
        if (argument instanceof String || argument instanceof Number || argument instanceof Boolean)
            return argument.toString();
        if (argument instanceof Collection<?>) return formatCollection((Collection<?>) argument, visited);
        if (argument.getClass().isArray()) return formatArray(argument, visited);
        return formatObjectFields(argument, visited);
    }


    private String formatCollection(Collection<?> collection, Set<Object> visited) {
        return collection.stream()
                .map(element -> formatArgumentValue(element, visited))
                .collect(Collectors.joining(", "));
    }

    private String formatArray(Object array, Set<Object> visited) {
        int length = Array.getLength(array);
        return IntStream.range(0, length)
                .mapToObj(i -> formatArgumentValue(Array.get(array, i), visited))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public String formatObjectFields(Object obj) {
        return formatObjectFields(obj, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    private String formatObjectFields(Object obj, Set<Object> visited) {
        if (obj == null) return "null";
        if (visited.contains(obj)) return "[Circular Reference Detected]";
        visited.add(obj);

        StringBuilder result = new StringBuilder();
        try {
            for (Field field : FIELDS_CACHE.computeIfAbsent(obj.getClass(), Class::getDeclaredFields)) {
                field.setAccessible(true);
                result.append(field.getName()).append(": ")
                        .append(formatArgumentValue(field.get(obj), visited))
                        .append("\n");
            }
        } catch (IllegalAccessException e) {
            result.append("[Error accessing fields]");
        }
        visited.remove(obj);
        return result.toString();
    }


    public String generateHtmlContent(Map<Enum<?>, LinkedList<Object>> arguments) {
        String htmlTemplate = ResourceLoader.loadHtmlTemplate("allure/html/epilogue-template.html");
        return htmlTemplate.replace("{{argumentRows}}", buildRowsFromMap("", arguments));
    }

    private String buildRowsFromMap(String label, Map<Enum<?>, LinkedList<Object>> map) {
        if ("PreArguments".equals(label)) return "";
        return map.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .map(entry -> String.format("<tr><td>%s</td><td>%s</td></tr>", entry.getKey(), formatObject(entry.getValue())))
                .collect(Collectors.joining());
    }

    private String formatObject(LinkedList<Object> objects) {
        return objects == null || objects.isEmpty() ? "" :
                objects.stream()
                        .filter(Objects::nonNull)
                        .filter(obj -> !obj.getClass().getName().contains("Lambda"))
                        .map(this::formatObjectFields)
                        .collect(Collectors.joining(", "));
    }

    protected String escapeHtml(String input) {
        return input == null ? "N/A" : input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("%", "%%");
    }

    protected String formatAnnotationsToNewRows(String annotations) {
        if (annotations == null || annotations.trim().isEmpty()) {
            return "None";
        }
        String[] annotationList = annotations.split("(?=@{1,2})");

        StringBuilder formattedAnnotations = new StringBuilder("<pre class='annotation'>");

        int indentLevel = 0;

        for (String annotation : annotationList) {
            if (annotation.trim().isEmpty()) {
                continue;
            }

            String cleanedAnnotation = annotation.trim()
                    .replaceAll("com\\.theairebellion\\.zeus\\.framework\\.annotation\\.", "")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\n\n", "\n")
                    .replaceAll("value\\s*=\\s*", "");

            cleanedAnnotation = cleanedAnnotation.replaceAll("late\\s*=\\s*false,?", "")
                    .replaceAll("order\\s*=\\s*\\d+,?", "")
                    .replaceAll("useJavaDoc\\s*=\\s*false,?", "");

            if (cleanedAnnotation.contains("@Description")) {
                cleanedAnnotation = cleanedAnnotation.replaceAll("@Description\\(([^)]+)\\)", "@Description(\"$1\")")
                        .replaceAll("\n", "");
            }

            if (cleanedAnnotation.contains("requestUrlSubStrings")) {
                cleanedAnnotation = cleanedAnnotation.replaceAll("requestUrlSubStrings\\s*=\\s*\\[([^]]+)]", "requestUrlSubStrings = [$1]");
            }

            cleanedAnnotation = cleanedAnnotation
                    .replaceAll("\\{", "{\n" + getIndent(indentLevel + 1))
                    .replaceAll("}", "\n" + getIndent(indentLevel) + "}")
                    .replaceAll("\\[", "[\n" + getIndent(indentLevel + 1))
                    .replaceAll("]", "\n" + getIndent(indentLevel) + "]")
                    .replaceAll(",", ",\n" + getIndent(indentLevel + 1))
                    .replaceAll("\n\\s*\n", "\n");

            formattedAnnotations.append(getIndent(indentLevel)).append(cleanedAnnotation).append("\n");

            indentLevel += countOccurrences(cleanedAnnotation, "{") + countOccurrences(cleanedAnnotation, "[");
            indentLevel -= countOccurrences(cleanedAnnotation, "}") + countOccurrences(cleanedAnnotation, "]");
        }

        formattedAnnotations.append("</pre>");
        return formattedAnnotations.toString();
    }

    private String getIndent(int level) {
        return "    ".repeat(level);
    }

    private int countOccurrences(String str, String target) {
        return str.length() - str.replace(target, "").length();
    }

    protected String formatLongText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "None";
        }

        text = text.replace("<", "&lt;")
                .replace(">", "&gt;");

        String[] words = text.split("\\s+");
        StringBuilder formattedText = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() > 80) {
                formattedText.append(currentLine.toString().trim()).append("<br>");
                currentLine = new StringBuilder();
            }
            currentLine.append(word).append(" ");
        }

        formattedText.append(currentLine.toString().trim());

        return formattedText.toString();
    }

    protected String formatAnnotationArguments(Annotation annotation) {
        return Arrays.stream(annotation.annotationType().getDeclaredMethods())
                .filter(method -> method.getParameterCount() == 0)
                .map(method -> {
                    try {
                        return method.getName() + "=" + method.invoke(annotation);
                    } catch (Exception e) {
                        return method.getName() + "=error";
                    }
                })
                .collect(Collectors.joining(", ", "(", ")"));
    }


    public String getClassAnnotations(ExtensionContext context) {
        String annotations = Arrays.stream(context.getRequiredTestClass().getAnnotations())
                .map(annotation -> "@" + annotation.annotationType().getSimpleName() +
                        (annotationHasArguments(annotation) ? formatAnnotationArguments(annotation) : ""))
                .collect(Collectors.joining("\n"));

        return annotations.isEmpty() ? "No class annotations" : annotations;
    }


    private boolean annotationHasArguments(Annotation annotation) {
        return Arrays.stream(annotation.annotationType().getDeclaredMethods())
                .anyMatch(method -> method.getParameterCount() > 0);
    }

    protected String getMethodAnnotations(ExtensionContext context) {
        return Arrays.stream(context.getRequiredTestMethod().getAnnotations())
                .map(this::formatAnnotation)
                .collect(Collectors.joining("\n"));
    }

    private String formatAnnotation(Annotation annotation) {
        String annotationName = annotation.annotationType().getSimpleName();
        String args = Arrays.stream(annotation.annotationType().getDeclaredMethods())
                .filter(method -> method.getParameterCount() == 0)
                .map(method -> getAnnotationArgument(annotation, method))
                .collect(Collectors.joining(", "));

        return "@" + annotationName + (args.isEmpty() ? "" : "(" + args + ")");
    }


    private String getAnnotationArgument(Annotation annotation, Method method) {
        try {
            Object value = method.invoke(annotation);
            return value.getClass().isArray() ? method.getName() + "=" + arrayToString(value) : method.getName() + "=" + value;
        } catch (Exception e) {
            return method.getName() + "=error";
        }
    }

    protected String getTestArguments(ExtensionContext context) {
        return context.getTestInstance()
                .flatMap(instance -> context.getTestMethod()
                        .map(method -> Arrays.stream(method.getParameters())
                                .map(param -> param.getType().getSimpleName())
                                .collect(Collectors.joining(", "))))
                .orElse("No arguments available.");
    }

    private String arrayToString(Object array) {
        if (array.getClass().isArray()) {
            int length = Array.getLength(array);
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(Array.get(array, i));
            }
            sb.append("]");
            return sb.toString();
        }
        return array.toString();
    }
}