package com.zerobank.project.base;

import com.theairebellion.zeus.db.service.fluent.DatabaseServiceFluent;
import com.zerobank.project.ui.UiServiceCustom;

public class World {

   public static final Class<DatabaseServiceFluent> UNDERWORLD = DatabaseServiceFluent.class;
   public static final Class<UiServiceCustom> EARTH = UiServiceCustom.class;
}
