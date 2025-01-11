package com.theairebellion.zeus.api.annotations;


import com.theairebellion.zeus.api.extensions.ApiTestExtension;
import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(ApiTestExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@FrameworkAdapter(basePackages = {"com.theairebellion.zeus.api"})
public @interface API {

}
