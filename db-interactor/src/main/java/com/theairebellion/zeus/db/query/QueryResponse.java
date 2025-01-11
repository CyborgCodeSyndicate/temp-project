package com.theairebellion.zeus.db.query;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class QueryResponse {

    private List<Map<String, Object>> rows;

}
