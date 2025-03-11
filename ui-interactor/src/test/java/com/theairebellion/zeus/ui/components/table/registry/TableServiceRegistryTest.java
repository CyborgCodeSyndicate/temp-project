package com.theairebellion.zeus.ui.components.table.registry;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.registry.mock.MockComponentType;
import com.theairebellion.zeus.ui.components.table.registry.mock.MockTableFilter;
import com.theairebellion.zeus.ui.components.table.registry.mock.MockTableInsertion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class TableServiceRegistryTest extends BaseUnitUITest {

    private TableServiceRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new TableServiceRegistry();
    }

    @Nested
    class RegistrationTests {
        @Test
        void testRegisterAndGetTableInsertion() {
            TableInsertion insertion = new MockTableInsertion();
            registry.registerService(MockComponentType.class, insertion);
            assertSame(insertion, registry.getTableService(MockComponentType.class));
        }

        @Test
        void testRegisterAndGetTableFilter() {
            TableFilter filter = new MockTableFilter();
            registry.registerService(MockComponentType.class, filter);
            assertSame(filter, registry.getFilterService(MockComponentType.class));
        }
    }

    @Nested
    class LookupTests {
        @Test
        void testGetTableServiceNotRegistered() {
            assertNull(registry.getTableService(MockComponentType.class));
        }

        @Test
        void testGetFilterServiceNotRegistered() {
            assertNull(registry.getFilterService(MockComponentType.class));
        }
    }
}