package com.theairebellion.zeus.ui.components.table.annotations;

import com.theairebellion.zeus.ui.components.base.ComponentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellFilter {


    Class<? extends ComponentType> type();

    String componentType();

}
