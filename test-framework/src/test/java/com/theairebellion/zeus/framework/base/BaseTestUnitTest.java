package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.framework.base.mock.MockEnum;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import com.theairebellion.zeus.framework.storage.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
@DisplayName("BaseTest Unit Tests")
class BaseTestUnitTest {

   private static final String DUMMY_PROP = "dummy.prop";
   private static final String DUMMY_VALUE = "dummyValue";
   private static final String TEST_VALUE = "testValue";
   private static final String DEFAULT_VALUE = "defaultValue";

   @Mock
   private SuperQuest superQuest;

   @Mock
   private Storage storage;

   @Mock
   private Storage subStorage;

   private BaseTest baseTest;

   private MockedStatic<QuestHolder> questHolderMock;
   private MockedStatic<LogTest> logTestMock;

   @BeforeEach
   void setUp() {
      // First, setup mocks that will be accessed during test
      lenient().when(superQuest.getStorage()).thenReturn(storage);

      // Then setup static mocks
      questHolderMock = mockStatic(QuestHolder.class);
      questHolderMock.when(QuestHolder::get).thenReturn(superQuest);

      // Setup LogTest mock
      logTestMock = mockStatic(LogTest.class);

      // Finally create test object
      baseTest = new BaseTest();
   }

   @AfterEach
   void tearDown() {
      if (questHolderMock != null) {
         questHolderMock.close();
      }
      if (logTestMock != null) {
         logTestMock.close();
      }
      System.clearProperty(DUMMY_PROP);
   }

   @Test
   @DisplayName("retrieve(Enum, Class) should return data from storage")
   void retrievesDataByEnumAndClass() {
      // Given
      MockEnum key = MockEnum.KEY1;
      Class<String> clazz = String.class;
      when(storage.get(key, clazz)).thenReturn(TEST_VALUE);

      // When
      String result = baseTest.retrieve(key, clazz);

      // Then
      assertEquals(TEST_VALUE, result);
      verify(storage).get(key, clazz);
      // Verify the log method was called with matchers for all parameters
      logTestMock.verify(() -> LogTest.extended(anyString(), any(), any()));
   }

   @Test
   @DisplayName("retrieve(Enum, Enum, Class) should return data from sub-storage")
   void retrievesDataBySubKeyEnumAndKeyEnumAndClass() {
      // Given
      MockEnum subKey = MockEnum.KEY2;
      MockEnum key = MockEnum.KEY1;
      Class<Integer> clazz = Integer.class;
      when(storage.sub(subKey)).thenReturn(subStorage);
      when(subStorage.get(key, clazz)).thenReturn(42);

      // When
      Integer result = baseTest.retrieve(subKey, key, clazz);

      // Then
      assertEquals(42, result);
      verify(storage).sub(subKey);
      verify(subStorage).get(key, clazz);
      // Verify the log method was called with matchers for all parameters
      logTestMock.verify(() -> LogTest.extended(anyString(), any(), any()));
   }

   @Test
   @DisplayName("retrieve(DataExtractor, Class) should return data from storage using extractor")
   void retrievesDataByDataExtractorAndClass() {
      // Given
      Class<Boolean> clazz = Boolean.class;
      DataExtractor<Boolean> extractor = mock(DataExtractor.class);
      doReturn(MockEnum.KEY1).when(extractor).getKey();
      when(storage.get(extractor, clazz)).thenReturn(true);

      // When
      Boolean result = baseTest.retrieve(extractor, clazz);

      // Then
      assertTrue(result);
      verify(storage).get(extractor, clazz);
      // Verify the log method was called with matchers for all parameters
      logTestMock.verify(() -> LogTest.extended(anyString(), any(), any()));
   }

   @Test
   @DisplayName("retrieve(DataExtractor, int, Class) should return indexed data from storage using extractor")
   void retrievesDataByDataExtractorIndexAndClass() {
      // Given
      Class<Double> clazz = Double.class;
      DataExtractor<Double> extractor = mock(DataExtractor.class);
      doReturn(MockEnum.KEY1).when(extractor).getKey();
      when(storage.get(extractor, clazz, 1)).thenReturn(9.99);

      // When
      Double result = baseTest.retrieve(extractor, 1, clazz);

      // Then
      assertEquals(9.99, result);
      verify(storage).get(extractor, clazz, 1);
      // Verify the log method was called with matchers for all parameters
      logTestMock.verify(() -> LogTest.extended(anyString(), any(), any()));
   }

   @Test
   @DisplayName("DefaultStorage.retrieve(Enum, Class) should return data from default sub-storage")
   void defaultStorageRetrievesDataByEnumAndClass() {
      // Given
      MockEnum key = MockEnum.KEY1;
      Class<String> clazz = String.class;
      when(storage.sub()).thenReturn(subStorage);
      when(subStorage.get(key, clazz)).thenReturn(DEFAULT_VALUE);

      // When
      String result = BaseTest.DefaultStorage.retrieve(key, clazz);

      // Then
      assertEquals(DEFAULT_VALUE, result);
      verify(storage).sub();
      verify(subStorage).get(key, clazz);
   }

   @Test
   @DisplayName("DefaultStorage.retrieve(DataExtractor, Class) should return data from default sub-storage using extractor")
   void defaultStorageRetrievesDataByDataExtractorAndClass() {
      // Given
      Class<Integer> clazz = Integer.class;
      DataExtractor<Integer> extractor = mock(DataExtractor.class);
      when(storage.sub()).thenReturn(subStorage);
      when(subStorage.get(extractor, clazz)).thenReturn(123);

      // When
      Integer result = BaseTest.DefaultStorage.retrieve(extractor, clazz);

      // Then
      assertEquals(123, result);
      verify(storage).sub();
      verify(subStorage).get(extractor, clazz);
   }


}