package com.example.project.db.hooks;

import com.theairebellion.zeus.db.hooks.DbHookFlow;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.DatabaseService;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Map;

public enum DbHookFlows implements DbHookFlow {

    INITIALIZE_H2(DbHookFlows::doWithDbSomething);


    public static final class Data {

        public static final String INITIALIZE_H2 = "INITIALIZE_H2";


        private Data() {
        }

    }

    private final TriConsumer<DatabaseService, Map<Object, Object>, String[]> flow;


    DbHookFlows(final TriConsumer<DatabaseService, Map<Object, Object>, String[]> flow) {
        this.flow = flow;
    }


    @Override
    public TriConsumer<DatabaseService, Map<Object, Object>, String[]> flow() {
        return flow;
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }


    public static void doWithDbSomething(DatabaseService service, Map<Object, Object> storage, String[] arguments) {
        String id = arguments[0];
        QueryResponse dasdas = service.query(QueriesH2.QUERY.withParam("dasdas", id));
        storage.put("Cvetko", dasdas);
    }
}
