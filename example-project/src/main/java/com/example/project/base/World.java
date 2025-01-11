package com.example.project.base;

import com.example.project.service.CustomService;
import com.theairebellion.zeus.db.service.fluent.DatabaseServiceFluent;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;

public class World {

    public static final Class<RestServiceFluent> OLYMPYS = RestServiceFluent.class;
    public static final Class<DatabaseServiceFluent> UNDERWORLD = DatabaseServiceFluent.class;
    public static final Class<UIServiceFluent> EARTH = UIServiceFluent.class;
    public static final Class<CustomService> FORGE = CustomService.class;

}
