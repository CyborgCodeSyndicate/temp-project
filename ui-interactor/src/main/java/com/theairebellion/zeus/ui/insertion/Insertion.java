package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import org.openqa.selenium.By;

public interface Insertion {

    void insertion(ComponentType componentType, By locator, Object... values);


}
