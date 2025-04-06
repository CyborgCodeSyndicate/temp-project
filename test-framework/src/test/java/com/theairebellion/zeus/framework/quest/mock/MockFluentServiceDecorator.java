package com.theairebellion.zeus.framework.quest.mock;

import com.theairebellion.zeus.framework.chain.FluentServiceDecorator;

public class MockFluentServiceDecorator extends FluentServiceDecorator {

    public MockFluentServiceDecorator(MockFluentService delegate) {
        super(delegate);
    }
}