package com.example.project.db;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.query.DbQuery;

public enum Queries implements DbQuery {

    EXAMPLE("Select * from table where id = '{id}' and company = '{company}')");

    private final String query;


    Queries(final String query) {
        this.query = query;
    }


    @Override
    public String query() {
        return query;
    }


    @Override
    public DatabaseConfiguration config() {
        return DbQuery.super.config();
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }


}
