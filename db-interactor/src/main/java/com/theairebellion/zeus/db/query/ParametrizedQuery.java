package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ParametrizedQuery implements DbQuery {

    private final DbQuery original;
    private final Map<String, Object> params = new HashMap<>();


    ParametrizedQuery(DbQuery original) {
        this.original = original;
    }


    @Override
    public String query() {
        String q = original.query();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q = q.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return q;
    }


    @Override
    public DatabaseConfiguration config() {
        return original.config();
    }


    @Override
    public Enum<?> enumImpl() {
        return original.enumImpl();
    }


    @Override
    public DbQuery withParam(String name, Object value) {
        ParametrizedQuery copy = new ParametrizedQuery(this.original);
        copy.params.putAll(this.params);
        copy.params.put(name, value);
        return copy;
    }

}