package com.example.project.ui.components.link;

import com.example.project.ui.types.LinkFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.link.Link;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;


@ImplementationOfType(LinkFieldTypes.MD_LINK)
public class LinkMDImpl extends BaseComponent implements Link {

    private static final By LINK_CLASS_NAME_SELECTOR = By.className("mat-link-base");
    private static final String DISABLED_STATE = "mat-link-disabled";


    public LinkMDImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }


    @Override
    public void click(WebElement container, String linkText) {
        WebElement link = findLinkInContainer(container, linkText);
        smartSelenium.smartClick(link);
    }


    @Override
    public void click(WebElement container) {
        WebElement link = findLinkInContainer(container, null);
        smartSelenium.smartClick(link);
    }


    @Override
    public void click(String linkText) {
        WebElement link = findLinkByText(linkText);
        smartSelenium.smartClick(link);
    }


    @Override
    public void click(By linkLocator) {
        WebElement link = smartSelenium.waitAndFindElement(linkLocator);
        smartSelenium.smartClick(link);
    }


    @Override
    public void doubleClick(WebElement container, String linkText) {
        WebElement link = findLinkInContainer(container, linkText);
        smartSelenium.smartDoubleClick(link);
    }


    @Override
    public void doubleClick(WebElement container) {
        WebElement link = findLinkInContainer(container, null);
        smartSelenium.smartDoubleClick(link);
    }


    @Override
    public void doubleClick(String linkText) {
        WebElement link = findLinkByText(linkText);
        smartSelenium.smartDoubleClick(link);
    }


    @Override
    public void doubleClick(By linkLocator) {
        WebElement link = smartSelenium.waitAndFindElement(linkLocator);
        smartSelenium.smartDoubleClick(link);
    }


    @Override
    public boolean isEnabled(WebElement container, String linkText) {
        WebElement link = findLinkInContainer(container, linkText);
        return isLinkEnabled(link);
    }


    @Override
    public boolean isEnabled(WebElement container) {
        WebElement link = findLinkInContainer(container, null);
        return isLinkEnabled(link);
    }


    @Override
    public boolean isEnabled(String linkText) {
        WebElement link = findLinkByText(linkText);
        return isLinkEnabled(link);
    }


    @Override
    public boolean isEnabled(By linkLocator) {
        WebElement link = smartSelenium.waitAndFindElement(linkLocator);
        return isLinkEnabled(link);
    }


    @Override
    public boolean isVisible(WebElement container, String linkText) {
        WebElement link = findLinkInContainer(container, linkText);
        return isLinkEnabled(link);
    }


    @Override
    public boolean isVisible(WebElement container) {
        WebElement link = findLinkInContainer(container, null);
        return isLinkEnabled(link);
    }


    @Override
    public boolean isVisible(String linkText) {
        WebElement link = findLinkByText(linkText);
        return isLinkEnabled(link);
    }


    @Override
    public boolean isVisible(By linkLocator) {
        WebElement link = smartSelenium.waitAndFindElement(linkLocator);
        return isLinkEnabled(link);
    }


    private WebElement findLinkInContainer(WebElement container, String linkText) {
        return smartSelenium.waitAndFindElements(container, LINK_CLASS_NAME_SELECTOR).stream()
                .filter(element -> linkText == null || smartSelenium.smartGetText(element).contains(linkText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Link with text %s not found", linkText)));
    }


    private WebElement findLinkByText(String linkText) {
        return smartSelenium.waitAndFindElements(LINK_CLASS_NAME_SELECTOR).stream()
                .filter(element -> smartSelenium.smartGetText(element).contains(linkText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Link with text %s not found", linkText)));
    }


    private boolean isLinkEnabled(WebElement link) {
        return !smartSelenium.smartGetAttribute(link, "class").contains(DISABLED_STATE);
    }
}