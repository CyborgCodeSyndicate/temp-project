package com.example.project.extensions.com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;

import com.theairebellion.zeus.ui.enums.Features;
import com.theairebellion.zeus.ui.service.fluent.*;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

@Extension
public class UIComponentsInUse {


    public static InputServiceFluent input(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.INPUT_FIELDS.getFieldName(), uiServiceFluent,
                InputServiceFluent.class);
    }


    public static ButtonServiceFluent button(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.BUTTON_FIELDS.getFieldName(), uiServiceFluent,
                ButtonServiceFluent.class);
    }


    public static RadioServiceFluent radio(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.RADIO_FIELDS.getFieldName(), uiServiceFluent,
                RadioServiceFluent.class);
    }


    public static SelectServiceFluent select(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.SELECT_FIELDS.getFieldName(), uiServiceFluent,
                SelectServiceFluent.class);
    }


    public static ItemListServiceFluent list(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.LIST_FIELDS.getFieldName(), uiServiceFluent,
                ItemListServiceFluent.class);
    }


    public static LoaderServiceFluent loader(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.LOADER_FIELDS.getFieldName(), uiServiceFluent,
                LoaderServiceFluent.class);
    }


    public static LinkServiceFluent link(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.LINK_FIELDS.getFieldName(), uiServiceFluent,
                LinkServiceFluent.class);
    }


    public static AlertServiceFluent alert(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.ALERT_FIELDS.getFieldName(), uiServiceFluent,
                AlertServiceFluent.class);
    }


    public static TabServiceFluent tab(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.TAB_FIELDS.getFieldName(), uiServiceFluent,
                TabServiceFluent.class);
    }


    public static InterceptorServiceFluent interceptor(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.REQUESTS_INTERCEPTOR.getFieldName(), uiServiceFluent,
                InterceptorServiceFluent.class);
    }


    public static InsertionServiceFluent insertion(@This UIServiceFluent uiServiceFluent) {
        return ReflectionUtil.getAttributeOfClass(Features.DATA_INSERTION.getFieldName(), uiServiceFluent,
                InsertionServiceFluent.class);
    }
//
//    public static ValidationServiceFluent validation(@This UIServiceFluent uiServiceFluent) {
//        return ReflectionUtil.getAttributeOfClass(Features.VALIDATION.getFieldName(), uiServiceFluent,
//                ValidationServiceFluent.class);
//    }


}
