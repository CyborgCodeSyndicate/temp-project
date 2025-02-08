package com.theairebellion.zeus.ui.selenium.enums;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.stream.Collectors;

@Getter
public enum WebElementAction {

    FIND_ELEMENT("findElement") {
        @Override
        public Object performActionWebElement(WebDriver driver, WebElement element, Object... args) {
            if (args.length == 0 || !(args[0] instanceof By)) {
                throw new IllegalArgumentException("FIND_ELEMENT requires a By locator.");
            }
            return new SmartWebElement(element.findElement((By) args[0]), driver);
        }

        @Override
        public Object performActionWebDriver(WebDriver driver, Object... args) {
            if (args.length == 0 || !(args[0] instanceof By)) {
                throw new IllegalArgumentException("FIND_ELEMENT requires a By locator.");
            }
            return new SmartWebElement(driver.findElement((By) args[0]), driver);
        }
    },

    FIND_ELEMENTS("findElements") {
        @Override
        public Object performActionWebElement(WebDriver driver, WebElement element, Object... args) {
            return element.findElements((By) args[0]).stream()
                    .map(e -> new SmartWebElement(e, driver))
                    .collect(Collectors.toList());
        }

        @Override
        public Object performActionWebDriver(WebDriver driver, Object... args) {
            return driver.findElements((By) args[0]).stream()
                    .map(e -> new SmartWebElement(e, driver))
                    .collect(Collectors.toList());
        }
    },

    CLICK("click") {
        @Override
        public Void performActionWebElement(WebDriver driver, WebElement element, Object... args) {
            element.click();
            return null;
        }

        @Override
        public Object performActionWebDriver(WebDriver driver, Object... args) {
            return null;
        }
    },

    SEND_KEYS("sendKeys") {
        @Override
        public Void performActionWebElement(WebDriver driver, WebElement element, Object... args) {
            element.sendKeys((String) args[0]);
            return null;
        }

        @Override
        public Object performActionWebDriver(WebDriver driver, Object... args) {
            return null;
        }
    },

    SUBMIT("submit") {
        @Override
        public Void performActionWebElement(WebDriver driver, WebElement element, Object... args) {
            element.submit();
            return null;
        }

        @Override
        public Object performActionWebDriver(WebDriver driver, Object... args) {
            return null;
        }
    },

    CLEAR("clear") {
        @Override
        public Void performActionWebElement(WebDriver driver, WebElement element, Object... args) {
            element.clear();
            return null;
        }

        @Override
        public Object performActionWebDriver(WebDriver driver, Object... args) {
            return null;
        }
    };

    public abstract Object performActionWebElement(WebDriver driver, WebElement element, Object... args);
    public abstract Object performActionWebDriver(WebDriver driver, Object... args);

    private final String methodName;

    WebElementAction(String methodName) {
        this.methodName = methodName;
    }
}
