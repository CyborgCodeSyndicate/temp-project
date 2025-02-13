package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FluentServiceDecoratorTest {

    private FluentService mockFluentService;
    private FluentServiceDecorator decorator;

    @BeforeEach
    void setUp() {
        mockFluentService = mock(FluentService.class);
        decorator = new FluentServiceDecorator(mockFluentService);
    }

    @Test
    void testConstructor() {
        assertNotNull(decorator);
    }

    @Test
    void testSetQuest() {
        SuperQuest quest = mock(SuperQuest.class);
        decorator.setQuest(quest);
        verify(mockFluentService).setQuest(quest);
    }

    @Test
    void testValidation() {
        List<AssertionResult<Object>> results = List.of();
        decorator.validation(results);
        verify(mockFluentService).validation(results);
    }

    @Test
    void testPostQuestSetupInitialization() {
        decorator.postQuestSetupInitialization();
        verify(mockFluentService).postQuestSetupInitialization();
    }
}