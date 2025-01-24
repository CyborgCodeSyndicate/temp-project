package com.example.project;


import com.example.project.base.World;
import com.example.project.ui.elements.bootstrap.ButtonFields;
import com.example.project.ui.elements.bootstrap.InputFields;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.UI;
import org.junit.jupiter.api.Test;

@UI
public class ZeroBankTest extends BaseTest {


    @Test
    public void firstTest(Quest quest) {
        quest
            .enters(World.EARTH)
                .navigate("http://zero.webappsecurity.com/")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .input().insert(InputFields.USERNAME_FIELD, "username")
                .input().insert(InputFields.PASSWORD_FIELD, "password")
                .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
                .navigate("http://zero.webappsecurity.com/")
            .complete();
    }

}
