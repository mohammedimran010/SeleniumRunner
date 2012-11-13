/*
 * Copyright 2011 Aleksey Shilin
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.openqa.runner;

import org.openqa.runner.tests.State;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

/**
 * Implementation of SeServer commands, with WebDriver.
 */
public class Commands {


    /*
     * @TODO add changing State.BaseUrl
     */
    public static void open(WebDriver webDriver, State state, Map<String, String> params) {
        String url = params.get("url");
        if (checkIsRelativeUrl(url)) {

            String base = state.getBaseUrl();
            if (base.endsWith("/"))
                base = base.substring(0, base.length() - 1);

            url = base.concat(url);
        }
        webDriver.get(url);
    }

    /**
     * @param url
     * @return
     * @TODO improve check
     */
    private static boolean checkIsRelativeUrl(String url) {
        if (url.startsWith("http://") || url.startsWith("file://"))
            return false;
        else
            return true;
    }


    /* Basic methods */
    public static void click(WebDriver webDriver, State state, Map<String, String> params) {
        webDriver.findElement(CommandMappings.detectTargetMethod(params.get("target"))).click();
    }

    public static void type(WebDriver webDriver, State state, Map<String, String> params) {
        webDriver.findElement(CommandMappings.detectTargetMethod(params.get("target"))).sendKeys(params.get("text"));
    }


    /* Assert methods */

    public static void assertText(WebDriver webDriver, State state, Map<String, String> params) {
        String targetText = webDriver.findElement(CommandMappings.detectTargetMethod(params.get("target"))).getText();
        if (!targetText.equals(params.get("text")))
            state.setAborted();
    }

    public static void assertTextPresent(WebDriver webDriver, State state, Map<String, String> params) {
        String allText = webDriver.findElement(By.tagName("body")).getText();
        if (!allText.contains(params.get("text")))
            state.setAborted();
    }

    public static void assertTitle(WebDriver webDriver, State state, Map<String, String> params) {
        String title = webDriver.getTitle();
        if (!title.equals(params.get("value")))
            state.setAborted();
    }

    public static void assertValue(WebDriver webDriver, State state, Map<String, String> params) {
        String value = webDriver.findElement(CommandMappings.detectTargetMethod(params.get("target"))).getAttribute("value");
        if (!value.equals(params.get("value")))
            state.setAborted();
    }

    public static void assertTable(WebDriver webDriver, State state, Map<String, String> params) {
        String[] path = params.get("target").split("\\.");
        WebElement webElement = webDriver.findElement(CommandMappings.detectTargetMethod(path[0])).
                findElements(By.tagName("tr")).get(Integer.parseInt(path[1])).
                findElements(By.tagName("td")).get(Integer.parseInt(path[2]));
        String text = webElement.getText();
        if (!text.equals(params.get("value")))
            state.setAborted();
    }

    public static void assertElementPresent(WebDriver webDriver, State state, Map<String, String> params) {
        try {
            WebElement webElement = webDriver.findElement(CommandMappings.detectTargetMethod(params.get("target")));
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            state.setAborted();
        }
    }

    /* Verify Methods */

    public static void verifyText(WebDriver webDriver, State state, Map<String, String> params) {
        String targetText = webDriver.findElement(CommandMappings.detectTargetMethod(params.get("target"))).getText();
        if (!targetText.equals(params.get("text")))
            state.setFailed();
    }

    public static void verifyTextPresent(WebDriver webDriver, State state, Map<String, String> params) {
        String allText = webDriver.findElement(By.tagName("body")).getText();
        if (!allText.contains(params.get("text")))
            state.setFailed();
    }

    public static void verifyTitle(WebDriver webDriver, State state, Map<String, String> params) {
        String title = webDriver.getTitle();
        if (!title.equals(params.get("value")))
            state.setFailed();
    }

    public static void verifyValue(WebDriver webDriver, State state, Map<String, String> params) {
        String value = webDriver.findElement(CommandMappings.detectTargetMethod(params.get("target"))).getAttribute("value");
        if (!value.equals(params.get("value")))
            state.setFailed();
    }

    public static void verifyTable(WebDriver webDriver, State state, Map<String, String> params) {
        String[] path = params.get("target").split("\\.");
        WebElement webElement = webDriver.findElement(CommandMappings.detectTargetMethod(path[0])).
                findElements(By.tagName("tr")).get(Integer.parseInt(path[1])).
                findElements(By.tagName("td")).get(Integer.parseInt(path[2]));
        String text = webElement.getText();
        if (!text.equals(params.get("value")))
            state.setFailed();
    }

    public static void verifyElementPresent(WebDriver webDriver, State state, Map<String, String> params) {
        try {
            WebElement webElement = webDriver.findElement(CommandMappings.detectTargetMethod(params.get("target")));
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            state.setFailed();
        }
    }

    /* WaitFor Methods  */
    public static void waitForText(WebDriver webDriver, State state, Map<String, String> params) {
        throw new NotImplementedException();
    }

    public static void waitForTextPresent(WebDriver webDriver, State state, Map<String, String> params) {
        throw new NotImplementedException();
    }

    public static void waitForTitle(WebDriver webDriver, State state, Map<String, String> params) {
        throw new NotImplementedException();
    }

    public static void waitForValue(WebDriver webDriver, State state, Map<String, String> params) {
        throw new NotImplementedException();
    }

    public static void waitForTable(WebDriver webDriver, State state, Map<String, String> params) {
        throw new NotImplementedException();
    }

    public static void waitForElementPresent(WebDriver webDriver, State state, Map<String, String> params) {
        throw new NotImplementedException();
    }

    /* Store Methods  */
    public static void storeText(WebDriver webDriver, State state, Map<String, String> params) {
        String value = webDriver.findElement(CommandMappings.detectTargetMethod(params.get("target"))).getText();
        state.setVariable(params.get("text"), value);
    }

    public static void storeTextPresent(WebDriver webDriver, State state, Map<String, String> params) {
        throw new NotImplementedException();
    }

    public static void storeTitle(WebDriver webDriver, State state, Map<String, String> params) {
        state.setVariable(params.get("title"), webDriver.getTitle());
    }

    public static void storeValue(WebDriver webDriver, State state, Map<String, String> params) {
        String value = webDriver.findElement(CommandMappings.detectTargetMethod(params.get("target"))).getAttribute("value");
        state.setVariable(params.get("value"), value);
    }

    public static void storeTable(WebDriver webDriver, State state, Map<String, String> params) {
        throw new NotImplementedException();
    }

    public static void storeElementPresent(WebDriver webDriver, State state, Map<String, String> params) {
        throw new NotImplementedException();
    }
}