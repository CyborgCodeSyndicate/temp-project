package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import org.openqa.selenium.support.FindBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InsertionField {

    FindBy locator();

    Class<? extends ComponentType> type();

    String componentType();

    int order();

}
