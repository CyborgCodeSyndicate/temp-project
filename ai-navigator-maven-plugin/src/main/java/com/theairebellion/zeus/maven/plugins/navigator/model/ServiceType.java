package com.theairebellion.zeus.maven.plugins.navigator.model;

import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.db.annotations.DB;
import com.theairebellion.zeus.db.service.fluent.DatabaseServiceFluent;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.annotation.Ripper;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.annotations.UI;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public enum ServiceType {

    REST(RestServiceFluent.class, List.of(API.class, AuthenticateViaApiAs.class)),
    UI(UIServiceFluent.class, List.of(UI.class, InterceptRequests.class, AuthenticateViaUiAs.class)),
    DATABASE(DatabaseServiceFluent.class, List.of(DB.class));

    @Getter
    private final Class<? extends FluentService> serviceClass;
    private final List<Class<? extends Annotation>> annotationClasses;


    ServiceType(Class<? extends FluentService> serviceClass,
                final List<Class<? extends Annotation>> annotationClasses) {
        this.serviceClass = serviceClass;
        this.annotationClasses = annotationClasses;
    }


    public List<Class<? extends Annotation>> getAnnotationClasses() {
        List<Class<? extends Annotation>> annotations = new ArrayList<>();
        annotations.addAll(annotationClasses);
        annotations.addAll(List.of(PreQuest.class, Ripper.class, Craft.class));
        return annotations;
    }
}
