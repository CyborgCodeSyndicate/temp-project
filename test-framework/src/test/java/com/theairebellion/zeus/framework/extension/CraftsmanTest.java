package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.config.FrameworkConfig;
import com.theairebellion.zeus.framework.config.FrameworkConfigHolder;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Parameter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
class CraftsmanTest {

    public static final String DOG_PET = "DOG_PET";
    public static final String COM_EXAMPLE = "com.example";

    @InjectMocks
    private Craftsman craftsman;

    @Mock
    private ParameterContext parameterContext;

    @Mock
    private ExtensionContext extensionContext;

    @Mock
    private ExtensionContext.Store store;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private DecoratorsFactory decoratorsFactory;

    @Mock
    private Quest quest;

    @Mock
    private SuperQuest superQuest;

    @Mock
    private Craft craft;

    @Mock
    private DataForge dataForge;

    @Mock
    private Late<Object> late;

    @Mock
    private FrameworkConfig frameworkConfig;

    private Parameter parameter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        parameter = mock(Parameter.class);
        lenient().when(parameterContext.getParameter()).thenReturn(parameter);
    }

    @Test
    void supportsParameterReturnsTrueWhenAnnotatedWithCraft() {
        when(parameterContext.isAnnotated(Craft.class)).thenReturn(true);
        assertTrue(craftsman.supportsParameter(parameterContext, extensionContext));
    }

    @Test
    void supportsParameterReturnsFalseWhenNotAnnotatedWithCraft() {
        when(parameterContext.isAnnotated(Craft.class)).thenReturn(false);
        assertFalse(craftsman.supportsParameter(parameterContext, extensionContext));
    }

    @Test
    void resolveParameterThrowsExceptionWhenQuestNotInStore() {
        when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        when(store.get(StoreKeys.QUEST)).thenReturn(null);
        when(parameterContext.findAnnotation(Craft.class)).thenReturn(Optional.of(craft));
        when(parameter.getType()).thenReturn((Class) Object.class);

        try (var springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(extensionContext))
                    .thenReturn(applicationContext);

            when(applicationContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

            assertThrows(IllegalStateException.class, () ->
                    craftsman.resolveParameter(parameterContext, extensionContext)
            );
        }
    }

    @Test
    void resolveParameterThrowsExceptionIfAnnotationNotPresent() {
        when(parameterContext.findAnnotation(Craft.class)).thenReturn(Optional.empty());
        when(parameter.getType()).thenReturn((Class) Object.class);
        assertThrows(ParameterResolutionException.class, () ->
                craftsman.resolveParameter(parameterContext, extensionContext)
        );
    }

    @Test
    void resolveParameterReturnsLateWhenParameterTypeIsLate() {
        when(parameterContext.findAnnotation(Craft.class)).thenReturn(Optional.of(craft));
        when(parameter.getType()).thenReturn((Class) Late.class);
        when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        when(store.get(StoreKeys.QUEST)).thenReturn(quest);
        when(craft.model()).thenReturn(DOG_PET);

        try (var springExt = mockStatic(SpringExtension.class);
             var frameworkCfg = mockStatic(FrameworkConfigHolder.class);
             var reflection = mockStatic(ReflectionUtil.class)) {

            springExt.when(() -> SpringExtension.getApplicationContext(extensionContext))
                    .thenReturn(applicationContext);

            frameworkCfg.when(FrameworkConfigHolder::getFrameworkConfig)
                    .thenReturn(frameworkConfig);

            when(frameworkConfig.projectPackage()).thenReturn(COM_EXAMPLE);
            when(applicationContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);

            var storageMock = mock(Storage.class);
            var subStorageMock = mock(Storage.class);
            when(superQuest.getStorage()).thenReturn(storageMock);
            when(storageMock.sub(StorageKeysTest.ARGUMENTS)).thenReturn(subStorageMock);

            reflection.when(() ->
                    ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(DataForge.class),
                            eq(DOG_PET),
                            eq(COM_EXAMPLE)
                    )
            ).thenReturn(dataForge);

            when(dataForge.dataCreator()).thenReturn(late);

            var result = craftsman.resolveParameter(parameterContext, extensionContext);
            assertSame(late, result);
        }
    }

    @Test
    void resolveParameterReturnsJoinedObjectWhenParameterTypeIsNotLate() {
        when(parameterContext.findAnnotation(Craft.class)).thenReturn(Optional.of(craft));
        when(parameter.getType()).thenReturn((Class) String.class);
        when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        when(store.get(StoreKeys.QUEST)).thenReturn(quest);
        when(craft.model()).thenReturn(DOG_PET);

        try (var springExt = mockStatic(SpringExtension.class);
             var frameworkCfg = mockStatic(FrameworkConfigHolder.class);
             var reflection = mockStatic(ReflectionUtil.class)) {

            springExt.when(() -> SpringExtension.getApplicationContext(extensionContext))
                    .thenReturn(applicationContext);

            frameworkCfg.when(FrameworkConfigHolder::getFrameworkConfig)
                    .thenReturn(frameworkConfig);

            when(frameworkConfig.projectPackage()).thenReturn(COM_EXAMPLE);
            when(applicationContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);

            var storageMock = mock(Storage.class);
            var subStorageMock = mock(Storage.class);
            when(superQuest.getStorage()).thenReturn(storageMock);
            when(storageMock.sub(StorageKeysTest.ARGUMENTS)).thenReturn(subStorageMock);

            reflection.when(() ->
                    ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(DataForge.class),
                            eq(DOG_PET),
                            eq(COM_EXAMPLE)
                    )
            ).thenReturn(dataForge);

            when(dataForge.dataCreator()).thenReturn(late);
            var joinedObject = new Object();
            when(late.join()).thenReturn(joinedObject);

            var result = craftsman.resolveParameter(parameterContext, extensionContext);
            assertSame(joinedObject, result);
        }
    }
}
