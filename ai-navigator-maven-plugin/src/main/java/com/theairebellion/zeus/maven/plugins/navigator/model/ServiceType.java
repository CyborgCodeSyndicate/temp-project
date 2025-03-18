package com.theairebellion.zeus.maven.plugins.navigator.model;

import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.db.service.fluent.DatabaseServiceFluent;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;

public enum ServiceType {

    REST(RestServiceFluent.class),
    UI(UIServiceFluent.class),
    DATABASE(DatabaseServiceFluent.class);

    private final Class<? extends FluentService> serviceClass;


    ServiceType(Class<? extends FluentService> serviceClass) {
        this.serviceClass = serviceClass;
    }


    public Class<? extends FluentService> getServiceClass() {
        return serviceClass;
    }

}
