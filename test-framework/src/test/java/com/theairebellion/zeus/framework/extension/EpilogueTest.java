package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import io.qameta.allure.Allure;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Disabled
class EpilogueTest {

   public static final String TEST_DISPLAY_NAME = "TestDisplayName";
   public static final String ERROR = "Error";
   public static final String TEST_NAME = "testName";
   public static final String EMPTY_LOGS = "emptyLogs";
   public static final String LOG_FILE_NAME = "logFileName";
   public static final String NO_MATCHES = "NoMatches";
   public static final String SOME_TEST = "SomeTest";
   public static final String TEST_WITH_LOGS = "TestWithLogs";
   public static final String THISPATHDOESNOTEXIST = "thispathdoesnotexist";
   public static final String IO_EXCEPTION_TEST = "IOExceptionTest";

   private Epilogue epilogue;
   private ExtensionContext extensionContext;
   private ExtensionContext.Store store;

   @BeforeEach
   void setUp() {
      epilogue = new Epilogue();
      extensionContext = mock(ExtensionContext.class);
      store = mock(ExtensionContext.Store.class);
      lenient().when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
   }

   @Test
   void testAfterTestExecutionSuccess() {
      when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis() - 2000);
      when(extensionContext.getDisplayName()).thenReturn(TEST_DISPLAY_NAME);
      when(extensionContext.getExecutionException()).thenReturn(Optional.empty());
      try (MockedStatic<ThreadContext> threadContextMock = mockStatic(ThreadContext.class);
           MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {

         epilogue.afterTestExecution(extensionContext);

         // Verify the success log message was called
         logTestMock.verify(() ->
               LogTest.info(
                     eq("The quest of '{}' has concluded with glory. Status: {}. Duration: {} seconds."),
                     eq(TEST_DISPLAY_NAME),
                     eq("SUCCESS"),
                     anyLong()
               )
         );

         // Verify ThreadContext.remove was called
         threadContextMock.verify(() -> ThreadContext.remove(TEST_NAME));
      }
   }

   @Test
   void testAfterTestExecutionFailure() {
      when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis() - 3000);
      when(extensionContext.getDisplayName()).thenReturn(TEST_DISPLAY_NAME);
      RuntimeException exception = new RuntimeException(ERROR);
      when(extensionContext.getExecutionException()).thenReturn(Optional.of(exception));

      try (MockedStatic<ThreadContext> threadContextMock = mockStatic(ThreadContext.class);
           MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {

         epilogue.afterTestExecution(extensionContext);

         // Verify the failure log message was called
         logTestMock.verify(() ->
               LogTest.info(
                     eq("The quest of '{}' has ended in defeat. Status: {}. Duration: {} seconds."),
                     eq(TEST_DISPLAY_NAME),
                     eq("FAILED"),
                     anyLong()
               )
         );

         // Verify debug log with the exception
         logTestMock.verify(() -> LogTest.debug(eq("Failure reason:"), eq(exception)));

         // Verify ThreadContext.remove was called
         threadContextMock.verify(() -> ThreadContext.remove(TEST_NAME));
      }
   }

   @Test
   void testAttachFilteredLogsNullName() {
      when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());
      try (MockedStatic<ThreadContext> threadContextMock = mockStatic(ThreadContext.class);
           MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
         threadContextMock.when(() -> ThreadContext.get(TEST_NAME)).thenReturn(null);

         epilogue.afterTestExecution(extensionContext);

         // Verify correct Allure attachment for null test name
         allureMock.verify(() ->
               Allure.addAttachment(
                     eq("Filtered Logs"),
                     eq("text/plain"),
                     eq("Test name is not available."),
                     eq(".log")
               )
         );

         // Verify ThreadContext.remove was called
         threadContextMock.verify(() -> ThreadContext.remove(TEST_NAME));
      }
   }

   @Test
   void testAttachFilteredLogsEmptyName() {
      when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());
      try (MockedStatic<ThreadContext> threadContextMock = mockStatic(ThreadContext.class);
           MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
         threadContextMock.when(() -> ThreadContext.get(TEST_NAME)).thenReturn("");

         epilogue.afterTestExecution(extensionContext);

         // Verify correct Allure attachment for empty test name
         allureMock.verify(() ->
               Allure.addAttachment(
                     eq("Filtered Logs"),
                     eq("text/plain"),
                     eq("Test name is not available."),
                     eq(".log")
               )
         );

         // Verify ThreadContext.remove was called
         threadContextMock.verify(() -> ThreadContext.remove(TEST_NAME));
      }
   }

   @Test
   void testAttachFilteredLogsNoLogsFound() throws IOException {
      when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());
      var tempFile = File.createTempFile(EMPTY_LOGS, ".log");
      tempFile.deleteOnExit();
      System.setProperty(LOG_FILE_NAME, tempFile.getAbsolutePath());

      try (MockedStatic<ThreadContext> threadContextMock = mockStatic(ThreadContext.class);
           MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
         threadContextMock.when(() -> ThreadContext.get(TEST_NAME)).thenReturn(NO_MATCHES);

         epilogue.afterTestExecution(extensionContext);

         // Verify correct Allure attachment for no matching logs
         allureMock.verify(() ->
               Allure.addAttachment(
                     eq("Filtered Logs for Test: " + NO_MATCHES),
                     eq("text/plain"),
                     eq("No logs found for test: " + NO_MATCHES),
                     eq(".log")
               )
         );

         // Verify ThreadContext.remove was called
         threadContextMock.verify(() -> ThreadContext.remove(TEST_NAME));
      }
   }

   @Test
   void testAttachFilteredLogsWithMatchingContent(@TempDir Path tempDir) throws IOException {
      when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());

      // Create a temporary log file with matching content
      Path logFile = tempDir.resolve("test.log");
      String testName = TEST_WITH_LOGS;
      String testIdentifier = "[scenario=" + testName + "]";

      // Create log entries with the test identifier
      List<String> logEntries = List.of(
            "2023-05-01 10:00:00 INFO " + testIdentifier + " Log entry 1",
            "2023-05-01 10:00:01 INFO Some other log without identifier",
            "2023-05-01 10:00:02 ERROR " + testIdentifier + " Log entry 2",
            "2023-05-01 10:00:03 INFO [scenario=otherTest] Other test log"
      );

      Files.write(logFile, logEntries);
      System.setProperty(LOG_FILE_NAME, logFile.toString());

      ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

      try (MockedStatic<ThreadContext> threadContextMock = mockStatic(ThreadContext.class);
           MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
         threadContextMock.when(() -> ThreadContext.get(TEST_NAME)).thenReturn(testName);

         epilogue.afterTestExecution(extensionContext);

         // Verify Allure attachment was called with the filtered content
         allureMock.verify(() ->
               Allure.addAttachment(
                     eq("Filtered Logs for Test: " + testName),
                     eq("text/plain"),
                     contentCaptor.capture(),
                     eq(".log")
               )
         );

         // Verify the content contains only logs with the test identifier
         String capturedContent = contentCaptor.getValue();
         assertTrue(capturedContent.contains("Log entry 1"));
         assertTrue(capturedContent.contains("Log entry 2"));
         assertFalse(capturedContent.contains("Some other log without identifier"));
         assertFalse(capturedContent.contains("Other test log"));

         // Verify ThreadContext.remove was called
         threadContextMock.verify(() -> ThreadContext.remove(TEST_NAME));
      }
   }

   @Test
   void testAttachFilteredLogsIOException() {
      when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());
      System.setProperty(LOG_FILE_NAME, File.separator + THISPATHDOESNOTEXIST + File.separator + "fake.log");

      try (MockedStatic<ThreadContext> threadContextMock = mockStatic(ThreadContext.class);
           MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
         threadContextMock.when(() -> ThreadContext.get(TEST_NAME)).thenReturn(IO_EXCEPTION_TEST);

         epilogue.afterTestExecution(extensionContext);

         // Verify correct Allure attachment for IO exception
         allureMock.verify(() ->
               Allure.addAttachment(
                     eq("Filtered Logs for Test: " + IO_EXCEPTION_TEST),
                     eq("text/plain"),
                     contains("Failed to read or filter logs for test: " + IO_EXCEPTION_TEST),
                     eq(".log")
               )
         );

         // Verify ThreadContext.remove was called
         threadContextMock.verify(() -> ThreadContext.remove(TEST_NAME));
      }
   }

   @Test
   void testStoreNullStartTimeThrowsNPE() {
      when(store.get(StoreKeys.START_TIME, long.class)).thenReturn(null);
      try (MockedStatic<ThreadContext> ignored = mockStatic(ThreadContext.class)) {
         assertThrows(NullPointerException.class, () -> epilogue.afterTestExecution(extensionContext));
      }
   }

   @Test
   void testNoExceptionWhenStoreNotNull() {
      when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());
      when(extensionContext.getExecutionException()).thenReturn(Optional.empty());
      when(extensionContext.getDisplayName()).thenReturn(SOME_TEST);

      try (MockedStatic<ThreadContext> ignored = mockStatic(ThreadContext.class)) {
         assertDoesNotThrow(() -> epilogue.afterTestExecution(extensionContext));
      }
   }
}