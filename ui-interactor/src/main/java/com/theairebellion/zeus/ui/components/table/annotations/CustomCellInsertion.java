package com.theairebellion.zeus.ui.components.table.annotations;

import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomCellInsertion {

    Class<? extends CellInsertionFunction> insertionFunction();

    int order() default 0;

}
