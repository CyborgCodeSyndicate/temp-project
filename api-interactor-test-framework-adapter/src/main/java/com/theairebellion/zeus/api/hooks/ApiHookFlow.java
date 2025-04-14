package com.theairebellion.zeus.api.hooks;

import com.theairebellion.zeus.api.service.RestService;
import java.util.Map;
import org.apache.logging.log4j.util.TriConsumer;

public interface ApiHookFlow {

   TriConsumer<RestService, Map<Object, Object>, String[]> flow();

   Enum<?> enumImpl();


}
