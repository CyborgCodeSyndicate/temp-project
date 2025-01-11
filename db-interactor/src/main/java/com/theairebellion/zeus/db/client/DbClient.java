package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.query.QueryResponse;

public interface DbClient {

    QueryResponse executeQuery(String query);

}
