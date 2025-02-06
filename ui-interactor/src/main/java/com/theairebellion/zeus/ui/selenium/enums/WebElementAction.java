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
        public Object performAction(WebDriver driver, WebElement element, Object... args) {
            if (args.length == 0 || !(args[0] instanceof By)) {
                throw new IllegalArgumentException("FIND_ELEMENT requires a By locator.");
            }
            return new SmartWebElement(element.findElement((By) args[0]), driver);
        }
    },

    FIND_ELEMENTS("findElements") {
        @Override
        public Object performAction(WebDriver driver, WebElement element, Object... args) {
            return element.findElements((By) args[0]).stream()
                    .map(e -> new SmartWebElement(e, driver))
                    .collect(Collectors.toList());
        }
    },

    CLICK("click") {
        @Override
        public Object performAction(WebDriver driver, WebElement element, Object... args) {
            element.click();
            return null;
        }
    },

    SEND_KEYS("sendKeys") {
        @Override
        public Object performAction(WebDriver driver, WebElement element, Object... args) {
            element.sendKeys((String) args[0]);
            return null;
        }
    },

    SUBMIT("submit") {
        @Override
        public Object performAction(WebDriver driver, WebElement element, Object... args) {
            element.submit();
            return null;
        }
    };

    public abstract Object performAction(WebDriver driver, WebElement element, Object... args);

    private final String methodName;

    WebElementAction(String methodName) {
        this.methodName = methodName;
    }
}
