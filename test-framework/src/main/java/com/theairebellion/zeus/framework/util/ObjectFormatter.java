package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.framework.annotation.JourneyData;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.SerializationFeature;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Utility class for formatting objects and their fields into various string representations.
 *
 * <p>Provides methods to format object fields, collections, arrays, generate HTML content from data,
 * format annotations, process responses, and format journey data.
 * </p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public final class ObjectFormatter {

   /**
    * Cache for storing declared fields of classes.
    */
   private static final ConcurrentHashMap<Class<?>, Field[]> FIELDS_CACHE = new ConcurrentHashMap<>();

   /**
    * ObjectMapper instance configured for JSON formatting.
    */
   private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();


   private ObjectFormatter() {
   }


   /**
    * Creates and configures an ObjectMapper with specific serialization settings.
    *
    * @return a configured ObjectMapper instance
    */
   private static ObjectMapper createObjectMapper() {
      ObjectMapper mapper = new ObjectMapper();
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      return mapper;
   }


   /**
    * Formats the fields of the provided object into a string representation.
    * Uses a set to detect circular references.
    *
    * @param obj the object whose fields will be formatted
    * @return a formatted string representing the object's fields
    */
   public static String formatObjectFields(Object obj) {
      return formatObjectFields(obj, Collections.newSetFromMap(new IdentityHashMap<>()));
   }


   /**
    * Recursively formats the fields of the provided object, tracking visited objects to avoid circular references.
    *
    * @param obj     the object to format
    * @param visited a set of already visited objects to prevent infinite recursion
    * @return a formatted string representation of the object's fields
    */
   private static String formatObjectFields(Object obj, Set<Object> visited) {
      if (obj == null) {
         return "null";
      }
      if (visited.contains(obj)) {
         return "[Circular Reference Detected]";
      }
      visited.add(obj);

      Class<?> objClass = obj.getClass();
      if (objClass.getPackageName().startsWith("java.") || objClass.isEnum()) {
         visited.remove(obj);
         return obj.toString();
      }

      StringBuilder result = new StringBuilder(objClass.getSimpleName()).append(" {\n");
      try {
         for (Field field : FIELDS_CACHE.computeIfAbsent(objClass, Class::getDeclaredFields)) {
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            Object target = isStatic ? null : obj;
            if (!field.canAccess(target)) {
               try {
                  field.setAccessible(true);
               } catch (InaccessibleObjectException e) {
                  result.append(field.getName()).append(": [Inaccessible Field]\n");
                  continue;
               }
            }
            result.append(field.getName()).append(": ")
                  .append(formatArgumentValue(field.get(obj), visited))
                  .append("\n");
         }
      } catch (IllegalAccessException e) {
         result.append("[Error accessing fields]");
      }
      result.append("}");
      visited.remove(obj);
      return result.toString();
   }


   /**
    * Formats a single argument value, handling primitive types, collections, arrays, or objects.
    *
    * @param argument the argument value to format
    * @param visited  a set of visited objects for circular reference detection
    * @return a string representation of the argument value
    */
   private static String formatArgumentValue(Object argument, Set<Object> visited) {
      if (argument == null) {
         return "null";
      }
      if (argument instanceof String || argument instanceof Number || argument instanceof Boolean) {
         return argument.toString();
      }
      if (argument instanceof Collection<?>) {
         return formatCollection((Collection<?>) argument, visited);
      }
      if (argument.getClass().isArray()) {
         return formatArray(argument, visited);
      }
      return formatObjectFields(argument, visited);
   }


   /**
    * Formats a collection of objects into a comma-separated string representation.
    *
    * @param collection the collection to format
    * @param visited    a set of visited objects for circular reference detection
    * @return a string representation of the collection elements
    */
   private static String formatCollection(Collection<?> collection, Set<Object> visited) {
      return collection.stream()
            .map(element -> formatArgumentValue(element, visited))
            .collect(Collectors.joining(", "));
   }


   /**
    * Formats an array of objects into a comma-separated string representation.
    *
    * @param array   the array to format
    * @param visited a set of visited objects for circular reference detection
    * @return a formatted string representation of the array elements
    */
   private static String formatArray(Object array, Set<Object> visited) {
      int length = Array.getLength(array);
      return IntStream.range(0, length)
            .mapToObj(i -> formatArgumentValue(Array.get(array, i), visited))
            .collect(Collectors.joining(", ", "[", "]"));
   }


   /**
    * Generates HTML content by loading an HTML template and replacing placeholders with formatted argument rows.
    *
    * @param arguments a map of Enum keys to LinkedList of objects representing arguments
    * @return a string containing the generated HTML content
    */
   public static String generateHtmlContent(Map<Enum<?>, List<Object>> arguments) {
      String htmlTemplate = ResourceLoader.loadResourceFile("allure/html/test-data.html");
      return htmlTemplate.replace("{{argumentRows}}", buildRowsFromMap("", arguments));
   }


   /**
    * Builds HTML table rows from a map of arguments.
    *
    * @param label a label used to filter certain keys (e.g. "PreArguments")
    * @param map   the map containing argument data
    * @return a string containing HTML table rows
    */
   private static String buildRowsFromMap(String label, Map<Enum<?>, List<Object>> map) {
      if ("PreArguments".equals(label)) {
         return "";
      }
      return map.entrySet().stream()
            .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
            .map(entry -> String.format("<tr><td>%s</td><td>%s</td></tr>",
                  entry.getKey(), formatObject(entry.getValue())))
            .collect(Collectors.joining());
   }


   /**
    * Formats a linked list of objects by concatenating their formatted field representations.
    *
    * @param objects the linked list of objects to format
    * @return a string representation of the formatted objects
    */
   private static String formatObject(List<Object> objects) {
      return objects == null || objects.isEmpty() ? "" :
            objects.stream()
                  .filter(Objects::nonNull)
                  .filter(obj -> !obj.getClass().getName().contains("Lambda"))
                  .map(ObjectFormatter::formatObjectFields)
                  .collect(Collectors.joining(", "));
   }


   /**
    * Escapes HTML special characters in the provided input string.
    *
    * @param input the string to escape
    * @return the escaped string suitable for HTML display
    */
   public static String escapeHtml(String input) {
      return input == null ? "N/A" : input.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
            .replace("%", "%%");
   }


   /**
    * Retrieves and formats annotations present on the test class associated with the provided extension context.
    *
    * @param context the JUnit extension context containing test class information
    * @return a string containing formatted class annotations or a message if none are found
    */
   public static String getClassAnnotations(ExtensionContext context) {
      String annotations = Arrays.stream(context.getRequiredTestClass().getAnnotations())
            .map(annotation -> "@" + annotation.annotationType().getSimpleName()
                  + (annotationHasArguments(annotation)
                  ? formatAnnotationArguments(annotation) : ""))
            .collect(Collectors.joining("\n"));

      return annotations.isEmpty() ? "No class annotations" : annotations;
   }


   /**
    * Retrieves and formats annotations present on the test method associated with the provided extension context.
    *
    * @param context the JUnit extension context containing test method information
    * @return a string containing formatted method annotations
    */
   static String getMethodAnnotations(ExtensionContext context) {
      return Arrays.stream(context.getRequiredTestMethod().getAnnotations())
            .map(ObjectFormatter::formatAnnotation)
            .collect(Collectors.joining("\n"));
   }


   /**
    * Formats an annotation into a string including its simple name and any arguments.
    *
    * @param annotation the annotation to format
    * @return a string representation of the annotation
    */
   private static String formatAnnotation(Annotation annotation) {
      String annotationName = annotation.annotationType().getSimpleName();
      String arguments = formatAnnotationArguments(annotation);

      return "@" + annotationName + (arguments.isEmpty() ? "" : arguments);
   }


   /**
    * Determines whether the provided annotation has any arguments.
    *
    * @param annotation the annotation to check
    * @return true if the annotation has arguments, false otherwise
    */
   private static boolean annotationHasArguments(Annotation annotation) {
      return Arrays.stream(annotation.annotationType().getDeclaredMethods())
            .anyMatch(method -> {
               try {
                  Object defaultValue = method.getDefaultValue();
                  Object actualValue = method.invoke(annotation);
                  return defaultValue == null || !defaultValue.equals(actualValue);
               } catch (IllegalAccessException | InvocationTargetException | SecurityException e) {
                  return false;
               } catch (Exception e) {
                  throw new RuntimeException("Unexpected exception occurred while processing annotation", e);
               }
            });
   }


   /**
    * Formats the arguments of an annotation into a comma-separated string enclosed in parentheses.
    *
    * @param annotation the annotation whose arguments will be formatted
    * @return a string representation of the annotation's arguments
    */
   private static String formatAnnotationArguments(Annotation annotation) {
      List<String> args = Arrays.stream(annotation.annotationType().getDeclaredMethods())
            .map(method -> {
               try {
                  Object value = method.invoke(annotation);
                  if (value.getClass().isArray()) {
                     value = Arrays.toString((Object[]) value)
                           .replace("[", "")
                           .replace("]", "")
                           .replace(",", "");
                  }
                  if (value.equals(method.getDefaultValue())) {
                     return "";
                  }
                  return method.getName() + "=" + value;
               } catch (IllegalAccessException | InvocationTargetException | SecurityException e) {
                  return method.getName() + "=error";
               } catch (Exception e) {
                  throw new RuntimeException("Unexpected exception occurred while formatting annotation arguments", e);
               }
            })
            .filter(arg -> !arg.isEmpty())
            .toList();

      return args.isEmpty() ? "" : args.stream().collect(Collectors.joining(", ", "(", ")"));
   }


   /**
    * Formats a string of annotations into HTML rows with proper indentation.
    *
    * @param annotations the raw annotations string to format
    * @return a string containing HTML formatted annotations
    */
   static String formatAnnotationsToNewRows(String annotations) {
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


   /**
    * Cleans the annotation string by removing package names and unnecessary formatting.
    *
    * @param annotation the raw annotation string
    * @return a cleaned annotation string
    */
   private static String cleanAnnotation(String annotation) {
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


   /**

    * Applies indentation to the provided text based on the base indent level,
    * formatting braces and commas appropriately.
    *
    * @param text            the text to format
    * @param baseIndentLevel the base level of indentation
    * @return the text with applied indentation
    */
   private static String applyIndentation(String text, int baseIndentLevel) {
      return text
            .replaceAll("\\{", "{\n" + getIndent(baseIndentLevel + 1))
            .replaceAll("}", "\n" + getIndent(baseIndentLevel) + "}")
            .replaceAll("\\[", "[\n" + getIndent(baseIndentLevel + 1))
            .replaceAll("]", "\n" + getIndent(baseIndentLevel) + "]")
            .replaceAll(",", ",\n" + getIndent(baseIndentLevel + 1))
            .replaceAll("\n\\s*\n", "\n");
   }


   /**
    * Generates a string containing spaces for indentation.
    *
    * @param level the number of indent levels to generate
    * @return a string representing the indentation (each level consists of 4 spaces)
    */
   private static String getIndent(int level) {
      return "    ".repeat(level);
   }


   /**
    * Counts the number of occurrences of a target substring within a given string.
    *
    * @param str    the string to search in
    * @param target the substring to count
    * @return the count of occurrences of the target in the string
    */
   private static int countOccurrences(String str, String target) {
      return str.length() - str.replace(target, "").length();
   }


   /**
    * Formats a long text into multiple lines with a maximum of 80 characters per line, escaping HTML characters.
    *
    * @param text the text to format
    * @return a string with formatted long text
    */
   static String formatLongText(String text) {
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


   /**
    * Retrieves the test method's argument types from the provided extension context.
    *
    * @param context the JUnit extension context containing test instance and method information
    * @return a string listing the test method's argument types or a message if none are available
    */
   static String getTestArguments(ExtensionContext context) {
      return context.getTestInstance()
            .flatMap(instance -> context.getTestMethod()
                  .map(method -> Arrays.stream(method.getParameters())
                        .map(param -> param.getType().getSimpleName())
                        .collect(Collectors.joining(", "))))
            .orElse("No arguments available.");
   }


   /**
    * Formats processed journey data along with the corresponding original data into a readable string.
    *
    * @param originalData  an array of JourneyData annotations associated with the data
    * @param processedData an array of processed data objects corresponding to the original data
    * @return a formatted string representing the processed journey data
    */
   public static String formatProcessedData(JourneyData[] originalData, Object[] processedData) {
      if (processedData == null || processedData.length == 0) {
         return "No data available";
      }

      StringBuilder sb = new StringBuilder();
      sb.append("Processed Journey Data:\n");
      sb.append("-------------------\n");

      for (int i = 0; i < processedData.length; i++) {
         Object data = processedData[i];
         String journeyDataValue = extractJourneyDataValue(originalData[i]);

         sb.append(String.format("Journey: %s%n", journeyDataValue));

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


   /**
    * Extracts the value from a JourneyData annotation.
    *
    * @param journeyData the JourneyData annotation
    * @return the value contained in the annotation
    */
   private static String extractJourneyDataValue(JourneyData journeyData) {
      return journeyData.value();
   }


   /**
    * Formats an object as JSON using the configured ObjectMapper and appends it to the provided StringBuilder.
    * If JSON formatting fails, falls back to calling the object's toString method.
    *
    * @param sb   the StringBuilder to append the formatted JSON to
    * @param data the data object to format as JSON
    */
   private static void formatJsonData(StringBuilder sb, Object data) {
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
