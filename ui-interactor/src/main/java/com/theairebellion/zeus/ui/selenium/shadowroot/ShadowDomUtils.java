package com.theairebellion.zeus.ui.selenium.shadowroot;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Map;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Utility class for interacting with Shadow DOM elements using Selenium.
 * Provides methods to locate elements within Shadow Roots, including
 * support for waiting and polling mechanisms.
 *
 * @author Cyborg Code Syndicate
 */
public class ShadowDomUtils {

    /**
     * Default polling interval in milliseconds if not explicitly specified.
     */
    private static final long DEFAULT_POLL_INTERVAL_MS = 500;


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
                    "value", asString.substring("By.id: ".length()).trim());
        } else if (asString.startsWith("By.name: ")) {
            return Map.of("type", "name",
                    "value", asString.substring("By.name: ".length()).trim());
        } else if (asString.startsWith("By.className: ")) {
            return Map.of("type", "className",
                    "value", asString.substring("By.className: ".length()).trim());
        } else if (asString.startsWith("By.cssSelector: ")) {
            return Map.of("type", "css",
                    "value", asString.substring("By.cssSelector: ".length()).trim());
        } else if (asString.startsWith("By.tagName: ")) {
            return Map.of("type", "tagName",
                    "value", asString.substring("By.tagName: ".length()).trim());
        } else if (asString.startsWith("By.linkText: ")) {
            return Map.of("type", "linkText",
                    "value", asString.substring("By.linkText: ".length()).trim());
        } else if (asString.startsWith("By.partialLinkText: ")) {
            return Map.of("type", "partialLinkText",
                    "value", asString.substring("By.partialLinkText: ".length()).trim());
        } else if (asString.startsWith("By.xpath: ")) {
            throw new IllegalArgumentException(
                    "Xpath selectors are not supported inside shadow roots. Please change your xpath selector: " + asString + "to any other selenium supported selector");
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
    private static final String FIND_SHADOW_ELEMENT_JS =
            "function findShadowElement(params) {\n" +
                    "  const type = params.type;\n" +
                    "  const value = params.value;\n" +
                    "  const maxWait = params.maxWait || 10000;\n" +
                    "  const pollInterval = params.pollInterval || 500;\n" +
                    "\n" +
                    "  function findInRoot(root, type, value) {\n" +
                    "    switch (type) {\n" +
                    "      case 'id':\n" +
                    "        return root.querySelector('#' + value);\n" +
                    "      case 'name':\n" +
                    "        return root.querySelector('[name=\"' + value + '\"]');\n" +
                    "      case 'className':\n" +
                    "        var classes = value.trim().split(/\\s+/).join('.');\n" +
                    "        return root.querySelector('.' + classes);\n" +
                    "      case 'css':\n" +
                    "        return root.querySelector(value);\n" +
                    "      case 'tagName':\n" +
                    "        return root.querySelector(value);\n" +
                    "      case 'xpath':\n" +
                    "        return document.evaluate(\n" +
                    "          value,\n" +
                    "          root,\n" +
                    "          null,\n" +
                    "          XPathResult.FIRST_ORDERED_NODE_TYPE,\n" +
                    "          null\n" +
                    "        ).singleNodeValue;\n" +
                    "      case 'linkText':\n" +
                    "        var anchors = root.querySelectorAll('a');\n" +
                    "        for (var i = 0; i < anchors.length; i++) {\n" +
                    "          if (anchors[i].textContent === value) {\n" +
                    "            return anchors[i];\n" +
                    "          }\n" +
                    "        }\n" +
                    "        return null;\n" +
                    "      case 'partialLinkText':\n" +
                    "        var anchors2 = root.querySelectorAll('a');\n" +
                    "        for (var j = 0; j < anchors2.length; j++) {\n" +
                    "          if (anchors2[j].textContent.includes(value)) {\n" +
                    "            return anchors2[j];\n" +
                    "          }\n" +
                    "        }\n" +
                    "        return null;\n" +
                    "      default:\n" +
                    "        return null;\n" +
                    "    }\n" +
                    "  }\n" +
                    "\n" +
                    "  function searchShadow(rootNodes, type, value) {\n" +
                    "    for (var i = 0; i < rootNodes.length; i++) {\n" +
                    "      var node = rootNodes[i];\n" +
                    "      if (node && node.shadowRoot) {\n" +
                    "        var found = findInRoot(node.shadowRoot, type, value);\n" +
                    "        if (found) {\n" +
                    "          return found;\n" +
                    "        }\n" +
                    "        // Recurse deeper\n" +
                    "        var deeper = searchShadow(node.shadowRoot.querySelectorAll('*'), type, value);\n" +
                    "        if (deeper) {\n" +
                    "          return deeper;\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }\n" +
                    "    return null;\n" +
                    "  }\n" +
                    "\n" +
                    "  const startTime = performance.now();\n" +
                    "  while ((performance.now() - startTime) < maxWait) {\n" +
                    "    // 1) Try main document\n" +
                    "    var direct = findInRoot(document, type, value);\n" +
                    "    if (direct) {\n" +
                    "      return direct;\n" +
                    "    }\n" +
                    "    // 2) Search shadow\n" +
                    "    var all = document.querySelectorAll('*');\n" +
                    "    var shadowFound = searchShadow(all, type, value);\n" +
                    "    if (shadowFound) {\n" +
                    "      return shadowFound;\n" +
                    "    }\n" +
                    "    // 3) Wait/poll\n" +
                    "    var now = performance.now();\n" +
                    "    while (performance.now() - now < pollInterval) {\n" +
                    "      // busy wait - will block the page\n" +
                    "    }\n" +
                    "  }\n" +
                    "  return null; // not found\n" +
                    "} \n" +
                    "return findShadowElement(arguments[0]);";


    /**
     * Finds the first matching element across the entire document, including nested shadow roots.
     * Waits up to the default timeout duration, polling at defined intervals.
     *
     * @param driver The {@link SmartWebDriver} instance.
     * @param by     The {@link By} locator to use.
     * @return The found {@link SmartWebElement}, or null if not found.
     */
    public static SmartWebElement findElementInShadowRoots(SmartWebDriver driver, By by) {
        long wait = getUiConfig().waitDuration() * 1000L;
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
        selector.put("maxWait", waitInMillis);
        selector.put("pollInterval", DEFAULT_POLL_INTERVAL_MS);

        WebElement element = (WebElement) js.executeScript(FIND_SHADOW_ELEMENT_JS, selector);
        return new SmartWebElement(element, driver.getOriginal());
    }

    /**
     * JavaScript function to locate a single shadow DOM element.
     * The script searches for elements inside shadow roots and supports
     * different locator strategies such as id, name, class, CSS, tag name,
     * link text, and partial link text. XPath is not supported within shadow DOMs.
     */
    private static final String FIND_SHADOW_ELEMENT_FROM_ELEMENT_JS =
            "function findShadowElementFromRoot(rootElem, params) {\n" +
                    "  const type = params.type;\n" +
                    "  const value = params.value;\n" +
                    "  const maxWait = params.maxWait || 10000;\n" +
                    "  const pollInterval = params.pollInterval || 500;\n" +
                    "\n" +
                    "  function findInRoot(root, type, value) {\n" +
                    "    // same approach as above\n" +
                    "    switch (type) {\n" +
                    "      case 'id':\n" +
                    "        return root.querySelector('#' + value);\n" +
                    "      case 'name':\n" +
                    "        return root.querySelector('[name=\"' + value + '\"]');\n" +
                    "      case 'className':\n" +
                    "        var classes = value.trim().split(/\\s+/).join('.');\n" +
                    "        return root.querySelector('.' + classes);\n" +
                    "      case 'css':\n" +
                    "        return root.querySelector(value);\n" +
                    "      case 'tagName':\n" +
                    "        return root.querySelector(value);\n" +
                    "      case 'xpath':\n" +
                    "        return document.evaluate(value, root, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;\n" +
                    "      case 'linkText':\n" +
                    "        var anchors = root.querySelectorAll('a');\n" +
                    "        for (var i = 0; i < anchors.length; i++) {\n" +
                    "          if (anchors[i].textContent === value) {\n" +
                    "            return anchors[i];\n" +
                    "          }\n" +
                    "        }\n" +
                    "        return null;\n" +
                    "      case 'partialLinkText':\n" +
                    "        var anchors2 = root.querySelectorAll('a');\n" +
                    "        for (var j = 0; j < anchors2.length; j++) {\n" +
                    "          if (anchors2[j].textContent.includes(value)) {\n" +
                    "            return anchors2[j];\n" +
                    "          }\n" +
                    "        }\n" +
                    "        return null;\n" +
                    "      default:\n" +
                    "        return null;\n" +
                    "    }\n" +
                    "  }\n" +
                    "\n" +
                    "  function searchShadow(rootNodes, type, value) {\n" +
                    "    for (var i = 0; i < rootNodes.length; i++) {\n" +
                    "      var node = rootNodes[i];\n" +
                    "      if (node && node.shadowRoot) {\n" +
                    "        var found = findInRoot(node.shadowRoot, type, value);\n" +
                    "        if (found) {\n" +
                    "          return found;\n" +
                    "        }\n" +
                    "        var deeper = searchShadow(node.shadowRoot.querySelectorAll('*'), type, value);\n" +
                    "        if (deeper) {\n" +
                    "          return deeper;\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }\n" +
                    "    return null;\n" +
                    "  }\n" +
                    "\n" +
                    "  const startTime = performance.now();\n" +
                    "  while ((performance.now() - startTime) < maxWait) {\n" +
                    "    // 1) if rootElem itself has a shadowRoot\n" +
                    "    if (rootElem.shadowRoot) {\n" +
                    "      var shadowDirect = findInRoot(rootElem.shadowRoot, type, value);\n" +
                    "      if (shadowDirect) {\n" +
                    "        return shadowDirect;\n" +
                    "      }\n" +
                    "      var shadowDeep = searchShadow(rootElem.shadowRoot.querySelectorAll('*'), type, value);\n" +
                    "      if (shadowDeep) {\n" +
                    "        return shadowDeep;\n" +
                    "      }\n" +
                    "    }\n" +
                    "\n" +
                    "    // 2) check rootElem itself as a normal DOM container\n" +
                    "    var direct = findInRoot(rootElem, type, value);\n" +
                    "    if (direct) {\n" +
                    "      return direct;\n" +
                    "    }\n" +
                    "\n" +
                    "    // 3) check children shadow roots\n" +
                    "    var children = rootElem.querySelectorAll('*');\n" +
                    "    var deeper = searchShadow(children, type, value);\n" +
                    "    if (deeper) {\n" +
                    "      return deeper;\n" +
                    "    }\n" +
                    "\n" +
                    "    // 4) poll\n" +
                    "    var now = performance.now();\n" +
                    "    while (performance.now() - now < pollInterval) {\n" +
                    "      // busy wait\n" +
                    "    }\n" +
                    "  }\n" +
                    "  return null;\n" +
                    "}\n" +
                    "return findShadowElementFromRoot(arguments[0], arguments[1]);";

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
        selector.put("maxWait", wait);
        selector.put("pollInterval", DEFAULT_POLL_INTERVAL_MS);

        WebElement element = (WebElement) js.executeScript(FIND_SHADOW_ELEMENT_FROM_ELEMENT_JS, root.getOriginal(),
                selector);
        return new SmartWebElement(element, driver);
    }

    // -------------------------------------------------------------------------------------------
    // 2) SCRIPTS FOR FINDING MULTIPLE ELEMENTS + WAIT
    // -------------------------------------------------------------------------------------------

    /**
     * JavaScript function to find all matching elements within Shadow DOMs.
     * The script recursively searches the main document and any shadow roots
     * for elements that match the given selector. The results are collected
     * and returned as an array of matching elements.
     */
    private static final String FIND_SHADOW_ELEMENTS_JS =
            "function findShadowElements(params) {\n" +
                    "  const type = params.type;\n" +
                    "  const value = params.value;\n" +
                    "  const maxWait = params.maxWait || 10000;\n" +
                    "  const pollInterval = params.pollInterval || 500;\n" +
                    "\n" +
                    "  // We'll collect all matches in an array.\n" +
                    "  function collectInRoot(root, type, value) {\n" +
                    "    let nodeList = [];\n" +
                    "    switch (type) {\n" +
                    "      case 'id':\n" +
                    "        let elById = root.querySelector('#' + value);\n" +
                    "        if (elById) nodeList.push(elById);\n" +
                    "        break;\n" +
                    "      case 'name':\n" +
                    "        nodeList = root.querySelectorAll('[name=\"' + value + '\"]');\n" +
                    "        break;\n" +
                    "      case 'className':\n" +
                    "        var classes = value.trim().split(/\\s+/).join('.');\n" +
                    "        nodeList = root.querySelectorAll('.' + classes);\n" +
                    "        break;\n" +
                    "      case 'css':\n" +
                    "        nodeList = root.querySelectorAll(value);\n" +
                    "        break;\n" +
                    "      case 'tagName':\n" +
                    "        nodeList = root.querySelectorAll(value);\n" +
                    "        break;\n" +
                    "      case 'xpath':\n" +
                    "        let xresult = document.evaluate(value, root, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);\n" +
                    "        for (let i = 0; i < xresult.snapshotLength; i++) {\n" +
                    "          nodeList.push(xresult.snapshotItem(i));\n" +
                    "        }\n" +
                    "        break;\n" +
                    "      case 'linkText':\n" +
                    "        let allAnchors = root.querySelectorAll('a');\n" +
                    "        allAnchors.forEach(a => {\n" +
                    "          if (a.textContent === value) nodeList.push(a);\n" +
                    "        });\n" +
                    "        break;\n" +
                    "      case 'partialLinkText':\n" +
                    "        let anchors2 = root.querySelectorAll('a');\n" +
                    "        anchors2.forEach(a => {\n" +
                    "          if (a.textContent.includes(value)) nodeList.push(a);\n" +
                    "        });\n" +
                    "        break;\n" +
                    "    }\n" +
                    "    return Array.from(nodeList);\n" +
                    "  }\n" +
                    "\n" +
                    "  function collectShadow(rootNodes, type, value, results) {\n" +
                    "    for (var i = 0; i < rootNodes.length; i++) {\n" +
                    "      var node = rootNodes[i];\n" +
                    "      if (node && node.shadowRoot) {\n" +
                    "        // collect in this shadow root\n" +
                    "        results.push(...collectInRoot(node.shadowRoot, type, value));\n" +
                    "        // go deeper\n" +
                    "        collectShadow(node.shadowRoot.querySelectorAll('*'), type, value, results);\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }\n" +
                    "\n" +
                    "  function doSearch() {\n" +
                    "    let found = [];\n" +
                    "    // 1) main document\n" +
                    "    found.push(...collectInRoot(document, type, value));\n" +
                    "    // 2) shadow\n" +
                    "    let all = document.querySelectorAll('*');\n" +
                    "    collectShadow(all, type, value, found);\n" +
                    "    return found;\n" +
                    "  }\n" +
                    "\n" +
                    "  const startTime = performance.now();\n" +
                    "  while ((performance.now() - startTime) < maxWait) {\n" +
                    "    let result = doSearch();\n" +
                    "    if (result.length > 0) {\n" +
                    "      return result; // return array of elements\n" +
                    "    }\n" +
                    "    // poll\n" +
                    "    var now = performance.now();\n" +
                    "    while (performance.now() - now < pollInterval) {\n" +
                    "      // busy wait\n" +
                    "    }\n" +
                    "  }\n" +
                    "  return [];\n" +
                    "}\n" +
                    "return findShadowElements(arguments[0]);";

    /**
     * Finds all matching elements across the document and nested shadow roots.
     * Waits up to the configured maximum wait time, polling every {@code DEFAULT_POLL_INTERVAL_MS}.
     *
     * @param driver The {@link SmartWebDriver} instance.
     * @param by     The {@link By} locator to use for finding the elements.
     * @return A list of {@link SmartWebElement} containing all matching elements, or an empty list if no elements are found.
     */
    public static java.util.List<SmartWebElement> findElementsInShadowRoots(SmartWebDriver driver, By by) {
        JavascriptExecutor js = (JavascriptExecutor) driver.getOriginal();

        Map<String, Object> selector = parseBy(by);
        selector = new java.util.HashMap<>(selector);
        long wait = getUiConfig().waitDuration() * 1000L;
        selector.put("maxWait", wait);
        selector.put("pollInterval", DEFAULT_POLL_INTERVAL_MS);

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
     * JavaScript function to find all matching elements starting from a given root element.
     * The script searches within the specified root element and its shadow DOM,
     * collecting all elements that match the given selector.
     * <p>
     * Supports standard Selenium locator types including ID, name, class, CSS selector,
     * tag name, link text, and partial link text. XPath is not supported.
     */
    private static final String FIND_SHADOW_ELEMENTS_FROM_ELEMENT_JS =
            "function findShadowElementsFromRoot(rootElem, params) {\n" +
                    "  const type = params.type;\n" +
                    "  const value = params.value;\n" +
                    "  const maxWait = params.maxWait || 10000;\n" +
                    "  const pollInterval = params.pollInterval || 500;\n" +
                    "\n" +
                    "  function collectInRoot(root, type, value) {\n" +
                    "    let nodeList = [];\n" +
                    "    switch (type) {\n" +
                    "      case 'id':\n" +
                    "        let elById = root.querySelector('#' + value);\n" +
                    "        if (elById) nodeList.push(elById);\n" +
                    "        break;\n" +
                    "      case 'name':\n" +
                    "        nodeList = root.querySelectorAll('[name=\"' + value + '\"]');\n" +
                    "        break;\n" +
                    "      case 'className':\n" +
                    "        var classes = value.trim().split(/\\s+/).join('.');\n" +
                    "        nodeList = root.querySelectorAll('.' + classes);\n" +
                    "        break;\n" +
                    "      case 'css':\n" +
                    "        nodeList = root.querySelectorAll(value);\n" +
                    "        break;\n" +
                    "      case 'tagName':\n" +
                    "        nodeList = root.querySelectorAll(value);\n" +
                    "        break;\n" +
                    "      case 'xpath':\n" +
                    "        let xres = document.evaluate(value, root, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);\n" +
                    "        for (let i = 0; i < xres.snapshotLength; i++) {\n" +
                    "          nodeList.push(xres.snapshotItem(i));\n" +
                    "        }\n" +
                    "        break;\n" +
                    "      case 'linkText':\n" +
                    "        let allAnchors = root.querySelectorAll('a');\n" +
                    "        allAnchors.forEach(a => { if (a.textContent === value) nodeList.push(a); });\n" +
                    "        break;\n" +
                    "      case 'partialLinkText':\n" +
                    "        let anchors2 = root.querySelectorAll('a');\n" +
                    "        anchors2.forEach(a => { if (a.textContent.includes(value)) nodeList.push(a); });\n" +
                    "        break;\n" +
                    "    }\n" +
                    "    return Array.from(nodeList);\n" +
                    "  }\n" +
                    "\n" +
                    "  function collectShadow(rootNodes, type, value, results) {\n" +
                    "    for (var i = 0; i < rootNodes.length; i++) {\n" +
                    "      var node = rootNodes[i];\n" +
                    "      if (node && node.shadowRoot) {\n" +
                    "        results.push(...collectInRoot(node.shadowRoot, type, value));\n" +
                    "        collectShadow(node.shadowRoot.querySelectorAll('*'), type, value, results);\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }\n" +
                    "\n" +
                    "  function doSearch() {\n" +
                    "    let found = [];\n" +
                    "    // Check rootElem's shadow root\n" +
                    "    if (rootElem.shadowRoot) {\n" +
                    "      found.push(...collectInRoot(rootElem.shadowRoot, type, value));\n" +
                    "      collectShadow(rootElem.shadowRoot.querySelectorAll('*'), type, value, found);\n" +
                    "    }\n" +
                    "    // Also treat rootElem as a normal DOM container\n" +
                    "    found.push(...collectInRoot(rootElem, type, value));\n" +
                    "    collectShadow(rootElem.querySelectorAll('*'), type, value, found);\n" +
                    "    return found;\n" +
                    "  }\n" +
                    "\n" +
                    "  const startTime = performance.now();\n" +
                    "  while ((performance.now() - startTime) < maxWait) {\n" +
                    "    let result = doSearch();\n" +
                    "    if (result.length > 0) {\n" +
                    "      return result;\n" +
                    "    }\n" +
                    "    var now = performance.now();\n" +
                    "    while (performance.now() - now < pollInterval) {\n" +
                    "      // busy wait\n" +
                    "    }\n" +
                    "  }\n" +
                    "  return [];\n" +
                    "}\n" +
                    "return findShadowElementsFromRoot(arguments[0], arguments[1]);";

    /**
     * Finds all matching elements within a given Shadow DOM root.
     * Waits up to the configured maximum wait time, polling every {@code DEFAULT_POLL_INTERVAL_MS}.
     *
     * @param root The root {@link SmartWebElement} to start the search from.
     * @param by   The {@link By} locator to use for finding the elements.
     * @return A list of {@link SmartWebElement} containing all matching elements, or an empty list if no elements are found.
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
        selector.put("maxWait", wait);
        selector.put("pollInterval", DEFAULT_POLL_INTERVAL_MS);

        Object result = js.executeScript(FIND_SHADOW_ELEMENTS_FROM_ELEMENT_JS, root.getOriginal(), selector);
        if (result instanceof java.util.List) {
            return ((java.util.List<WebElement>) result).stream()
                    .map(webElement -> new SmartWebElement(webElement, driver)).toList();
        }
        return java.util.Collections.emptyList();
    }

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
        Boolean result = (Boolean) js.executeScript(script);
        return result != null && result;
    }


    /**
     * Checks if there is at least one element that has a Shadow Root starting from a given WebElement.
     *
     * @param root The root {@link SmartWebElement} to check.
     * @return True if the root element or any descendant has a Shadow Root, otherwise false.
     */
    public static boolean shadowRootElementsPresent(SmartWebElement root) {
        if (root == null) {
            return false;
        }
        WebDriver driver = root.getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Check if the root itself has a shadowRoot or any of its descendants
        String script =
                "if (arguments[0].shadowRoot) { " +
                        "  return true; " +
                        "} else { " +
                        "  return Array.from(arguments[0].querySelectorAll('*')).some(el => el.shadowRoot);" +
                        "}";
        Boolean result = (Boolean) js.executeScript(script, root.getOriginal());
        return result != null && result;
    }

}