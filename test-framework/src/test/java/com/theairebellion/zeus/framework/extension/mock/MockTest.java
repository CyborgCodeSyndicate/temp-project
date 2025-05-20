package com.theairebellion.zeus.framework.extension.mock;

import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;

public class MockTest {

   @PreQuest({
         @Journey(value = "mockJourney", journeyData = {@JourneyData(value = "mockData", late = false)}, order = 1)
   })
   public void annotatedMethod() {
   }

   public void nonAnnotatedMethod() {
   }
}
