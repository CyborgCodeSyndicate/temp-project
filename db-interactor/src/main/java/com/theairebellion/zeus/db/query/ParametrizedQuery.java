package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.log.LogDb;

import java.util.HashMap;
import java.util.Map;

public class ParametrizedQuery implements DbQuery {

    private final DbQuery original;
    private final Map<String, Object> params = new HashMap<>();

    public ParametrizedQuery(DbQuery original) {
        this.original = original;
    }

    @Override
    public String query() {
        String q = original.query();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            q = q.replace(placeholder, entry.getValue() != null ? entry.getValue().toString() : "null");
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
        LogDb.debug("Adding parameter '{}' with value '{}' to the query.", name, value);
        ParametrizedQuery copy = new ParametrizedQuery(this.original);
        copy.params.putAll(this.params);
        copy.params.put(name, value);
        return copy;
    }

}