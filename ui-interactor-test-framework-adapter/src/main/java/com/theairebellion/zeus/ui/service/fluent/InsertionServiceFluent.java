package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.ui.insertion.InsertionService;
import com.theairebellion.zeus.framework.storage.Storage;

public class InsertionServiceFluent {

    private final InsertionService insertionService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public InsertionServiceFluent(final InsertionService insertionService, final UIServiceFluent uiServiceFluent,
                                  final Storage storage) {
        this.insertionService = insertionService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent insertData(Object data) {
        insertionService.insertData(data);
        return uiServiceFluent;
    }

}
