package com.theairebellion.zeus.framework.parameters;

import java.util.function.Supplier;

public interface DataIntercept {

    Supplier<String> getEndpoint();

    Enum<?> enumImpl();

}
