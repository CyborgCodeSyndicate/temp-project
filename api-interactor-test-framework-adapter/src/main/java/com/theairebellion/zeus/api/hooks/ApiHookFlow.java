package com.theairebellion.zeus.api.hooks;

import com.theairebellion.zeus.api.service.RestService;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Map;

@SuppressWarnings("java:S1452")
public interface ApiHookFlow<T extends Enum<T>> {

    TriConsumer<RestService, Map<Object, Object>, String[]> flow();

    T enumImpl();


}
