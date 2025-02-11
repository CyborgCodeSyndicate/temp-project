package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.chain.mock.DummyFluentChain;
import com.theairebellion.zeus.framework.chain.mock.DummySuperQuest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.*;

public class FluentChainTest {

    @Test
    void testValidateWithConsumer() {
        DummyFluentChain chain = new DummyFluentChain();
        AtomicBoolean consumerCalled = new AtomicBoolean(false);
        Consumer<SoftAssertions> consumer = soft -> consumerCalled.set(true);
        FluentChain result = chain.validate(consumer);
        assertSame(chain, result, "Method chaining should return the same instance");
        assertTrue(consumerCalled.get(), "Consumer should be called");
    }

    @Test
    void testValidateWithRunnable() {
        DummyFluentChain chain = new DummyFluentChain();
        AtomicBoolean runnableCalled = new AtomicBoolean(false);
        Runnable runnable = () -> runnableCalled.set(true);
        FluentChain result = chain.validate(runnable);
        assertSame(chain, result, "Method chaining should return the same instance");
        assertTrue(runnableCalled.get(), "Runnable should be executed");
    }

    @Test
    void testValidateWithConsumerThrows() {
        DummyFluentChain chain = new DummyFluentChain();
        Consumer<SoftAssertions> consumer = soft -> { throw new RuntimeException("consumer error"); };
        RuntimeException ex = assertThrows(RuntimeException.class, () -> chain.validate(consumer));
        assertEquals("consumer error", ex.getMessage(), "Exception message should match");
    }

    @Test
    void testValidateWithRunnableThrows() {
        DummyFluentChain chain = new DummyFluentChain();
        Runnable runnable = () -> { throw new RuntimeException("runnable error"); };
        RuntimeException ex = assertThrows(RuntimeException.class, () -> chain.validate(runnable));
        assertEquals("runnable error", ex.getMessage(), "Exception message should match");
    }

    @Test
    void testComplete() {
        DummyFluentChain chain = new DummyFluentChain();
        // Before calling complete(), completeCalled should be false.
        DummySuperQuest quest = (DummySuperQuest) chain.then();
        assertFalse(quest.isCompleteCalled(), "Before complete() is called, completeCalled should be false");
        chain.complete();
        assertTrue(quest.isCompleteCalled(), "Quest.complete() should have been called");
    }
}