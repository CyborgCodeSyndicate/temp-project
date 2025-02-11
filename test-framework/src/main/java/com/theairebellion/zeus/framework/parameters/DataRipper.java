package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.quest.SuperQuest;

import java.util.function.Consumer;

public interface DataRipper {

    Consumer<SuperQuest> eliminate();

    Enum<?> enumImpl();

}
