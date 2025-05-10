package com.bakery.project.base;

import com.bakery.project.service.CustomService;
import com.bakery.project.ui.UiServiceCustom;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.db.service.fluent.DatabaseServiceFluent;

public class World {

   public static final Class<RestServiceFluent> OLYMPYS = RestServiceFluent.class;
   public static final Class<DatabaseServiceFluent> UNDERWORLD = DatabaseServiceFluent.class;
   public static final Class<UiServiceCustom> EARTH = UiServiceCustom.class;
   public static final Class<CustomService> FORGE = CustomService.class;

}
