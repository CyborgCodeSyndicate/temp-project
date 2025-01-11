package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;

import java.util.function.Consumer;

public interface DataRipper {

    Consumer<@Jailbreak Quest> eliminate();

    Enum<?> enumImpl();

}
