package com.rookieintraining.cdp.alternative_examples;

import com.google.common.collect.ImmutableMap;
import com.rookieintraining.cdp.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.ConverterFunctions;
import org.openqa.selenium.devtools.v90.browser.model.Bounds;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Browser extends BaseTest {

    @Test
    public void crash_browser() {
        driver.get("https://duckduckgo.com");
        devTools.send(new Command<Void>("Browser.crash", ImmutableMap.of()));
    }

    @Test
    public void get_browser_version() {
        driver.get("https://duckduckgo.com");
        org.openqa.selenium.devtools.v90.browser.Browser.GetVersionResponse response =
                devTools.send(new Command<>("Browser.getVersion",
                        ImmutableMap.of(), input -> input.read(org.openqa.selenium.devtools.v90.browser.Browser.GetVersionResponse.class)));

        System.out.println("Browser Version : " + response.getProduct());
        System.out.println("Browser User Agent : " + response.getUserAgent());
        System.out.println("Browser Protocol Version : " + response.getProtocolVersion());
        System.out.println("Browser JS Version : " + response.getJsVersion());
    }

    @Test
    public void alternative_get_browser_height_width() {
        driver.get("https://duckduckgo.com");
        Bounds bounds = devTools.send(new Command<>("Browser.getWindowBounds",
                ImmutableMap.of("windowId", 1),
                ConverterFunctions.map("bounds", Bounds.class)));

        System.out.println("========================== Browser Bounds ==============================");
        System.out.print("Height : ");
        bounds.getHeight().ifPresent(System.out::println);
        System.out.print("Width : ");
        bounds.getWidth().ifPresent(System.out::println);
        System.out.print("Left : ");
        bounds.getLeft().ifPresent(System.out::println);
        System.out.print("Top : ");
        bounds.getTop().ifPresent(System.out::println);
        System.out.print("Window State : ");
        bounds.getWindowState().ifPresent(System.out::println);
        System.out.println("========================================================================");
    }

}
