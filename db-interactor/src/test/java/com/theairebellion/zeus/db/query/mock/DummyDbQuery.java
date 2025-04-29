package com.theairebellion.zeus.db.query.mock;

import com.theairebellion.zeus.db.query.DbQuery;

public class DummyDbQuery implements DbQuery<TestEnum> {
    @Override
    public String query() {
        return "SELECT * FROM dummy";
    }

    @Override
    public TestEnum enumImpl() {
        return TestEnum.VALUE;
    }
}