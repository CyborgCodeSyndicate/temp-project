package com.theairebellion.zeus.ui.selenium.decorators;

import lombok.Getter;
import lombok.experimental.Delegate;
import org.openqa.selenium.WebElement;

@Getter
public abstract class WebElementDecorator implements WebElement {

    @Delegate
    protected final WebElement original;

    public WebElementDecorator(WebElement original) {
        this.original = original;
    }


}
