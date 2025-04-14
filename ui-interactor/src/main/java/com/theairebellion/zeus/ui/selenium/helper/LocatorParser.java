package com.theairebellion.zeus.ui.selenium.helper;

import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

/**
 * Utility class for parsing and handling locators in Selenium.
 *
 * <p>This class provides methods for extracting locators from exception messages,
 * updating WebElement references, and converting locator strings into By objects.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class LocatorParser {

   private static final String LOCATOR_NOT_FOUND_MESSAGE = "Locator not found";
   private static final String NO_MESSAGE_AVAILABLE = "No message available";

   /**
    * Updates the WebElement reference by parsing locators from the element string representation.
    *
    * @param driver  The WebDriver instance.
    * @param element The SmartWebElement to relocate.
    * @return A newly located SmartWebElement.
    */
   public static SmartWebElement updateWebElement(WebDriver driver, SmartWebElement element) {
      LogUi.extended("Element: '{}' is being relocated.", element.toString());
      List<By> locatorsList = new ArrayList<>();
      try {
         locatorsList = parseLocators(element.toString());
      } catch (Exception ignored) {
      }
      if (locatorsList.isEmpty()) {
         throw new RuntimeException("Element can't be auto updated");
      }
      return new SmartWebDriver(driver).findSmartElement(
            new ByChained(locatorsList.toArray(new By[0])), 10);
   }

   /**
    * Extracts the blocking element locator from an exception message.
    *
    * @param exceptionMessage The exception message containing locator details.
    * @return The XPath locator of the blocking element.
    */
   public static String extractBlockingElementLocator(String exceptionMessage) {
      String pattern = "Other element would receive the click: <(\\w+)([^>]*)>";
      Matcher matcher = Pattern.compile(pattern).matcher(exceptionMessage);
      if (matcher.find()) {
         String tag = matcher.group(1);
         String attributes = matcher.group(2).trim();
         if (attributes.isEmpty()) {
            return "//" + tag;
         }
         String[] attrPairs = attributes.split("\\s+");
         StringBuilder xpath = new StringBuilder("//" + tag + "[");
         for (int i = 0; i < attrPairs.length; i++) {
            String[] keyValue = attrPairs[i].split("=");
            if (keyValue.length == 2) {
               String key = keyValue[0];
               String value = keyValue[1].replace("\"", "").replace("'", "");
               xpath.append("@").append(key).append("='").append(value).append("'");
               if (i < attrPairs.length - 1) {
                  xpath.append(" and ");
               }
            }
         }
         xpath.append("]");
         return xpath.toString();
      } else {
         throw new IllegalArgumentException("Can't extract locator from exception message: " + exceptionMessage + ".");
      }
   }

   /**
    * Extracts a locator from a given array of arguments.
    *
    * @param args The array containing locator arguments.
    * @return The extracted By locator, or null if not found.
    */
   public static By extractLocator(Object[] args) {
      if (args != null && args.length > 0 && args[0] instanceof By) {
         return (By) args[0];
      }
      return null;
   }

   /**
    * Extracts a locator from an exception message.
    *
    * @param message The exception message.
    * @return The extracted locator string, or a default message if not found.
    */
   public static String extractLocatorFromMessage(String message) {
      if (message == null) {
         return NO_MESSAGE_AVAILABLE;
      }
      String locatorPattern = "By\\.(\\w+)\\((.*?)\\)";
      Pattern pattern = Pattern.compile(locatorPattern);
      Matcher matcher = pattern.matcher(message);
      return matcher.find() ? matcher.group() : LOCATOR_NOT_FOUND_MESSAGE;
   }

   /**
    * Retrieves detailed information about a WebElement.
    *
    * @param element   The target WebElement.
    * @param outerHtml The outer HTML representation of the element.
    * @param innerHtml The inner HTML representation of the element.
    * @return A formatted string with element details.
    */
   public static String getElementDetails(WebElement element, String outerHtml, String innerHtml) {
      String tag = element.getTagName();
      String text = element.getText();
      return String.format("Tag: [%s]%nText: [%s]%nOuter HTML: [%s]%nInner HTML: [%s]",
            tag, text, outerHtml, innerHtml);
   }

   /**
    * Parses locators from a locator string representation.
    *
    * @param locatorString The string representation of locators.
    * @return A list of By locators extracted from the string.
    */
   private static List<By> parseLocators(String locatorString) {
      String[] parts = locatorString.split("->");
      List<By> byList = new ArrayList<>();
      for (int i = 1; i < parts.length; i++) {
         String trimmed = parts[i].trim();
         addLocatorIfMatches(byList, trimmed, "tag name", By::tagName);
         addLocatorIfMatches(byList, trimmed, "css selector", By::cssSelector);
         addLocatorIfMatches(byList, trimmed, "xpath", By::xpath);
         addLocatorIfMatches(byList, trimmed, "id", By::id);
         addLocatorIfMatches(byList, trimmed, "class name", By::className);
         addLocatorIfMatches(byList, trimmed, "name", By::name);
         addLocatorIfMatches(byList, trimmed, "link text", By::linkText);
         addLocatorIfMatches(byList, trimmed, "partial link text", By::partialLinkText);
      }
      return byList;
   }

   /**
    * Adds a locator to the list if the given key matches a known locator type.
    *
    * @param locatorList     The list to which the locator should be added.
    * @param locatorText     The locator text representation.
    * @param key             The key representing a locator type.
    * @param locatorFunction The function to generate a By locator.
    */
   private static void addLocatorIfMatches(List<By> locatorList, String locatorText, String key,
                                           Function<String, By> locatorFunction) {
      if (locatorText.split(":")[0].equals(key)) {
         String locator = extractLocatorValue(locatorText);
         locatorList.add(locatorFunction.apply(locator));
      }
   }

   /**
    * Extracts the value of a locator from its text representation.
    *
    * @param locatorText The text containing the locator value.
    * @return The extracted locator value.
    */
   private static String extractLocatorValue(String locatorText) {
      String[] parts = locatorText.split(":");
      String value = parts[1].trim();
      return value.substring(0, value.length() - 2);
   }
}
