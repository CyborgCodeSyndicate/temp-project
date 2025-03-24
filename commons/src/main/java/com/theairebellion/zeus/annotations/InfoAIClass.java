package com.theairebellion.zeus.annotations;

import com.theairebellion.zeus.ai.metadata.model.classes.CreationType;
import com.theairebellion.zeus.ai.metadata.model.classes.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface InfoAIClass {

    Level level() default Level.NA;

    String description();

    CreationType creationType() default CreationType.NOT_SPECIFIED;

    boolean useAsKeyInStorage() default false;


}
