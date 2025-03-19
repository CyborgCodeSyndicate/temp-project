package com.theairebellion.zeus.api.hooks;

import com.theairebellion.zeus.api.service.RestService;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Map;

public interface ApiHookFlow {

    TriConsumer<RestService, Map<Object, Object>, String[]> flow();

    Enum<?> enumImpl();


}
