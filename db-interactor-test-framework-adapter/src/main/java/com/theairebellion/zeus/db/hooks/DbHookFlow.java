package com.theairebellion.zeus.db.hooks;

import com.theairebellion.zeus.db.service.DatabaseService;
import java.util.Map;
import org.apache.logging.log4j.util.TriConsumer;

public interface DbHookFlow<T extends Enum<T>> {

   TriConsumer<DatabaseService, Map<Object, Object>, String[]> flow();

   T enumImpl();

}
