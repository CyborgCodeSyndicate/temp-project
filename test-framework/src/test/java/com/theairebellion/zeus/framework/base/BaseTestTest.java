package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.framework.base.mock.MockEnum;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import com.theairebellion.zeus.framework.storage.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class BaseTestTest {

    @Mock
    SuperQuest superQuest;

    @Mock
    Storage storage;

    @Mock
    Storage subStorage;

    @BeforeAll
    static void loadBaseTestClass() throws ClassNotFoundException {
        Class.forName("com.theairebellion.zeus.framework.base.BaseTest");
    }

    @BeforeEach
    void setUp() {
        QuestHolder.set(superQuest);
        when(superQuest.getStorage()).thenReturn(storage);
    }

    @AfterEach
    void tearDown() {
        QuestHolder.clear();
    }

    @Test
    void retrievesDataByEnumAndClass() {
        MockEnum key = MockEnum.KEY1;
        Class<String> clazz = String.class;
        when(storage.get(key, clazz)).thenReturn("testValue");
        String result = new BaseTest().retrieve(key, clazz);
        assertEquals("testValue", result);
    }

    @Test
    void retrievesDataBySubKeyEnumAndKeyEnumAndClass() {
        MockEnum subKey = MockEnum.KEY2;
        MockEnum key = MockEnum.KEY1;
        Class<Integer> clazz = Integer.class;
        when(storage.sub(subKey)).thenReturn(subStorage);
        when(subStorage.get(key, clazz)).thenReturn(42);
        Integer result = new BaseTest().retrieve(subKey, key, clazz);
        assertEquals(42, result);
    }

    @Test
    void retrievesDataByDataExtractorAndClass() {
        Class<Boolean> clazz = Boolean.class;
        DataExtractor<Boolean> extractor = mock(DataExtractor.class);
        doReturn(MockEnum.KEY1).when(extractor).getKey();
        when(storage.get(extractor, clazz)).thenReturn(true);
        Boolean result = new BaseTest().retrieve(extractor, clazz);
        assertTrue(result);
    }

    @Test
    void retrievesDataByDataExtractorIndexAndClass() {
        Class<Double> clazz = Double.class;
        DataExtractor<Double> extractor = mock(DataExtractor.class);
        doReturn(MockEnum.KEY1).when(extractor).getKey();
        when(storage.get(extractor, clazz, 1)).thenReturn(9.99);
        Double result = new BaseTest().retrieve(extractor, 1, clazz);
        assertEquals(9.99, result);
    }

    @Test
    void defaultStorageRetrievesDataByEnumAndClass() {
        MockEnum key = MockEnum.KEY1;
        Class<String> clazz = String.class;
        when(storage.sub()).thenReturn(subStorage);
        when(subStorage.get(key, clazz)).thenReturn("defaultValue");
        String result = BaseTest.DefaultStorage.retrieve(key, clazz);
        assertEquals("defaultValue", result);
    }

    @Test
    void defaultStorageRetrievesDataByDataExtractorAndClass() {
        Class<Integer> clazz = Integer.class;
        DataExtractor<Integer> extractor = mock(DataExtractor.class);
        when(storage.sub()).thenReturn(subStorage);
        when(subStorage.get(extractor, clazz)).thenReturn(123);
        Integer result = BaseTest.DefaultStorage.retrieve(extractor, clazz);
        assertEquals(123, result);
    }
}
