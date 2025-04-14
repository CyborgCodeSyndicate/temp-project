package com.theairebellion.zeus.db.query.mock;

import com.theairebellion.zeus.db.query.DbQuery;

public class DummyDbQuery implements DbQuery {
   @Override
   public String query() {
      return "SELECT * FROM dummy";
   }

   @Override
   public Enum<?> enumImpl() {
      return TestEnum.VALUE;
   }
}