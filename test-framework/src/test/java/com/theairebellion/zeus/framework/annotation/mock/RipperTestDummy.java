package com.theairebellion.zeus.framework.annotation.mock;

import com.theairebellion.zeus.framework.annotation.Ripper;

public class RipperTestDummy {
    @Ripper(targets = {"target1", "target2"})
    public void dummyRipperMethod() {
    }
}
