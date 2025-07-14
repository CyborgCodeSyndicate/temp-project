package com.theairebellion.zeus.ui.selenium.shadowroot;

import com.theairebellion.zeus.ui.selenium.exceptions.UiInteractionException;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Utility class for interacting with Shadow DOM elements using Selenium.
 * Provides methods to locate elements within Shadow Roots, including
 * support for waiting and polling mechanisms.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class ShadowDomUtils {

   /**
    * Default polling interval in milliseconds if not explicitly specified.
    */
   private static final long DEFAULT_POLL_INTERVAL_MS = 500;
   private static final String VALUE = "value";
   private static final String MAX_WAIT = "maxWait";
   private static final String POLL_INTERVAL = "pollInterval";

   private ShadowDomUtils() {
   }

   /**
    * Converts a Selenium {@code By} locator into a map representation.
    * This is used by JavaScript scripts for element searching.
    *
    * @param by Selenium {@code By} locator.
    * @return A map containing the locator type and value.
    * @throws IllegalArgumentException if an unsupported locator type is used.
    */
   private static Map<String, Object> parseBy(By by) {
      String asString = by.toString(); // e.g. "By.id: username"

      if (asString.startsWith("By.id: ")) {
         return Map.of("type", "id",
               VALUE, asString.substring("By.id: ".length()).trim());
      } else if (asString.startsWith("By.name: ")) {
         return Map.of("type", "name",
               VALUE, asString.substring("By.name: ".length()).trim());
      } else if (asString.startsWith("By.className: ")) {
         return Map.of("type", "className",
               VALUE, asString.substring("By.className: ".length()).trim());
      } else if (asString.startsWith("By.cssSelector: ")) {
         return Map.of("type", "css",
               VALUE, asString.substring("By.cssSelector: ".length()).trim());
      } else if (asString.startsWith("By.tagName: ")) {
         return Map.of("type", "tagName",
               VALUE, asString.substring("By.tagName: ".length()).trim());
      } else if (asString.startsWith("By.linkText: ")) {
         return Map.of("type", "linkText",
               VALUE, asString.substring("By.linkText: ".length()).trim());
      } else if (asString.startsWith("By.partialLinkText: ")) {
         return Map.of("type", "partialLinkText",
               VALUE, asString.substring("By.partialLinkText: ".length()).trim());
      } else if (asString.startsWith("By.xpath: ")) {
         throw new IllegalArgumentException(
               "Xpath selectors are not supported inside shadow roots. Please change your xpath selector: "
                     + asString + "to any other selenium supported selector");
      } else {
         throw new IllegalArgumentException("Unsupported By type: " + asString);
      }
   }

   // -------------------------------------------------------------------------------------------
   // 1) SCRIPTS FOR FINDING A SINGLE ELEMENT (with optional in-JS waiting)
   // -------------------------------------------------------------------------------------------

   /**
    * JavaScript function to locate a single shadow DOM element.
    * The script searches for elements inside shadow roots and supports
    * different locator strategies such as id, name, class, CSS, tag name,
    * link text, and partial link text. XPath is not supported within shadow DOMs.
    */
   private static final String FIND_SHADOW_ELEMENT_JS = """
         function findShadowElement(params) {
           const type = params.type;
           const value = params.value;
           const maxWait = params.maxWait || 10000;
           const pollInterval = params.pollInterval || 500;
         
           function findInRoot(root, type, value) {
             switch (type) {
               case 'id':
                 return root.querySelector('#' + value);
               case 'name':
                 return root.querySelector('[name="' + value + '"]');
               case 'className':
                 var classes = value.trim().split(/\\s+/).join('.');
                 return root.querySelector('.' + classes);
               case 'css':
                 return root.querySelector(value);
               case 'tagName':
                 return root.querySelector(value);
               case 'xpath':
                 return document.evaluate(
                   value,
                   root,
                   null,
                   XPathResult.FIRST_ORDERED_NODE_TYPE,
                   null
                 ).singleNodeValue;
               case 'linkText':
                 var anchors = root.querySelectorAll('a');
                 for (var i = 0; i < anchors.length; i++) {
                   if (anchors[i].textContent === value) {
                     return anchors[i];
                   }
                 }
                 return null;
               case 'partialLinkText':
                 var anchors2 = root.querySelectorAll('a');
                 for (var j = 0; j < anchors2.length; j++) {
                   if (anchors2[j].textContent.includes(value)) {
                     return anchors2[j];
                   }
                 }
                 return null;
               default:
                 return null;
             }
           }
         
           function searchShadow(rootNodes, type, value) {
             for (var i = 0; i < rootNodes.length; i++) {
               var node = rootNodes[i];
               if (node && node.shadowRoot) {
                 var found = findInRoot(node.shadowRoot, type, value);
                 if (found) {
                   return found;
                 }
                 // Recurse deeper
                 var deeper = searchShadow(node.shadowRoot.querySelectorAll('*'), type, value);
                 if (deeper) {
                   return deeper;
                 }
               }
             }
             return null;
           }
         
           const startTime = performance.now();
           while ((performance.now() - startTime) < maxWait) {
             // 1) Try main document
             var direct = findInRoot(document, type, value);
             if (direct) {
               return direct;
             }
             // 2) Search shadow
             var all = document.querySelectorAll('*');
             var shadowFound = searchShadow(all, type, value);
             if (shadowFound) {
               return shadowFound;
             }
             // 3) Wait/poll
             var now = performance.now();
             while (performance.now() - now < pollInterval) {
               // busy wait - will block the page
             }
           }
           return null; // not found
         }
         return findShadowElement(arguments[0]);
         """;


   /**
    * Finds the first matching element across the entire document, including nested shadow roots.
    * Waits up to the default timeout duration, polling at defined intervals.
    *
    * @param driver The {@link SmartWebDriver} instance.
    * @param by     The {@link By} locator to use.
    * @return The found {@link SmartWebElement}, or null if not found.
    */
   public static SmartWebElement findElementInShadowRoots(SmartWebDriver driver, By by) {
      long wait = getUiConfig().waitDuration()
            * 1000L;
      return findElementInShadowRoots(driver, by, wait);
   }

   /**
    * Finds the first matching element across the document and nested shadow roots with a specified timeout.
    *
    * @param driver       The {@link SmartWebDriver} instance.
    * @param by           The {@link By} locator.
    * @param waitInMillis Maximum wait time in milliseconds.
    * @return The found {@link SmartWebElement}, or null if not found.
    */
   public static SmartWebElement findElementInShadowRoots(SmartWebDriver driver, By by, long waitInMillis) {
      JavascriptExecutor js = (JavascriptExecutor) driver.getOriginal();
      Map<String, Object> selector = parseBy(by);

      selector = new java.util.HashMap<>(selector);
      selector.put(MAX_WAIT, waitInMillis);
      selector.put(POLL_INTERVAL, DEFAULT_POLL_INTERVAL_MS);

      WebElement element = (WebElement) js.executeScript(FIND_SHADOW_ELEMENT_JS, selector);
      if (element == null) {
         throw new UiInteractionException("Finding element in shadow root via java script failed");
      }

      return new SmartWebElement(element, driver.getOriginal());
   }

   /**
    * Finds the first matching element in nested Shadow DOMs, starting from a given root element.
    * Waits up to the configured maximum wait time, polling every {@code DEFAULT_POLL_INTERVAL_MS}.
    *
    * @param root The root {@link SmartWebElement} to start the search from.
    * @param by   The {@link By} locator to use for finding the element.
    * @return The found {@link SmartWebElement}, or {@code null} if no element is found within the time limit.
    */
   public static SmartWebElement findElementInShadowRoots(SmartWebElement root, By by) {
      if (root == null) {
         return null;
      }
      WebDriver driver = root.getDriver();
      JavascriptExecutor js = (JavascriptExecutor) driver;

      Map<String, Object> selector = parseBy(by);
      selector = new java.util.HashMap<>(selector);
      long wait = getUiConfig().waitDuration() * 1000L;
      selector.put(MAX_WAIT, wait);
      selector.put(POLL_INTERVAL, DEFAULT_POLL_INTERVAL_MS);

      WebElement element = (WebElement) js.executeScript(FIND_SHADOW_ELEMENT_FROM_ELEMENT_JS, root.getOriginal(),
            selector);
      if (element == null) {
         throw new UiInteractionException("Finding element in shadow root via java script failed");
      }

      return new SmartWebElement(element, driver);
   }

   /**
    * JavaScript function to locate a single shadow DOM element.
    * The script searches for elements inside shadow roots and supports
    * different locator strategies such as id, name, class, CSS, tag name,
    * link text, and partial link text. XPath is not supported within shadow DOMs.
    */
   private static final String FIND_SHADOW_ELEMENT_FROM_ELEMENT_JS = """
         function findShadowElementFromRoot(rootElem, params) {
           const type = params.type;
           const value = params.value;
           const maxWait = params.maxWait || 10000;
           const pollInterval = params.pollInterval || 500;
         
           function findInRoot(root, type, value) {
             // same approach as above
             switch (type) {
               case 'id':
                 return root.querySelector('#' + value);
               case 'name':
                 return root.querySelector('[name="' + value + '"]');
               case 'className':
                 var classes = value.trim().split(/\\s+/).join('.');
                 return root.querySelector('.' + classes);
               case 'css':
                 return root.querySelector(value);
               case 'tagName':
                 return root.querySelector(value);
               case 'xpath':
                 return document.evaluate(value, root, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
               case 'linkText':
                 var anchors = root.querySelectorAll('a');
                 for (var i = 0; i < anchors.length; i++) {
                   if (anchors[i].textContent === value) {
                     return anchors[i];
                   }
                 }
                 return null;
               case 'partialLinkText':
                 var anchors2 = root.querySelectorAll('a');
                 for (var j = 0; j < anchors2.length; j++) {
                   if (anchors2[j].textContent.includes(value)) {
                     return anchors2[j];
                   }
                 }
                 return null;
               default:
                 return null;
             }
           }
         
           function searchShadow(rootNodes, type, value) {
             for (var i = 0; i < rootNodes.length; i++) {
               var node = rootNodes[i];
               if (node && node.shadowRoot) {
                 var found = findInRoot(node.shadowRoot, type, value);
                 if (found) {
                   return found;
                 }
                 var deeper = searchShadow(node.shadowRoot.querySelectorAll('*'), type, value);
                 if (deeper) {
                   return deeper;
                 }
               }
             }
             return null;
           }
         
           const startTime = performance.now();
           while ((performance.now() - startTime) < maxWait) {
             // 1) if rootElem itself has a shadowRoot
             if (rootElem.shadowRoot) {
               var shadowDirect = findInRoot(rootElem.shadowRoot, type, value);
               if (shadowDirect) {
                 return shadowDirect;
               }
               var shadowDeep = searchShadow(rootElem.shadowRoot.querySelectorAll('*'), type, value);
               if (shadowDeep) {
                 return shadowDeep;
               }
             }
         
             // 2) check rootElem itself as a normal DOM container
             var direct = findInRoot(rootElem, type, value);
             if (direct) {
               return direct;
             }
         
             // 3) check children shadow roots
             var children = rootElem.querySelectorAll('*');
             var deeper = searchShadow(children, type, value);
             if (deeper) {
               return deeper;
             }
         
             // 4) poll
             var now = performance.now();
             while (performance.now() - now < pollInterval) {
               // busy wait
             }
           }
           return null;
         }
         return findShadowElementFromRoot(arguments[0], arguments[1]);
         """;

   // -------------------------------------------------------------------------------------------
   // 2) SCRIPTS FOR FINDING MULTIPLE ELEMENTS + WAIT
   // -------------------------------------------------------------------------------------------

   /**
    * JavaScript function to find all matching elements within Shadow DOMs.
    * The script recursively searches the main document and any shadow roots
    * for elements that match the given selector. The results are collected
    * and returned as an array of matching elements.
    */
   private static final String FIND_SHADOW_ELEMENTS_JS = """
         function findShadowElements(params) {
           const type = params.type;
           const value = params.value;
           const maxWait = params.maxWait || 10000;
           const pollInterval = params.pollInterval || 500;
         
           // We'll collect all matches in an array.
           function collectInRoot(root, type, value) {
             let nodeList = [];
             switch (type) {
               case 'id':
                 let elById = root.querySelector('#' + value);
                 if (elById) nodeList.push(elById);
                 break;
               case 'name':
                 nodeList = root.querySelectorAll('[name="' + value + '"]');
                 break;
               case 'className':
                 var classes = value.trim().split(/\\s+/).join('.');
                 nodeList = root.querySelectorAll('.' + classes);
                 break;
               case 'css':
                 nodeList = root.querySelectorAll(value);
                 break;
               case 'tagName':
                 nodeList = root.querySelectorAll(value);
                 break;
               case 'xpath':
                 let xresult = document.evaluate(value, root, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
                 for (let i = 0; i < xresult.snapshotLength; i++) {
                   nodeList.push(xresult.snapshotItem(i));
                 }
                 break;
               case 'linkText':
                 let allAnchors = root.querySelectorAll('a');
                 allAnchors.forEach(a => {
                   if (a.textContent === value) nodeList.push(a);
                 });
                 break;
               case 'partialLinkText':
                 let anchors2 = root.querySelectorAll('a');
                 anchors2.forEach(a => {
                   if (a.textContent.includes(value)) nodeList.push(a);
                 });
                 break;
             }
             return Array.from(nodeList);
           }
         
           function collectShadow(rootNodes, type, value, results) {
             for (var i = 0; i < rootNodes.length; i++) {
               var node = rootNodes[i];
               if (node && node.shadowRoot) {
                 // collect in this shadow root
                 results.push(...collectInRoot(node.shadowRoot, type, value));
                 // go deeper
                 collectShadow(node.shadowRoot.querySelectorAll('*'), type, value, results);
               }
             }
           }
         
           function doSearch() {
             let found = [];
             // 1) main document
             found.push(...collectInRoot(document, type, value));
             // 2) shadow
             let all = document.querySelectorAll('*');
             collectShadow(all, type, value, found);
             return found;
           }
         
           const startTime = performance.now();
           while ((performance.now() - startTime) < maxWait) {
             let result = doSearch();
             if (result.length > 0) {
               return result; // return array of elements
             }
             // poll
             var now = performance.now();
             while (performance.now() - now < pollInterval) {
               // busy wait
             }
           }
           return [];
         }
         return findShadowElements(arguments[0]);
         """;

   /**
    * Finds all matching elements across the document and nested shadow roots.
    * Waits up to the configured maximum wait time, polling every {@code DEFAULT_POLL_INTERVAL_MS}.
    *
    * @param driver The {@link SmartWebDriver} instance.
    * @param by     The {@link By} locator to use for finding the elements.
    * @return A list of {@link SmartWebElement} containing all matching elements, or an empty list if no elements
    *       are found.
    */
   public static java.util.List<SmartWebElement> findElementsInShadowRoots(SmartWebDriver driver, By by) {
      JavascriptExecutor js = (JavascriptExecutor) driver.getOriginal();

      Map<String, Object> selector = parseBy(by);
      selector = new java.util.HashMap<>(selector);
      long wait = getUiConfig().waitDuration() * 1000L;
      selector.put(MAX_WAIT, wait);
      selector.put(POLL_INTERVAL, DEFAULT_POLL_INTERVAL_MS);

      Object result = js.executeScript(FIND_SHADOW_ELEMENTS_JS, selector);
      // The returned object should be a List<RemoteWebElement> if found
      if (result instanceof java.util.List) {
         return ((java.util.List<WebElement>) result).stream()
               .map(webElement -> new SmartWebElement(webElement, driver.getOriginal())).toList();
      }
      // otherwise return empty
      return java.util.Collections.emptyList();
   }

   /**
    * Finds all matching elements within a given Shadow DOM root.
    * Waits up to the configured maximum wait time, polling every {@code DEFAULT_POLL_INTERVAL_MS}.
    *
    * @param root The root {@link SmartWebElement} to start the search from.
    * @param by   The {@link By} locator to use for finding the elements.
    * @return A list of {@link SmartWebElement} containing all matching elements, or an empty list if no elements
    *       are found.
    */
   public static java.util.List<SmartWebElement> findElementsInShadowRoots(SmartWebElement root, By by) {
      if (root == null) {
         return java.util.Collections.emptyList();
      }
      WebDriver driver = root.getDriver();
      JavascriptExecutor js = (JavascriptExecutor) driver;

      Map<String, Object> selector = parseBy(by);
      selector = new java.util.HashMap<>(selector);
      long wait = getUiConfig().waitDuration() * 1000L;
      selector.put(MAX_WAIT, wait);
      selector.put(POLL_INTERVAL, DEFAULT_POLL_INTERVAL_MS);

      Object result = js.executeScript(FIND_SHADOW_ELEMENTS_FROM_ELEMENT_JS, root.getOriginal(), selector);
      if (result instanceof java.util.List) {
         return ((java.util.List<WebElement>) result).stream()
               .map(webElement -> new SmartWebElement(webElement, driver)).toList();
      }
      return java.util.Collections.emptyList();
   }

   /**
    * JavaScript function to find all matching elements starting from a given root element.
    * The script searches within the specified root element and its shadow DOM,
    * collecting all elements that match the given selector.
    *
    * <p>Supports standard Selenium locator types including ID, name, class, CSS selector,
    * tag name, link text, and partial link text. XPath is not supported.
    */
   private static final String FIND_SHADOW_ELEMENTS_FROM_ELEMENT_JS = """
         function findShadowElementsFromRoot(rootElem, params) {
           const type = params.type;
           const value = params.value;
           const maxWait = params.maxWait || 10000;
           const pollInterval = params.pollInterval || 500;
         
           function collectInRoot(root, type, value) {
             let nodeList = [];
             switch (type) {
               case 'id':
                 let elById = root.querySelector('#' + value);
                 if (elById) nodeList.push(elById);
                 break;
               case 'name':
                 nodeList = root.querySelectorAll('[name="' + value + '"]');
                 break;
               case 'className':
                 var classes = value.trim().split(/\\s+/).join('.');
                 nodeList = root.querySelectorAll('.' + classes);
                 break;
               case 'css':
                 nodeList = root.querySelectorAll(value);
                 break;
               case 'tagName':
                 nodeList = root.querySelectorAll(value);
                 break;
               case 'xpath':
                 let xres = document.evaluate(value, root, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
                 for (let i = 0; i < xres.snapshotLength; i++) {
                   nodeList.push(xres.snapshotItem(i));
                 }
                 break;
               case 'linkText':
                 let allAnchors = root.querySelectorAll('a');
                 allAnchors.forEach(a => { if (a.textContent === value) nodeList.push(a); });
                 break;
               case 'partialLinkText':
                 let anchors2 = root.querySelectorAll('a');
                 anchors2.forEach(a => { if (a.textContent.includes(value)) nodeList.push(a); });
                 break;
             }
             return Array.from(nodeList);
           }
         
           function collectShadow(rootNodes, type, value, results) {
             for (var i = 0; i < rootNodes.length; i++) {
               var node = rootNodes[i];
               if (node && node.shadowRoot) {
                 results.push(...collectInRoot(node.shadowRoot, type, value));
                 collectShadow(node.shadowRoot.querySelectorAll('*'), type, value, results);
               }
             }
           }
         
           function doSearch() {
             let found = [];
             // Check rootElem's shadow root
             if (rootElem.shadowRoot) {
               found.push(...collectInRoot(rootElem.shadowRoot, type, value));
               collectShadow(rootElem.shadowRoot.querySelectorAll('*'), type, value, found);
             }
             // Also treat rootElem as a normal DOM container
             found.push(...collectInRoot(rootElem, type, value));
             collectShadow(rootElem.querySelectorAll('*'), type, value, found);
             return found;
           }
         
           const startTime = performance.now();
           while ((performance.now() - startTime) < maxWait) {
             let result = doSearch();
             if (result.length > 0) {
               return result;
             }
             var now = performance.now();
             while (performance.now() - now < pollInterval) {
               // busy wait
             }
           }
           return [];
         }
         return findShadowElementsFromRoot(arguments[0], arguments[1]);
         """;

   /**
    * Checks whether there are elements with Shadow DOM in the current document.
    *
    * @param driver The {@link SmartWebDriver} instance.
    * @return True if Shadow DOM elements are present, otherwise false.
    */
   public static boolean shadowRootElementsPresent(SmartWebDriver driver) {
      JavascriptExecutor js = (JavascriptExecutor) driver.getOriginal();
      String script =
            "return Array.from(document.querySelectorAll('*')).some(el => el.shadowRoot);";
      Object rawResult = js.executeScript(script);

      if (rawResult instanceof Boolean result) {
         return result;
      }

      return false;
   }


   /**
    * Checks if there is at least one element that has a Shadow Root starting from a given WebElement.
    *
    * @param root The root {@link SmartWebElement} to check.
    * @return True if the root element or any descendant has a Shadow Root, otherwise false.
    */
   public static boolean shadowRootElementsPresent(SmartWebElement root) {
      if (root
            == null) {
         return false;
      }
      WebDriver driver = root.getDriver();
      JavascriptExecutor js = (JavascriptExecutor) driver;

      // Check if the root itself has a shadowRoot or any of its descendants
      String script =
            "if (arguments[0].shadowRoot) { "
                  + "  return true; "
                  + "} else { "
                  + "  return Array.from(arguments[0].querySelectorAll('*')).some(el => el.shadowRoot);"
                  + "}";

      Object rawResult = js.executeScript(script, root.getOriginal());

      if (rawResult instanceof Boolean result) {
         return result;
      }

      return false;
   }

}