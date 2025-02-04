package com.theairebellion.zeus.framework.annotation.mock;

import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;

public class JourneyTestDummy {
    @Journey(value = "first", order = 1, journeyData = {@JourneyData(value = "data1", late = true)})
    @Journey(value = "second", order = 2)
    public void dummyJourneyMethod() {
    }
}
