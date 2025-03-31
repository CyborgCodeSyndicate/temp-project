package com.reqres.test.framework;

import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.*;
import static com.reqres.test.framework.rest.Endpoints.GET_ALL_RESOURCES;
import static com.reqres.test.framework.rest.Endpoints.GET_RESOURCE;
import static com.reqres.test.framework.utils.Helpers.EMPTY_JSON;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.*;

@API
public class ResourcesAIGeneratedTest extends BaseTest {

    @Test
    public void testGetAllResourcesPage1(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_RESOURCES.withQueryParam("page", 1),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_ID_BY_INDEX.getJsonPath(0)).type(IS).expected(1).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_NAME_BY_INDEX.getJsonPath(0)).type(IS).expected("cerulean").build(),
                        Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(CONTAINS).expected("Tired of writing endless social media content?").build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build()
                )
                .complete();
    }

    @Test
    public void testGetAllResourcesPage2(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_RESOURCES.withQueryParam("page", 2),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_PAGE.getJsonPath()).type(IS).expected(2).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_NAME_BY_INDEX.getJsonPath(5)).type(IS).expected("honeysuckle").build(),
                        Assertion.builder().target(BODY).key(RESOURCE_COLOR_BY_INDEX.getJsonPath(5)).type(IS).expected("#D94F70").build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build()
                )
                .complete();
    }

    @Test
    public void testGetAllResourcesPage3OrMoreReturnsEmptyData(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_RESOURCES.withQueryParam("page", 3),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_PAGE.getJsonPath()).type(IS).expected(3).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(EMPTY).expected(true).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(CONTAINS).expected("Content Caddy").build()
                )
                .complete();
    }

    @Test
    public void testGetResourceById2_Positive(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_RESOURCE.withPathParam("id", 2),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_ID.getJsonPath()).type(IS).expected(2).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_NAME.getJsonPath()).type(IS).expected("fuchsia rose").build(),
                        Assertion.builder().target(BODY).key(RESOURCE_YEAR.getJsonPath()).type(IS).expected(2001).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_COLOR.getJsonPath()).type(IS).expected("#C74375").build(),
                        Assertion.builder().target(BODY).key(RESOURCE_PANTONE.getJsonPath()).type(IS).expected("17-2031").build()
                )
                .complete();
    }

    @Test
    public void testGetResourceById23_NotFound(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_RESOURCE.withPathParam("id", 23),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NOT_FOUND).build(),
                        Assertion.builder().target(BODY).key("$").type(IS).expected(EMPTY_JSON).build()
                )
                .complete();
    }

}
