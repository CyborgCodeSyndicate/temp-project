package com.theairebellion.zeus.api.hooks;

import com.theairebellion.zeus.api.service.RestService;
import java.util.Map;
import org.apache.logging.log4j.util.TriConsumer;

@SuppressWarnings("java:S1452")
public interface ApiHookFlow<T extends Enum<T>> {

   TriConsumer<RestService, Map<Object, Object>, String[]> flow();

   T enumImpl();

}
