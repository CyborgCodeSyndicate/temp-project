package com.example.project.service;

import com.example.project.base.World;
import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@WorldName("Custom")
@Service
@Scope("prototype")
public class CustomService extends FluentService {


    public CustomService somethingCustom() {

        SmartSelenium artifact = quest.artifact(World.EARTH, SmartSelenium.class);


        return this;
    }


}
