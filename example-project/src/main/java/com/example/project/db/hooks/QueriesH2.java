package com.example.project.db.hooks;

import com.example.project.db.MyDatabases;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.query.DbQuery;

public enum QueriesH2 implements DbQuery {
    QUERY("fdsfsd");

    private final String query;


    QueriesH2(final String query) {
        this.query = query;
    }


    @Override
    public String query() {
        return query;
    }


    @Override
    public DatabaseConfiguration config() {
        DatabaseConfiguration.builder().dbType(MyDatabases.H2);
        //todo override this to use from config for h2
        return null;
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }
}
