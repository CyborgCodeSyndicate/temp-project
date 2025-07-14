package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.allure.CustomAllureListener;
import com.theairebellion.zeus.framework.allure.StepType;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.framework.util.AllureStepHelper;
import com.theairebellion.zeus.framework.util.ResourceLoader;
import com.theairebellion.zeus.framework.util.TestContextManager;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.theairebellion.zeus.framework.storage.StoreKeys.HTML;
import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EpilogueTest {

    @Mock
    private ExtensionContext mockContext;

    @Mock
    private ExtensionContext.Store mockStore;

    @Mock
    private SuperQuest mockSuperQuest;

    @Mock
    private Storage subStorage;

    private Epilogue epilogue;

    private MockedStatic<CustomAllureListener> customAllureMock;
    private MockedStatic<ResourceLoader> resourceLoaderMock;
    private MockedStatic<TestContextManager> testContextManagerMock;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        epilogue = spy(new Epilogue());

        // Initialize static mocks *within* each test setup
        customAllureMock = mockStatic(CustomAllureListener.class);
        resourceLoaderMock = mockStatic(ResourceLoader.class);
        testContextManagerMock = mockStatic(TestContextManager.class);

        // Mock TestContextManager
        testContextManagerMock.when(() -> TestContextManager.getSuperQuest(mockContext)).thenReturn(mockSuperQuest);
        Storage dummyStorage = mock(Storage.class);
        when(mockSuperQuest.getStorage()).thenReturn(dummyStorage);
        when(dummyStorage.sub(eq(StorageKeysTest.ARGUMENTS))).thenReturn(subStorage);

        // Mock ResourceLoader
        resourceLoaderMock.when(() -> ResourceLoader.loadResourceFile(anyString()))
                .thenReturn("<html><body>{{testName}} {{className}}</body></html>");

        // Setup common mocks for each test
        when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
        when(mockStore.get(eq(START_TIME), eq(long.class))).thenReturn(123L);
        when(mockContext.getExecutionException()).thenReturn(Optional.empty());

        // Setup method mock
        Method mockMethod = EpilogueTest.class.getDeclaredMethod("shouldStopStep_WhenActiveStepIsNotTearDown");
        lenient().when(mockContext.getRequiredTestMethod()).thenReturn(mockMethod);
        lenient().when(mockContext.getRequiredTestClass()).thenReturn((Class) EpilogueTest.class);
    }

    @AfterEach
    void tearDown() {
        // Close static mocks *within* each test teardown
        if (customAllureMock != null) {
            customAllureMock.close();
        }
        if (resourceLoaderMock != null) {
            resourceLoaderMock.close();
        }
        if (testContextManagerMock != null) {
            testContextManagerMock.close();
        }

        // Clear thread context for each test
        ThreadContext.clearAll();
    }

    @Test
    @DisplayName("Should stop step when active step is NOT TEAR_DOWN")
    void shouldStopStep_WhenActiveStepIsNotTearDown() {
        // Given
        customAllureMock.when(CustomAllureListener::getActiveStepName).thenReturn("Some Other Step");
        customAllureMock.when(() -> CustomAllureListener.isStepActive("Some Other Step")).thenReturn(true);
        List<String> htmlContent = new ArrayList<>(List.of("<td>Some content</td>", "<td>Other content</td>"));
        when(mockStore.get(HTML, List.class)).thenReturn(htmlContent);
        when(mockStore.get(HTML)).thenReturn(htmlContent);

        // When
        epilogue.afterTestExecution(mockContext);

        // Then
        customAllureMock.verify(CustomAllureListener::stopStep, times(2));
    }

    @Test
    @DisplayName("Should NOT stop step when active step IS TEAR_DOWN")
    void shouldNotStopStep_WhenActiveStepIsTearDown() {
        // Given
        customAllureMock.when(CustomAllureListener::getActiveStepName).thenReturn(StepType.TEAR_DOWN.getDisplayName());
        customAllureMock.when(() -> CustomAllureListener.isStepActive(StepType.TEAR_DOWN.getDisplayName()))
                .thenReturn(true);
        List<String> htmlContent = new ArrayList<>(List.of("<td>Some content</td>", "<td>Other content</td>"));
        when(mockStore.get(HTML, List.class)).thenReturn(htmlContent);
        when(mockStore.get(HTML)).thenReturn(htmlContent);

        // When
        epilogue.afterTestExecution(mockContext);

        // Then
        customAllureMock.verify(CustomAllureListener::stopStep, times(1));
    }

    @Test
    @DisplayName("Should create new list when html list don't exist")
    void shouldCreateNewList_WhenHtmlListDontExist() {
        // Given
        try (MockedStatic<AllureStepHelper> allureStepHelperMockedStatic = mockStatic(AllureStepHelper.class)) {
            customAllureMock.when(CustomAllureListener::getActiveStepName).thenReturn(StepType.TEAR_DOWN.getDisplayName());
            customAllureMock.when(() -> CustomAllureListener.isStepActive(StepType.TEAR_DOWN.getDisplayName()))
                    .thenReturn(true);
            when(mockStore.get(HTML)).thenReturn(null);
            when(mockStore.get(eq(HTML), eq(List.class))).thenReturn(null);
            when(mockContext.getExecutionException()).thenReturn(Optional.of(new RuntimeException("Test failure")));
            Epilogue spyEpilogue = spy(new Epilogue());

            List<String> htmlContent = new ArrayList<>(List.of("<td>Some content</td>", "<td>Other content</td>"));
            lenient().when(mockStore.get(HTML)).thenReturn(htmlContent);

            // When
            spyEpilogue.afterTestExecution(mockContext);

            // Then
            ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
            verify(mockStore, atLeastOnce()).put(eq(HTML), captor.capture());

            List<List> allPuts = captor.getAllValues();
            List capturedHtmlList = allPuts.get(allPuts.size() - 1);

            assertNotNull(capturedHtmlList);
            assertFalse(capturedHtmlList.isEmpty());
            allureStepHelperMockedStatic.verify(() ->
                    AllureStepHelper.logTestOutcome(
                            nullable(String.class), eq("FAILED"), anyLong(), any(Throwable.class)
                    )
            );
        }
    }
}