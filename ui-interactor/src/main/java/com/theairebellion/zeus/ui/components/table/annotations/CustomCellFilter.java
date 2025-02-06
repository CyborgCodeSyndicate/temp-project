package com.theairebellion.zeus.ui.components.table.annotations;

import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomCellFilter {

    Class<? extends CellFilterFunction> cellFilterFunction();

}
