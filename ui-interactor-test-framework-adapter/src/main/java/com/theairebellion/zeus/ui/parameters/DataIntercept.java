package com.theairebellion.zeus.ui.parameters;

public interface DataIntercept<T extends Enum<T>> {

    String getEndpointSubString();

    T enumImpl();

}
