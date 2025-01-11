package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import com.theairebellion.zeus.ui.extensions.UiTestExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(UiTestExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@FrameworkAdapter(basePackages = {"com.theairebellion.zeus.ui"})
public @interface UI {

}
