package com.theairebellion.zeus.ui.selenium.decorators;

import lombok.Getter;
import lombok.experimental.Delegate;
import org.openqa.selenium.WebDriver;

@Getter
public abstract class WebDriverDecorator implements WebDriver {

    @Delegate
    protected final WebDriver original;

    public WebDriverDecorator(WebDriver original) {
        this.original = original;
    }
}
