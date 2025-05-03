package com.reqres.test.framework.rest;

public enum ApiResponsesJsonPaths {

    // --- General / Root Keys ---
    ROOT("$"),
    TOTAL("total"),
    TOTAL_PAGES("total_pages"),
    PER_PAGE("per_page"),
    PAGE("page"),

    // --- Support Information ---
    SUPPORT_URL("support.url"),
    SUPPORT_TEXT("support.text"),
    SUPPORT_URL_EXPLICIT("support.url"),

    // --- Data Container ---
    DATA("data"),

    // --- User List (Indexed Fields) ---
    USER_EMAIL_BY_INDEX("data[%d].email"),
    USER_ID("data[%d].id"),
    USER_FIRST_NAME("data[%d].first_name"),
    USER_AVATAR_BY_INDEX("data[%d].avatar"),

    // --- Single User (Non-Indexed Fields) ---
    SINGLE_USER_FIRST_NAME("data.first_name"),
    SINGLE_USER_EMAIL("data.email"),
    SINGLE_USER_EMAIL_EXPLICIT("data.email"),
    SINGLE_USER_AVATAR("data.avatar"),

    // --- Create User Response Fields ---
    CREATE_USER_NAME("name"),
    CREATE_USER_JOB("job"),
    CREATED_USER_ID("id"),
    CREATED_USER_TIMESTAMP("createdAt"),
    CREATE_USER_NAME_RESPONSE("name"),
    CREATE_USER_JOB_RESPONSE("job"),

    // --- Resource List (Indexed Fields) ---
    RESOURCE_ID_BY_INDEX("data[%d].id"),
    RESOURCE_NAME_BY_INDEX("data[%d].name"),
    RESOURCE_COLOR_BY_INDEX("data[%d].color"),

    // --- Single Resource (Non-Indexed Fields) ---
    RESOURCE_ID("data.id"),
    RESOURCE_NAME("data.name"),
    RESOURCE_YEAR("data.year"),
    RESOURCE_COLOR("data.color"),
    RESOURCE_PANTONE("data.pantone_value"),
    RESOURCE_PAGE("page"),

    // --- Miscellaneous ---
    TOKEN("token"),
    ERROR("error");

    private final String jsonPath;

    ApiResponsesJsonPaths(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    /**
     * Returns the JSON path.
     * If the path contains placeholders (e.g., %d, %s), supply the arguments to format the string.
     *
     * @param args Optional arguments for formatting the JSON path.
     * @return The formatted JSON path.
     */
    public String getJsonPath(Object... args) {
        if (args != null && args.length > 0) {
            return String.format(jsonPath, args);
        }
        return jsonPath;
    }

}
