package com.example.project;


import com.example.project.base.World;
import com.example.project.ui.elements.InputFields;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.UI;
import com.theairebellion.zeus.ui.storage.DataExtractorsUi;
import org.junit.jupiter.api.Test;

@UI
public class NewUITest extends BaseTest {


    @Test
    //@AuthenticateAs(credentials = AdminAuth.class, type = PortalAuthentication.class)
    //@InterceptRequests(requestUrlSubStrings = {"api/create-campaign", "upload"})
    public void scenario_some(Quest quest) {
        quest
            .enters(World.EARTH)
                .navigate("http://zero.webappsecurity.com/")
            .input().insert(InputFields.PASSWORD_FIELD,
                String.valueOf(
                    retrieve(DataExtractorsUi.responseBodyExtraction("api/create-campaign", "$.id"), Long.class)))
            .then()
            .enters(World.FORGE)
            .complete();
    }

}
