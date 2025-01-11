package com.theairebellion.zeus.db.config;

public interface DbType {


    java.sql.Driver driver();

    String protocol();

    Enum<?> enumImpl();


}
