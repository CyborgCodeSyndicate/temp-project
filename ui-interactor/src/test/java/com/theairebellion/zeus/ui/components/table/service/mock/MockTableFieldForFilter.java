package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.model.TableCell;

public class MockTableFieldForFilter implements TableField<MockRowForFilter> {

    @Override
    public void invoke(MockRowForFilter instance, Object o) {
        instance.setCell((TableCell) o);
    }
}
