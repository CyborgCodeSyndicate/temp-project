package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.chain.mock.DummyFluentService;
import com.theairebellion.zeus.framework.chain.mock.DummyQuest;
import com.theairebellion.zeus.framework.chain.mock.DummySuperQuest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FluentServiceTest {

    @Test
    void testThenReturnsQuest() {
        DummyFluentService service = new DummyFluentService();
        DummyQuest dummyQuest = new DummyQuest();
        DummySuperQuest dummySuperQuest = new DummySuperQuest(dummyQuest);
        service.setQuest(dummySuperQuest);
        assertSame(dummySuperQuest, service.then(), "then() should return the quest that was set");
    }

    @Test
    void testSetQuest() {
        DummyFluentService service = new DummyFluentService();
        DummyQuest dummyQuest = new DummyQuest();
        DummySuperQuest dummySuperQuest = new DummySuperQuest(dummyQuest);
        service.setQuest(dummySuperQuest);
        assertSame(dummySuperQuest, service.then(), "setQuest() should store the quest");
    }

    @Test
    void testValidationHardSuccess() {
        DummyFluentService service = new DummyFluentService();
        DummyQuest dummyQuest = new DummyQuest();
        DummySuperQuest dummySuperQuest = new DummySuperQuest(dummyQuest);
        service.setQuest(dummySuperQuest);
        // Hard (non‑soft) assertion result that passes.
        AssertionResult<Object> ar = new AssertionResult<>(true, "Hard success test", "expected", "actual", false);
        List<AssertionResult<Object>> results = Collections.singletonList(ar);
        assertDoesNotThrow(() -> service.callValidation(results));
    }

    @Test
    void testValidationHardFailure() {
        DummyFluentService service = new DummyFluentService();
        DummyQuest dummyQuest = new DummyQuest();
        DummySuperQuest dummySuperQuest = new DummySuperQuest(dummyQuest);
        service.setQuest(dummySuperQuest);
        // Hard assertion result that fails.
        AssertionResult<Object> ar = new AssertionResult<>(false, "Hard failure test", "expected", "actual", false);
        List<AssertionResult<Object>> results = Collections.singletonList(ar);
        AssertionError error = assertThrows(AssertionError.class, () -> service.callValidation(results));
        assertTrue(error.getMessage().contains("Hard failure test"),
                "Error message should contain the test description");
    }

    @Test
    void testValidationSoftSuccess() throws Exception {
        DummyFluentService service = new DummyFluentService();
        DummyQuest dummyQuest = new DummyQuest();
        DummySuperQuest dummySuperQuest = new DummySuperQuest(dummyQuest);
        service.setQuest(dummySuperQuest);
        // Soft assertion result that passes.
        AssertionResult<Object> ar = new AssertionResult<>(true, "Soft success test", "expected", "actual", true);
        List<AssertionResult<Object>> results = Collections.singletonList(ar);
        assertDoesNotThrow(() -> service.callValidation(results));
        // Use reflection to call the private getSoftAssertions() method in Quest.
        Method getSoftAssertions = dummySuperQuest.getClass().getSuperclass().getDeclaredMethod("getSoftAssertions");
        getSoftAssertions.setAccessible(true);
        Object softObj = getSoftAssertions.invoke(dummySuperQuest);
        assertNotNull(softObj, "SoftAssertions object should not be null");
        SoftAssertions soft = (SoftAssertions) softObj;
        // For a passing soft assertion, assertAll() should not throw.
        assertDoesNotThrow(() -> soft.assertAll());
    }

    @Test
    void testValidationSoftFailure() throws Exception {
        DummyFluentService service = new DummyFluentService();
        DummyQuest dummyQuest = new DummyQuest();
        DummySuperQuest dummySuperQuest = new DummySuperQuest(dummyQuest);
        service.setQuest(dummySuperQuest);

        // Create a soft assertion result that fails (passed = false, soft = true)
        AssertionResult<Object> ar = new AssertionResult<>(false, "Soft failure test", "expected", "actual", true);
        List<AssertionResult<Object>> results = Collections.singletonList(ar);

        // Soft validations should not throw immediately.
        assertDoesNotThrow(() -> service.callValidation(results));

        // Retrieve the SoftAssertions object via reflection
        Method m = dummySuperQuest.getClass().getSuperclass().getDeclaredMethod("getSoftAssertions");
        m.setAccessible(true);
        Object softObj = m.invoke(dummySuperQuest);
        assertNotNull(softObj, "SoftAssertions object should not be null");
        SoftAssertions soft = (SoftAssertions) softObj;

        // When assertAll() is called, an AssertionError should be thrown.
        AssertionError error = assertThrows(AssertionError.class, soft::assertAll);
        // The expected error message should contain the result of AssertionResult.toString()
        // For a failing assertion, toString() returns a message starting with "✘ Validation failed: ..."
        assertTrue(error.getMessage().contains("✘ Validation failed: Soft failure test"),
                "Error message should contain the test description");
    }

    @Test
    void testPostQuestSetupInitialization() {
        DummyFluentService service = new DummyFluentService();
        // postQuestSetupInitialization() is empty; verify that it doesn't throw.
        assertDoesNotThrow(service::postQuestSetupInitialization);
    }
}