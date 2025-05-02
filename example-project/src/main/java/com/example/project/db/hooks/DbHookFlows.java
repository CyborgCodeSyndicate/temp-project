package com.example.project.db.hooks;

import com.theairebellion.zeus.db.hooks.DbHookFlow;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.DatabaseService;
import java.util.Map;
import org.apache.logging.log4j.util.TriConsumer;

import static com.example.project.db.Queries.QUERY_SELLER;
import static com.example.project.db.hooks.QueriesH2.CREATE_TABLE_ORDERS;
import static com.example.project.db.hooks.QueriesH2.CREATE_TABLE_SELLERS;
import static com.example.project.db.hooks.QueriesH2.INSERT_ORDERS;
import static com.example.project.db.hooks.QueriesH2.INSERT_SELLERS;

public enum DbHookFlows implements DbHookFlow<DbHookFlows> {

   INITIALIZE_H2(DbHookFlows::initializeH2),
   QUERY_SAVE_IN_STORAGE_H2(DbHookFlows::getFromDbSaveInStorage);


   public static final class Data {

      public static final String INITIALIZE_H2 = "INITIALIZE_H2";
      public static final String QUERY_SAVE_IN_STORAGE_H2 = "QUERY_SAVE_IN_STORAGE_H2";

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
   public DbHookFlows enumImpl() {
      return this;
   }


   public static void initializeH2(DatabaseService service, Map<Object, Object> storage, String[] arguments) {
      service.query(CREATE_TABLE_ORDERS);
      service.query(CREATE_TABLE_SELLERS);
      service.query(INSERT_ORDERS);
      service.query(INSERT_SELLERS);
   }


   public static void getFromDbSaveInStorage(DatabaseService service, Map<Object, Object> storage, String[] arguments) {
      String id = arguments[0];
      QueryResponse dasdas = service.query(QUERY_SELLER.withParam("dasdas", id));
      storage.put("Something", dasdas);
   }

}
