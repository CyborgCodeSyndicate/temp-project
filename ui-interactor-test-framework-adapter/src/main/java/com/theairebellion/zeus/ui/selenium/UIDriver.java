package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.framework.quest.QuestArtifacts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.WebDriver;

@AllArgsConstructor
public class UIDriver implements QuestArtifacts {

    @Getter
    WebDriver driver;

}
