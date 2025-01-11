package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.ui.selenium.UIElement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InsertionElement {

    Class<? extends UIElement> locatorClass();

    String elementEnum();

    int order();

}
