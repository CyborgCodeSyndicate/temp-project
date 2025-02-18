package com.theairebellion.zeus.ui.components.base;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

public class AbstractComponentServiceTest {

    private DummyComponentService service;

    public enum DummyType implements ComponentType {
        A, B;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @Getter
    public static class DummyComponent {
        private final String value;
        public DummyComponent(String value) {
            this.value = value;
        }
    }

    public static class DummyComponentService extends AbstractComponentService<DummyType, DummyComponent> {
        public DummyComponentService(SmartWebDriver driver) {
            super(driver);
        }
        @Override
        protected DummyComponent createComponent(DummyType componentType) {
            return new DummyComponent(componentType.name());
        }
    }

    @BeforeEach
    public void setUp() {
        System.setProperty("ui.config.file", "ui-config");
        System.setProperty("wait.duration.in.seconds", "2");
        WebDriver original = Mockito.mock(WebDriver.class);
        SmartWebDriver driver = new SmartWebDriver(original);
        service = new DummyComponentService(driver);
    }

    @Test
    public void testGetOrCreateComponentCaching() {
        DummyComponent compA1 = service.getOrCreateComponent(DummyType.A);
        DummyComponent compA2 = service.getOrCreateComponent(DummyType.A);
        DummyComponent compB = service.getOrCreateComponent(DummyType.B);
        assertNotNull(compA1);
        assertSame(compA1, compA2);
        assertNotSame(compA1, compB);
        assertEquals("A", compA1.getValue());
        assertEquals("B", compB.getValue());
    }
}