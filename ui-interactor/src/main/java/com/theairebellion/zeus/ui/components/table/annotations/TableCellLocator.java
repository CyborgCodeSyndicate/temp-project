package com.theairebellion.zeus.ui.components.table.annotations;

import org.openqa.selenium.support.FindBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableCellLocator {


    FindBy cellLocator();

    String tableSection() default "";

    FindBy cellTextLocator() default @FindBy(xpath = ".");

    FindBy headerCellLocator() default @FindBy(xpath = ".");


}
