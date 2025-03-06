package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.log.LogTest;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ObjectFormatter {

    private static final ConcurrentHashMap<Class<?>, Field[]> FIELDS_CACHE = new ConcurrentHashMap<>();
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();


    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }


    public String formatObjectFields(Object obj) {
        return formatObjectFields(obj, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    protected String formatObjectFields(Object obj, Set<Object> visited) {
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

    protected String formatArgumentValue(Object argument, Set<Object> visited) {
        if (argument == null) return "null";
        if (argument instanceof String || argument instanceof Number || argument instanceof Boolean)
            return argument.toString();
        if (argument instanceof Collection<?>) return formatCollection((Collection<?>) argument, visited);
        if (argument.getClass().isArray()) return formatArray(argument, visited);
        return formatObjectFields(argument, visited);
    }

    protected String formatCollection(Collection<?> collection, Set<Object> visited) {
        return collection.stream()
                .map(element -> formatArgumentValue(element, visited))
                .collect(Collectors.joining(", "));
    }

    protected String formatArray(Object array, Set<Object> visited) {
        int length = Array.getLength(array);
        return IntStream.range(0, length)
                .mapToObj(i -> formatArgumentValue(Array.get(array, i), visited))
                .collect(Collectors.joining(", ", "[", "]"));
    }



    public String generateHtmlContent(Map<Enum<?>, LinkedList<Object>> arguments) {
        String htmlTemplate = ResourceLoader.loadHtmlTemplate("allure/html/epilogue-template.html");
        return htmlTemplate.replace("{{argumentRows}}", buildRowsFromMap("", arguments));
    }

    protected String buildRowsFromMap(String label, Map<Enum<?>, LinkedList<Object>> map) {
        if ("PreArguments".equals(label)) return "";
        return map.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .map(entry -> String.format("<tr><td>%s</td><td>%s</td></tr>",
                        entry.getKey(), formatObject(entry.getValue())))
                .collect(Collectors.joining());
    }

    protected String formatObject(LinkedList<Object> objects) {
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



    public String getClassAnnotations(ExtensionContext context) {
        String annotations = Arrays.stream(context.getRequiredTestClass().getAnnotations())
                .map(annotation -> "@" + annotation.annotationType().getSimpleName() +
                        (annotationHasArguments(annotation) ? formatAnnotationArguments(annotation) : ""))
                .collect(Collectors.joining("\n"));

        return annotations.isEmpty() ? "No class annotations" : annotations;
    }

    protected String getMethodAnnotations(ExtensionContext context) {
        return Arrays.stream(context.getRequiredTestMethod().getAnnotations())
                .map(this::formatAnnotation)
                .collect(Collectors.joining("\n"));
    }

    protected String formatAnnotation(Annotation annotation) {
        String annotationName = annotation.annotationType().getSimpleName();
        String args = Arrays.stream(annotation.annotationType().getDeclaredMethods())
                .filter(method -> method.getParameterCount() == 0)
                .map(method -> getAnnotationArgument(annotation, method))
                .collect(Collectors.joining(", "));

        return "@" + annotationName + (args.isEmpty() ? "" : "(" + args + ")");
    }

    protected String getAnnotationArgument(Annotation annotation, Method method) {
        try {
            Object value = method.invoke(annotation);
            return value.getClass().isArray() ? method.getName() + "=" + arrayToString(value) : method.getName() + "=" + value;
        } catch (Exception e) {
            return method.getName() + "=error";
        }
    }

    protected String arrayToString(Object array) {
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

    protected boolean annotationHasArguments(Annotation annotation) {
        return Arrays.stream(annotation.annotationType().getDeclaredMethods())
                .anyMatch(method -> method.getParameterCount() > 0);
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

            String cleanedAnnotation = cleanAnnotation(annotation);
            String indentedAnnotation = applyIndentation(cleanedAnnotation, indentLevel);

            formattedAnnotations.append(getIndent(indentLevel)).append(indentedAnnotation).append("\n");

            indentLevel += countOccurrences(indentedAnnotation, "{") + countOccurrences(indentedAnnotation, "[");
            indentLevel -= countOccurrences(indentedAnnotation, "}") + countOccurrences(indentedAnnotation, "]");
        }

        formattedAnnotations.append("</pre>");
        return formattedAnnotations.toString();
    }

    protected String cleanAnnotation(String annotation) {
        return annotation.trim()
                .replaceAll("com\\.theairebellion\\.zeus\\.framework\\.annotation\\.", "")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n\n", "\n")
                .replaceAll("value\\s*=\\s*", "")
                .replaceAll("late\\s*=\\s*false,?", "")
                .replaceAll("order\\s*=\\s*\\d+,?", "")
                .replaceAll("useJavaDoc\\s*=\\s*false,?", "")
                .replaceAll("@Description\\(([^)]+)\\)", "@Description(\"$1\")")
                .replaceAll("\n", "")
                .replaceAll("requestUrlSubStrings\\s*=\\s*\\[([^]]+)]", "requestUrlSubStrings = [$1]");
    }

    protected String applyIndentation(String text, int baseIndentLevel) {
        return text
                .replaceAll("\\{", "{\n" + getIndent(baseIndentLevel + 1))
                .replaceAll("}", "\n" + getIndent(baseIndentLevel) + "}")
                .replaceAll("\\[", "[\n" + getIndent(baseIndentLevel + 1))
                .replaceAll("]", "\n" + getIndent(baseIndentLevel) + "]")
                .replaceAll(",", ",\n" + getIndent(baseIndentLevel + 1))
                .replaceAll("\n\\s*\n", "\n");
    }

    protected String getIndent(int level) {
        return "    ".repeat(level);
    }

    protected int countOccurrences(String str, String target) {
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

    protected String getTestArguments(ExtensionContext context) {
        return context.getTestInstance()
                .flatMap(instance -> context.getTestMethod()
                        .map(method -> Arrays.stream(method.getParameters())
                                .map(param -> param.getType().getSimpleName())
                                .collect(Collectors.joining(", "))))
                .orElse("No arguments available.");
    }


    public String formatResponses(List<Object> storedResponses) {
        try {
            List<Map<String, Object>> formattedResponses = new ArrayList<>();

            for (Object responseList : storedResponses) {
                if (responseList instanceof List) {
                    List<?> apiResponseList = (List<?>) responseList;
                    for (Object apiResponse : apiResponseList) {
                        Map<String, Object> responseMap = formatSingleResponse(apiResponse);
                        if (!responseMap.isEmpty()) {
                            formattedResponses.add(responseMap);
                        }
                    }
                }
            }

            return OBJECT_MAPPER.writeValueAsString(formattedResponses);
        } catch (Exception e) {
            LogTest.error("Error formatting API responses", e);
            return "Error formatting API responses: " + e.getMessage();
        }
    }

    protected Map<String, Object> formatSingleResponse(Object apiResponse) {
        Map<String, Object> responseMap = new LinkedHashMap<>();

        Object url = invokeGetter(apiResponse, "url", apiResponse.getClass());
        if (url != null) {
            responseMap.put("URL", url.toString());
        }

        Object status = invokeGetter(apiResponse, "status", apiResponse.getClass());
        if (status != null) {
            responseMap.put("Status", status);
        }

        Object body = invokeGetter(apiResponse, "body", apiResponse.getClass());
        if (body != null) {
            String bodyStr = body.toString();

            if (bodyStr.length() > 1000) {
                String bodyType = detectBodyType(bodyStr);

                responseMap.put("Body", Map.of(
                        "type", bodyType,
                        "length", bodyStr.length(),
                        "preview", bodyStr.substring(0, 200)
                ));
            } else {
                responseMap.put("Body", bodyStr);
            }
        }

        return responseMap;
    }

    protected String detectBodyType(String body) {
        if (body.startsWith("{") && body.endsWith("}")) return "JSON";
        if (body.startsWith("<!doctype html>") || body.startsWith("<html")) return "HTML";
        if (body.startsWith("function")) return "JavaScript";
        if (body.startsWith("data:")) return "Base64/Data URI";
        return "Text";
    }

    protected Object invokeGetter(Object obj, String fieldName, Class<?> clazz) {
        try {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if ((method.getName().equals("get" + capitalize(fieldName)) ||
                        method.getName().equals("is" + capitalize(fieldName)) ||
                        method.getName().equals(fieldName)) &&
                        method.getParameterCount() == 0) {
                    return method.invoke(obj);
                }
            }
        } catch (Exception e) {
            LogTest.error("Could not invoke getter for " + fieldName + ": " + e.getMessage());
        }
        return null;
    }

    protected String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    public String formatProcessedData(JourneyData[] originalData, Object[] processedData) {
        if (processedData == null || processedData.length == 0) {
            return "No data available";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Processed Journey Data:\n");
        sb.append("-------------------\n");

        for (int i = 0; i < processedData.length; i++) {
            Object data = processedData[i];
            String journeyDataValue = extractJourneyDataValue(originalData[i]);

            sb.append(String.format("Journey: %s\n", journeyDataValue));

            if (data == null) {
                sb.append("  Data: null\n");
            } else {
                formatJsonData(sb, data);
            }

            if (i < processedData.length - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    protected String extractJourneyDataValue(JourneyData journeyData) {
        return journeyData.value();
    }

    protected void formatJsonData(StringBuilder sb, Object data) {
        try {
            String formattedJson = OBJECT_MAPPER.writeValueAsString(data);
            String[] jsonLines = formattedJson.split("\n");
            for (String line : jsonLines) {
                sb.append("  ").append(line).append("\n");
            }
        } catch (Exception e) {
            sb.append("  Data: ").append(data.toString()).append("\n");
        }
    }
}