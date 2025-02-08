package com.example.project;


import com.example.project.base.World;
import com.example.project.ui.elements.Bakery.InputFields;
import com.example.project.ui.elements.Bakery.ButtonFields;
import com.example.project.ui.elements.Bakery.SelectFields;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.UI;
import org.junit.jupiter.api.Test;

@UI
public class NewUITest extends BaseTest {


    @Test
    public void scenario_some(Quest quest) {
        quest
                .enters(World.EARTH)
                .navigate("https://bakery-flow.demo.vaadin.com/")
                .input().insert(InputFields.USERNAME_FIELD, "barista@vaadin.com")
                .input().insert(InputFields.PASSWORD_FIELD, "barista")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .button().click(ButtonFields.NEW_ORDER_BUTTON)
                .select().selectOption(SelectFields.LOCATION_DDL, "Store")
                .select().getAvailableOptions(SelectFields.LOCATION_DDL)
                .select().getSelectedOptions(SelectFields.LOCATION_DDL)
                .complete();
    }

}
