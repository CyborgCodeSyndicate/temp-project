package com.theairebellion.zeus.framework.annotation.mock;

import com.theairebellion.zeus.framework.annotation.Ripper;

public class MockRipperTest {

    @Ripper(targets = {"target1", "target2"})
    public void mockRipperMethod() {
    }
}
