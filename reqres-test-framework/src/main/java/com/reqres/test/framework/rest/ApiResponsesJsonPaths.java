package com.reqres.test.framework.rest;

public enum ApiResponsesJsonPaths {

    TOTAL("total"),
    TOTAL_PAGES("total_pages"),
    PER_PAGE("per_page"),
    SUPPORT_URL("support.url"),
    SUPPORT_TEXT("support.text"),
    DATA("data"),
    USER_ID("data[%d].id"),
    USER_FIRST_NAME("data[%d].first_name"),
    USER_AVATAR_BY_INDEX("data[%d].avatar"),
    CREATE_USER_NAME("name"),
    CREATE_USER_JOB("job"),
    RESOURCE_ID_BY_INDEX("data[%d].id"),
    RESOURCE_NAME_BY_INDEX("data[%d].name"),
    RESOURCE_COLOR_BY_INDEX("data[%d].color"),
    RESOURCE_PAGE("page"),
    RESOURCE_ID("data.id"),
    RESOURCE_NAME("data.name"),
    RESOURCE_YEAR("data.year"),
    RESOURCE_COLOR("data.color"),
    RESOURCE_PANTONE("data.pantone_value");

    private final String jsonPath;

    ApiResponsesJsonPaths(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    /**
     * Returns the JSON path.
     * <p>
     * If the path contains a placeholder (like %d), you can supply the arguments
     * (e.g., an index) to format the path correctly.
     * </p>
     *
     * @param args optional arguments to format the JSON path
     * @return the formatted JSON path as a String
     */
    public String getJsonPath(Object... args) {
        if (args != null && args.length > 0) {
            return String.format(jsonPath, args);
        }
        return jsonPath;
    }

}
