package com.reqres.test.framework.base;

import com.reqres.test.framework.service.CustomService;
import com.reqres.test.framework.service.EvolutionService;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import lombok.experimental.UtilityClass;

@UtilityClass
public class World {

    public static final Class<RestServiceFluent> OLYMPYS = RestServiceFluent.class;
    public static final Class<CustomService> RIVENDELL = CustomService.class;
    public static final Class<EvolutionService> GONDOR = EvolutionService.class;

}
