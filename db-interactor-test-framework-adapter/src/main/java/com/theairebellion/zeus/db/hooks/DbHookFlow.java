package com.theairebellion.zeus.db.hooks;

import com.theairebellion.zeus.db.service.DatabaseService;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Map;

public interface DbHookFlow {

    TriConsumer<DatabaseService, Map<Object, Object>, String[]> flow();

    Enum<?> enumImpl();


}
