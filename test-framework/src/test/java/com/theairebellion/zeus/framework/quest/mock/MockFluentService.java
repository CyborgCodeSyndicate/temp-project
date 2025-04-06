package com.theairebellion.zeus.framework.quest.mock;

import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;

@TestService("MockWorld")
public class MockFluentService extends FluentService {

    public String mockField = "mockValue";
}
