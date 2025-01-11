package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.extension.Craftsman;
import com.theairebellion.zeus.framework.extension.Epilogue;
import com.theairebellion.zeus.framework.extension.Initiator;
import com.theairebellion.zeus.framework.extension.Oracle;
import com.theairebellion.zeus.framework.extension.Prologue;
import com.theairebellion.zeus.framework.extension.RipperMan;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith({Oracle.class, Prologue.class, Epilogue.class, Craftsman.class, RipperMan.class, Initiator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Odyssey {

}
