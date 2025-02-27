package com.theairebellion.zeus.ui.components.factory;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.Accordion;
import com.theairebellion.zeus.ui.components.accordion.mock.MockAccordionComponentType;
import com.theairebellion.zeus.ui.components.alert.Alert;
import com.theairebellion.zeus.ui.components.alert.mock.MockAlertComponentType;
import com.theairebellion.zeus.ui.components.button.Button;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonComponentType;
import com.theairebellion.zeus.ui.components.checkbox.Checkbox;
import com.theairebellion.zeus.ui.components.checkbox.mock.MockCheckboxComponentType;
import com.theairebellion.zeus.ui.components.factory.mock.*;
import com.theairebellion.zeus.ui.components.input.Input;
import com.theairebellion.zeus.ui.components.input.mock.MockInputComponentType;
import com.theairebellion.zeus.ui.components.link.Link;
import com.theairebellion.zeus.ui.components.link.mock.MockLinkComponentType;
import com.theairebellion.zeus.ui.components.list.ItemList;
import com.theairebellion.zeus.ui.components.list.mock.MockItemListComponentType;
import com.theairebellion.zeus.ui.components.loader.Loader;
import com.theairebellion.zeus.ui.components.loader.mock.MockLoaderComponentType;
import com.theairebellion.zeus.ui.components.modal.Modal;
import com.theairebellion.zeus.ui.components.modal.mock.MockModalComponentType;
import com.theairebellion.zeus.ui.components.radio.Radio;
import com.theairebellion.zeus.ui.components.radio.mock.MockRadioComponentType;
import com.theairebellion.zeus.ui.components.select.Select;
import com.theairebellion.zeus.ui.components.select.mock.MockSelectComponentType;
import com.theairebellion.zeus.ui.components.tab.Tab;
import com.theairebellion.zeus.ui.components.tab.mock.MockTabComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.base.mock.MockTableComponentType;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.service.Table;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.components.toggle.Toggle;
import com.theairebellion.zeus.ui.components.toggle.mock.MockToggleComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
class ComponentFactoryTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private MockedStatic<ReflectionUtil> reflectionMock;

    @BeforeEach
    void setUp() {
        driver = new SmartWebDriver(mock(WebDriver.class));
        reflectionMock = Mockito.mockStatic(ReflectionUtil.class);
    }

    @AfterEach
    void tearDown() {
        reflectionMock.close();
    }

    @Test
    void testGetComponentSuccess() throws Exception {
        Method method = ComponentFactory.class.getDeclaredMethod(
                "getComponent",
                Class.class,
                Class.forName("com.theairebellion.zeus.ui.components.base.ComponentType"),
                String.class,
                SmartWebDriver.class
        );
        method.setAccessible(true);
        when(ReflectionUtil.findImplementationsOfInterface(eq(MockInterface.class), anyString()))
                .thenReturn(new ArrayList<>(Collections.singletonList(MockImpl.class)));
        Object instance = method.invoke(
                null,
                MockInterface.class,
                MockComponentType.DUMMY,
                getClass().getPackage().getName(),
                driver
        );
        assertInstanceOf(MockImpl.class, instance);
    }

    @Test
    void testGetComponentNoImplementation() throws Exception {
        Method method = ComponentFactory.class.getDeclaredMethod(
                "getComponent",
                Class.class,
                Class.forName("com.theairebellion.zeus.ui.components.base.ComponentType"),
                String.class,
                SmartWebDriver.class
        );
        method.setAccessible(true);
        when(ReflectionUtil.findImplementationsOfInterface(eq(MockInterface.class), anyString()))
                .thenReturn(new ArrayList<>());
        assertThrows(InvocationTargetException.class, () ->
                method.invoke(
                        null,
                        MockInterface.class,
                        MockComponentType.NON_EXISTENT,
                        getClass().getPackage().getName(),
                        driver
                )
        );
    }

    @Test
    void testGetComponentFailCreation() throws Exception {
        Method method = ComponentFactory.class.getDeclaredMethod(
                "getComponent",
                Class.class,
                Class.forName("com.theairebellion.zeus.ui.components.base.ComponentType"),
                String.class,
                SmartWebDriver.class
        );
        method.setAccessible(true);
        when(ReflectionUtil.findImplementationsOfInterface(eq(MockInterface.class), anyString()))
                .thenReturn(new ArrayList<>(Collections.singletonList(FailImpl.class)));
        assertThrows(InvocationTargetException.class, () ->
                method.invoke(
                        null,
                        MockInterface.class,
                        MockComponentType.FAIL,
                        getClass().getPackage().getName(),
                        driver
                )
        );
    }

    @Test
    void testGetAccordionComponent() {
        List<Class<? extends Accordion>> implList = new ArrayList<>();
        implList.add(TempAccordionImpl.class);

        when(ReflectionUtil.findImplementationsOfInterface(eq(Accordion.class), anyString()))
                .thenReturn(implList);

        Accordion result = ComponentFactory.getAccordionComponent(
                MockAccordionComponentType.DUMMY, driver);

        assertNotNull(result);
    }

    @Test
    void testGetAlertComponent() {
        List<Class<? extends Alert>> implList = new ArrayList<>();
        implList.add(TempAlertImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Alert.class), anyString()))
                .thenReturn(implList);
        Alert result = ComponentFactory.getAlertComponent(MockAlertComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetButtonComponent() {
        List<Class<? extends Button>> implList = new ArrayList<>();
        implList.add(TempButtonImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Button.class), anyString()))
                .thenReturn(implList);
        Button result = ComponentFactory.getButtonComponent(MockButtonComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetCheckboxComponent() {
        List<Class<? extends Checkbox>> implList = new ArrayList<>();
        implList.add(TempCheckboxImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Checkbox.class), anyString()))
                .thenReturn(implList);
        Checkbox result = ComponentFactory.getCheckBoxComponent(MockCheckboxComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetInputComponent() {
        List<Class<? extends Input>> implList = new ArrayList<>();
        implList.add(TempInputImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Input.class), anyString()))
                .thenReturn(implList);
        Input result = ComponentFactory.getInputComponent(MockInputComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetLinkComponent() {
        List<Class<? extends Link>> implList = new ArrayList<>();
        implList.add(TempLinkImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Link.class), anyString()))
                .thenReturn(implList);
        Link result = ComponentFactory.getLinkComponent(MockLinkComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetListComponent() {
        List<Class<? extends ItemList>> implList = new ArrayList<>();
        implList.add(TempItemListImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(ItemList.class), anyString()))
                .thenReturn(implList);
        ItemList result = ComponentFactory.getListComponent(MockItemListComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetLoaderComponent() {
        List<Class<? extends Loader>> implList = new ArrayList<>();
        implList.add(TempLoaderImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Loader.class), anyString()))
                .thenReturn(implList);
        Loader result = ComponentFactory.getLoaderComponent(MockLoaderComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetModalComponent() {
        List<Class<? extends Modal>> implList = new ArrayList<>();
        implList.add(TempModalImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Modal.class), anyString()))
                .thenReturn(implList);
        Modal result = ComponentFactory.getModalComponent(MockModalComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetRadioComponent() {
        List<Class<? extends Radio>> implList = new ArrayList<>();
        implList.add(TempRadioImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Radio.class), anyString()))
                .thenReturn(implList);
        Radio result = ComponentFactory.getRadioComponent(MockRadioComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetSelectComponent() {
        List<Class<? extends Select>> implList = new ArrayList<>();
        implList.add(TempSelectImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Select.class), anyString()))
                .thenReturn(implList);
        Select result = ComponentFactory.getSelectComponent(MockSelectComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetTabComponent() {
        List<Class<? extends Tab>> implList = new ArrayList<>();
        implList.add(TempTabImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Tab.class), anyString()))
                .thenReturn(implList);
        Tab result = ComponentFactory.getTabComponent(MockTabComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetToggleComponent() {
        List<Class<? extends Toggle>> implList = new ArrayList<>();
        implList.add(TempToggleImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Toggle.class), anyString()))
                .thenReturn(implList);
        Toggle result = ComponentFactory.getToggleComponent(MockToggleComponentType.DUMMY, driver);
        assertNotNull(result);
    }

    @Test
    void testGetTableComponent() {
        List<Class<? extends Table>> implList = new ArrayList<>();
        implList.add(TempTableImpl.class);
        when(ReflectionUtil.findImplementationsOfInterface(eq(Table.class), anyString()))
                .thenReturn(implList);
        Table result = ComponentFactory.getTableComponent(MockTableComponentType.VALUE, driver);
        assertNotNull(result);
    }

    @ImplementationOfType("DUMMY")
    static class TempAccordionImpl implements Accordion {

        public TempAccordionImpl(SmartWebDriver driverRef) {
        }

        @Override
        public void expand(SmartWebElement container, String... accordionText) {

        }

        @Override
        public String expand(SmartWebElement container, Strategy strategy) {
            return "";
        }

        @Override
        public void expand(String... accordionText) {

        }

        @Override
        public void expand(By... accordionLocator) {

        }

        @Override
        public void collapse(SmartWebElement container, String... accordionText) {

        }

        @Override
        public String collapse(SmartWebElement container, Strategy strategy) {
            return "";
        }

        @Override
        public void collapse(String... accordionText) {

        }

        @Override
        public void collapse(By... accordionLocator) {

        }

        @Override
        public boolean areEnabled(SmartWebElement container, String... accordionText) {
            return false;
        }

        @Override
        public boolean areEnabled(String... accordionText) {
            return false;
        }

        @Override
        public boolean areEnabled(By... accordionLocator) {
            return false;
        }

        @Override
        public List<String> getExpanded(SmartWebElement container) {
            return List.of();
        }

        @Override
        public List<String> getCollapsed(SmartWebElement container) {
            return List.of();
        }

        @Override
        public List<String> getAll(SmartWebElement container) {
            return List.of();
        }

        @Override
        public String getTitle(By accordionLocator) {
            return "";
        }

        @Override
        public String getText(By accordionLocator) {
            return "";
        }
    }

    @ImplementationOfType("DUMMY")
    static class TempAlertImpl implements Alert {
        public TempAlertImpl(SmartWebDriver d) {
        }

        @Override
        public String getValue(SmartWebElement container) {
            return "";
        }

        @Override
        public String getValue(By containerLocator) {
            return "";
        }

        @Override
        public boolean isVisible(SmartWebElement container) {
            return false;
        }

        @Override
        public boolean isVisible(By containerLocator) {
            return false;
        }
    }

    @ImplementationOfType("DUMMY")
    static class TempButtonImpl implements Button {
        public TempButtonImpl(SmartWebDriver d) {
        }

        @Override
        public void click(SmartWebElement container, String buttonText) {

        }

        @Override
        public void click(SmartWebElement container) {

        }

        @Override
        public void click(String buttonText) {

        }

        @Override
        public void click(By buttonLocator) {

        }

        @Override
        public boolean isEnabled(SmartWebElement container, String buttonText) {
            return false;
        }

        @Override
        public boolean isEnabled(SmartWebElement container) {
            return false;
        }

        @Override
        public boolean isEnabled(String buttonText) {
            return false;
        }

        @Override
        public boolean isEnabled(By buttonLocator) {
            return false;
        }

        @Override
        public boolean isVisible(SmartWebElement container, String buttonText) {
            return false;
        }

        @Override
        public boolean isVisible(SmartWebElement container) {
            return false;
        }

        @Override
        public boolean isVisible(String buttonText) {
            return false;
        }

        @Override
        public boolean isVisible(By buttonLocator) {
            return false;
        }
    }

    @ImplementationOfType("DUMMY")
    static class TempCheckboxImpl implements Checkbox {
        public TempCheckboxImpl(SmartWebDriver d) {
        }

        @Override
        public void select(SmartWebElement container, String... checkBoxText) {

        }

        @Override
        public String select(SmartWebElement container, Strategy strategy) {
            return "";
        }

        @Override
        public void select(String... checkBoxText) {

        }

        @Override
        public void select(By... checkBoxLocator) {

        }

        @Override
        public void deSelect(SmartWebElement container, String... checkBoxText) {

        }

        @Override
        public String deSelect(SmartWebElement container, Strategy strategy) {
            return "";
        }

        @Override
        public void deSelect(String... checkBoxText) {

        }

        @Override
        public void deSelect(By... checkBoxLocator) {

        }

        @Override
        public boolean areSelected(SmartWebElement container, String... checkBoxText) {
            return false;
        }

        @Override
        public boolean areSelected(String... checkBoxText) {
            return false;
        }

        @Override
        public boolean areSelected(By... checkBoxLocator) {
            return false;
        }

        @Override
        public boolean areEnabled(SmartWebElement container, String... checkBoxText) {
            return false;
        }

        @Override
        public boolean areEnabled(String... checkBoxText) {
            return false;
        }

        @Override
        public boolean areEnabled(By... checkBoxLocator) {
            return false;
        }

        @Override
        public List<String> getSelected(SmartWebElement container) {
            return List.of();
        }

        @Override
        public List<String> getSelected(By containerLocator) {
            return List.of();
        }

        @Override
        public List<String> getAll(SmartWebElement SmartWebElement) {
            return List.of();
        }

        @Override
        public List<String> getAll(By containerLocator) {
            return List.of();
        }
    }

    @ImplementationOfType("DUMMY")
    static class TempInputImpl implements Input {
        public TempInputImpl(SmartWebDriver d) {
        }

        @Override
        public void insert(SmartWebElement container, String value) {

        }

        @Override
        public void insert(SmartWebElement container, String inputFieldLabel, String value) {

        }

        @Override
        public void insert(String inputFieldLabel, String value) {

        }

        @Override
        public void insert(By inputFieldContainerLocator, String value) {

        }

        @Override
        public void clear(SmartWebElement container) {

        }

        @Override
        public void clear(SmartWebElement container, String inputFieldLabel) {

        }

        @Override
        public void clear(String inputFieldLabel) {

        }

        @Override
        public void clear(By inputFieldContainerLocator) {

        }

        @Override
        public String getValue(SmartWebElement container) {
            return "";
        }

        @Override
        public String getValue(SmartWebElement container, String inputFieldLabel) {
            return "";
        }

        @Override
        public String getValue(String inputFieldLabel) {
            return "";
        }

        @Override
        public String getValue(By inputFieldContainerLocator) {
            return "";
        }

        @Override
        public boolean isEnabled(SmartWebElement container) {
            return false;
        }

        @Override
        public boolean isEnabled(SmartWebElement container, String inputFieldLabel) {
            return false;
        }

        @Override
        public boolean isEnabled(String inputFieldLabel) {
            return false;
        }

        @Override
        public boolean isEnabled(By inputFieldContainerLocator) {
            return false;
        }

        @Override
        public String getErrorMessage(SmartWebElement container) {
            return "";
        }

        @Override
        public String getErrorMessage(SmartWebElement container, String inputFieldLabel) {
            return "";
        }

        @Override
        public String getErrorMessage(String inputFieldLabel) {
            return "";
        }

        @Override
        public String getErrorMessage(By inputFieldContainerLocator) {
            return "";
        }
    }

    @ImplementationOfType("DUMMY")
    static class TempLinkImpl implements Link {
        public TempLinkImpl(SmartWebDriver d) {
        }

        @Override
        public void doubleClick(SmartWebElement container, String buttonText) {

        }

        @Override
        public void doubleClick(SmartWebElement container) {

        }

        @Override
        public void doubleClick(String buttonText) {

        }

        @Override
        public void doubleClick(By buttonLocator) {

        }

        @Override
        public void click(SmartWebElement container, String buttonText) {

        }

        @Override
        public void click(SmartWebElement container) {

        }

        @Override
        public void click(String buttonText) {

        }

        @Override
        public void click(By buttonLocator) {

        }

        @Override
        public boolean isEnabled(SmartWebElement container, String buttonText) {
            return false;
        }

        @Override
        public boolean isEnabled(SmartWebElement container) {
            return false;
        }

        @Override
        public boolean isEnabled(String buttonText) {
            return false;
        }

        @Override
        public boolean isEnabled(By buttonLocator) {
            return false;
        }

        @Override
        public boolean isVisible(SmartWebElement container, String buttonText) {
            return false;
        }

        @Override
        public boolean isVisible(SmartWebElement container) {
            return false;
        }

        @Override
        public boolean isVisible(String buttonText) {
            return false;
        }

        @Override
        public boolean isVisible(By buttonLocator) {
            return false;
        }
    }

    @ImplementationOfType("DUMMY")
    static class TempItemListImpl implements ItemList {
        public TempItemListImpl(SmartWebDriver d) {
        }

        @Override
        public void select(SmartWebElement container, String... itemText) {

        }

        @Override
        public void select(By containerLocator, String... itemText) {

        }

        @Override
        public String select(SmartWebElement container, Strategy strategy) {
            return "";
        }

        @Override
        public String select(By containerLocator, Strategy strategy) {
            return "";
        }

        @Override
        public void select(String... itemText) {

        }

        @Override
        public void select(By... itemListLocator) {

        }

        @Override
        public void deSelect(SmartWebElement container, String... itemText) {

        }

        @Override
        public void deSelect(By containerLocator, String... itemText) {

        }

        @Override
        public String deSelect(SmartWebElement container, Strategy strategy) {
            return "";
        }

        @Override
        public String deSelect(By containerLocator, Strategy strategy) {
            return "";
        }

        @Override
        public void deSelect(String... itemText) {

        }

        @Override
        public void deSelect(By... itemListLocator) {

        }

        @Override
        public boolean areSelected(SmartWebElement container, String... itemText) {
            return false;
        }

        @Override
        public boolean areSelected(By containerLocator, String... itemText) {
            return false;
        }

        @Override
        public boolean areSelected(String... itemText) {
            return false;
        }

        @Override
        public boolean areSelected(By... itemListLocator) {
            return false;
        }

        @Override
        public boolean areEnabled(SmartWebElement container, String... itemText) {
            return false;
        }

        @Override
        public boolean areEnabled(By containerLocator, String... itemText) {
            return false;
        }

        @Override
        public boolean areEnabled(String... itemText) {
            return false;
        }

        @Override
        public boolean areEnabled(By... itemLocator) {
            return false;
        }

        @Override
        public boolean areVisible(SmartWebElement container, String... itemText) {
            return false;
        }

        @Override
        public boolean areVisible(By containerLocator, String... itemText) {
            return false;
        }

        @Override
        public boolean areVisible(String... itemText) {
            return false;
        }

        @Override
        public boolean areVisible(By... itemLocator) {
            return false;
        }

        @Override
        public List<String> getSelected(SmartWebElement container) {
            return List.of();
        }

        @Override
        public List<String> getSelected(By containerLocator) {
            return List.of();
        }

        @Override
        public List<String> getAll(SmartWebElement container) {
            return List.of();
        }

        @Override
        public List<String> getAll(By containerLocator) {
            return List.of();
        }
    }

    @ImplementationOfType("DUMMY")
    static class TempLoaderImpl implements Loader {
        public TempLoaderImpl(SmartWebDriver d) {
        }

        @Override
        public boolean isVisible(SmartWebElement container) {
            return false;
        }

        @Override
        public boolean isVisible(By loaderLocator) {
            return false;
        }

        @Override
        public void waitToBeShown(SmartWebElement container, int secondsShown) {

        }

        @Override
        public void waitToBeShown(int secondsShown) {

        }

        @Override
        public void waitToBeShown(By loaderLocator, int secondsShown) {

        }

        @Override
        public void waitToBeRemoved(SmartWebElement container, int secondsRemoved) {

        }

        @Override
        public void waitToBeRemoved(int secondsRemoved) {

        }

        @Override
        public void waitToBeRemoved(By loaderLocator, int secondsRemoved) {

        }
    }

    @ImplementationOfType("DUMMY")
    static class TempModalImpl implements Modal {
        public TempModalImpl(SmartWebDriver d) {
        }

        @Override
        public boolean isOpened() {
            return false;
        }

        @Override
        public void clickButton(SmartWebElement container, String buttonText) {

        }

        @Override
        public void clickButton(String buttonText) {

        }

        @Override
        public void clickButton(By buttonLocator) {

        }

        @Override
        public String getTitle() {
            return "";
        }

        @Override
        public String getBodyText() {
            return "";
        }

        @Override
        public String getContentTitle() {
            return "";
        }

        @Override
        public void close() {

        }
    }

    @ImplementationOfType("DUMMY")
    static class TempRadioImpl implements Radio {
        public TempRadioImpl(SmartWebDriver d) {
        }

        @Override
        public void select(SmartWebElement container, String radioButtonText) {

        }

        @Override
        public String select(SmartWebElement container, Strategy strategy) {
            return "";
        }

        @Override
        public void select(String radioButtonText) {

        }

        @Override
        public void select(By radioButtonLocator) {

        }

        @Override
        public boolean isEnabled(SmartWebElement container, String radioButtonText) {
            return false;
        }

        @Override
        public boolean isEnabled(String radioButtonText) {
            return false;
        }

        @Override
        public boolean isEnabled(By radioButtonLocator) {
            return false;
        }

        @Override
        public boolean isSelected(SmartWebElement container, String radioButtonText) {
            return false;
        }

        @Override
        public boolean isSelected(String radioButtonText) {
            return false;
        }

        @Override
        public boolean isSelected(By radioButtonLocator) {
            return false;
        }

        @Override
        public boolean isVisible(SmartWebElement container, String radioButtonText) {
            return false;
        }

        @Override
        public boolean isVisible(String radioButtonText) {
            return false;
        }

        @Override
        public boolean isVisible(By radioButtonLocator) {
            return false;
        }

        @Override
        public String getSelected(SmartWebElement container) {
            return "";
        }

        @Override
        public String getSelected(By containerLocator) {
            return "";
        }

        @Override
        public List<String> getAll(SmartWebElement container) {
            return List.of();
        }

        @Override
        public List<String> getAll(By containerLocator) {
            return List.of();
        }
    }

    @ImplementationOfType("DUMMY")
    static class TempSelectImpl implements Select {
        public TempSelectImpl(SmartWebDriver d) {
        }

        @Override
        public void selectOptions(SmartWebElement container, String... values) {

        }

        @Override
        public void selectOptions(By containerLocator, String... values) {

        }

        @Override
        public List<String> selectOptions(SmartWebElement container, Strategy strategy) {
            return List.of();
        }

        @Override
        public List<String> selectOptions(By containerLocator, Strategy strategy) {
            return List.of();
        }

        @Override
        public List<String> getAvailableOptions(SmartWebElement container) {
            return List.of();
        }

        @Override
        public List<String> getAvailableOptions(By containerLocator) {
            return List.of();
        }

        @Override
        public List<String> getSelectedOptions(SmartWebElement container) {
            return List.of();
        }

        @Override
        public List<String> getSelectedOptions(By containerLocator) {
            return List.of();
        }

        @Override
        public boolean isOptionVisible(SmartWebElement container, String value) {
            return false;
        }

        @Override
        public boolean isOptionVisible(By containerLocator, String value) {
            return false;
        }

        @Override
        public boolean isOptionEnabled(SmartWebElement container, String value) {
            return false;
        }

        @Override
        public boolean isOptionEnabled(By containerLocator, String value) {
            return false;
        }
    }

    @ImplementationOfType("DUMMY")
    static class TempTabImpl implements Tab {
        public TempTabImpl(SmartWebDriver d) {
        }

        @Override
        public boolean isSelected(SmartWebElement container, String buttonText) {
            return false;
        }

        @Override
        public boolean isSelected(SmartWebElement container) {
            return false;
        }

        @Override
        public boolean isSelected(String buttonText) {
            return false;
        }

        @Override
        public boolean isSelected(By buttonLocator) {
            return false;
        }

        @Override
        public void click(SmartWebElement container, String buttonText) {

        }

        @Override
        public void click(SmartWebElement container) {

        }

        @Override
        public void click(String buttonText) {

        }

        @Override
        public void click(By buttonLocator) {

        }

        @Override
        public boolean isEnabled(SmartWebElement container, String buttonText) {
            return false;
        }

        @Override
        public boolean isEnabled(SmartWebElement container) {
            return false;
        }

        @Override
        public boolean isEnabled(String buttonText) {
            return false;
        }

        @Override
        public boolean isEnabled(By buttonLocator) {
            return false;
        }

        @Override
        public boolean isVisible(SmartWebElement container, String buttonText) {
            return false;
        }

        @Override
        public boolean isVisible(SmartWebElement container) {
            return false;
        }

        @Override
        public boolean isVisible(String buttonText) {
            return false;
        }

        @Override
        public boolean isVisible(By buttonLocator) {
            return false;
        }
    }

    @ImplementationOfType("DUMMY")
    static class TempToggleImpl implements Toggle {
        public TempToggleImpl(SmartWebDriver d) {
        }

        @Override
        public void activate(SmartWebElement container, String toggleText) {

        }

        @Override
        public void activate(String toggleText) {

        }

        @Override
        public void activate(By toggleLocator) {

        }

        @Override
        public void deactivate(SmartWebElement container, String toggleText) {

        }

        @Override
        public void deactivate(String toggleText) {

        }

        @Override
        public void deactivate(By toggleLocator) {

        }

        @Override
        public boolean isEnabled(SmartWebElement container, String toggleText) {
            return false;
        }

        @Override
        public boolean isEnabled(String toggleText) {
            return false;
        }

        @Override
        public boolean isEnabled(By toggleLocator) {
            return false;
        }

        @Override
        public boolean isActivated(SmartWebElement container, String toggleText) {
            return false;
        }

        @Override
        public boolean isActivated(String toggleText) {
            return false;
        }

        @Override
        public boolean isActivated(By toggleLocator) {
            return false;
        }
    }

    @ImplementationOfType("VALUE")
    static class TempTableImpl implements Table {
        public TempTableImpl(SmartWebDriver d) {
        }

        @Override
        public <T> List<T> readTable(Class<T> clazz) {
            return List.of();
        }

        @Override
        public <T> List<T> readTable(Class<T> clazz, TableField<T>... fields) {
            return List.of();
        }

        @Override
        public <T> List<T> readTable(int start, int end, Class<T> clazz) {
            return List.of();
        }

        @Override
        public <T> List<T> readTable(int start, int end, Class<T> clazz, TableField<T>... fields) {
            return List.of();
        }

        @Override
        public <T> T readRow(int row, Class<T> clazz) {
            return null;
        }

        @Override
        public <T> T readRow(List<String> searchCriteria, Class<T> clazz) {
            return null;
        }

        @Override
        public <T> T readRow(int row, Class<T> clazz, TableField<T>... fields) {
            return null;
        }

        @Override
        public <T> T readRow(List<String> searchCriteria, Class<T> clazz, TableField<T>... fields) {
            return null;
        }

        @Override
        public <T> void insertCellValue(int row, Class<T> tClass, T data) {

        }

        @Override
        public <T> void insertCellValue(int row, Class<T> tClass, TableField<T> field, int index, String... value) {

        }

        @Override
        public <T> void insertCellValue(List<String> searchCriteria, Class<T> tClass, TableField<T> field, int index, String... values) {

        }

        @Override
        public <T> void insertCellValue(List<String> searchCriteria, Class<T> tClass, T data) {

        }

        @Override
        public <T> void filterTable(Class<T> tclass, TableField<T> column, FilterStrategy filterStrategy, String... values) {

        }

        @Override
        public <T> void sortTable(Class<T> tclass, TableField<T> column, SortingStrategy sortingStrategy) {

        }
    }

}