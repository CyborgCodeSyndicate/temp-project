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
import static com.reqres.test.framework.utils.PathVariables.ID_PARAM;
import static com.reqres.test.framework.utils.QueryParams.PAGE_PARAM;
import static com.reqres.test.framework.utils.TestConstants.Pagination.*;
import static com.reqres.test.framework.utils.TestConstants.Resources.*;
import static com.reqres.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_BRAND;
import static com.reqres.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_FULL;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

@API
public class ResourcesAIGeneratedTest extends BaseTest {

    @Test
    public void testGetAllResourcesPage1(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_RESOURCES.withQueryParam(PAGE_PARAM, PAGE_ONE),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_ID_BY_INDEX.getJsonPath(0)).type(IS).expected(RESOURCE_ONE_ID).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_NAME_BY_INDEX.getJsonPath(0)).type(IS).expected(RESOURCE_ONE_NAME).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(CONTAINS).expected(SUPPORT_TEXT_FULL).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build()
                )
                .complete();
    }

    @Test
    public void testGetAllResourcesPage2(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_RESOURCES.withQueryParam(PAGE_PARAM, PAGE_TWO),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_PAGE.getJsonPath()).type(IS).expected(PAGE_TWO).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_NAME_BY_INDEX.getJsonPath(5)).type(IS).expected(RESOURCE_SIX_NAME).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_COLOR_BY_INDEX.getJsonPath(5)).type(IS).expected(RESOURCE_SIX_COLOR).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build()
                )
                .complete();
    }

    @Test
    public void testGetAllResourcesPage3OrMoreReturnsEmptyData(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_RESOURCES.withQueryParam(PAGE_PARAM, PAGE_THREE),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_PAGE.getJsonPath()).type(IS).expected(PAGE_THREE).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(EMPTY).expected(true).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(CONTAINS).expected(SUPPORT_TEXT_BRAND).build()
                )
                .complete();
    }

    @Test
    public void testGetResourceById2_Positive(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_RESOURCE.withPathParam(ID_PARAM, RESOURCE_TWO_ID),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_ID.getJsonPath()).type(IS).expected(RESOURCE_TWO_ID).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_NAME.getJsonPath()).type(IS).expected(RESOURCE_TWO_NAME).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_YEAR.getJsonPath()).type(IS).expected(RESOURCE_TWO_YEAR).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_COLOR.getJsonPath()).type(IS).expected(RESOURCE_TWO_COLOR).build(),
                        Assertion.builder().target(BODY).key(RESOURCE_PANTONE.getJsonPath()).type(IS).expected(RESOURCE_TWO_PANTONE).build()
                )
                .complete();
    }

    @Test
    public void testGetResourceById23_NotFound(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_RESOURCE.withPathParam(ID_PARAM, RESOURCE_INVALID_ID),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NOT_FOUND).build(),
                        Assertion.builder().target(BODY).key(ROOT.getJsonPath()).type(IS).expected(EMPTY_JSON).build()
                )
                .complete();
    }

}