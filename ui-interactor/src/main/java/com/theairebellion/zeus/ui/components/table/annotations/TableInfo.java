package com.theairebellion.zeus.ui.components.table.annotations;

import org.openqa.selenium.support.FindBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableInfo {

    FindBy tableContainerLocator();

    FindBy rowsLocator();

    FindBy headerRowLocator();

}
