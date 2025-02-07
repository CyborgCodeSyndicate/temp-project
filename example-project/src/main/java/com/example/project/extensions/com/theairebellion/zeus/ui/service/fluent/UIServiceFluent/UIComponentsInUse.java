package com.example.project.extensions.com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;

import com.theairebellion.zeus.ui.enums.Features;
import com.theairebellion.zeus.ui.service.fluent.InputServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.InsertionServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.InterceptorServiceFluent;
import com.theairebellion.zeus.ui.service.tables.TableServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

@Extension
public class UIComponentsInUse {


    public static InputServiceFluent input(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.INPUT_FIELDS.getFieldName(), uiServiceFluent,
            InputServiceFluent.class);
    }


    public static TableServiceFluent table(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.TABLE.getFieldName(), uiServiceFluent,
            TableServiceFluent.class);
    }


    public static InterceptorServiceFluent interceptor(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.REQUESTS_INTERCEPTOR.getFieldName(), uiServiceFluent,
            InterceptorServiceFluent.class);
    }


    public static InsertionServiceFluent insertion(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.DATA_INSERTION.getFieldName(), uiServiceFluent,
            InsertionServiceFluent.class);
    }


}
