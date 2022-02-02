package com.rookieintraining.cdp.examples;

import com.rookieintraining.cdp.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.devtools.v97.browser.model.Bounds;
import org.openqa.selenium.devtools.v97.browser.model.WindowID;

public class Browser extends BaseTest {

    @Test
    public void crash_browser() {
        driver.get("https://duckduckgo.com");
        devTools.send(org.openqa.selenium.devtools.v97.browser.Browser.crash());
    }

    @Test
    public void get_browser_version() {
        driver.get("https://duckduckgo.com");
        org.openqa.selenium.devtools.v97.browser.Browser.GetVersionResponse response =
                devTools.send(org.openqa.selenium.devtools.v97.browser.Browser.getVersion());

        System.out.println("Browser Version : " + response.getProduct());
        System.out.println("Browser User Agent : " + response.getUserAgent());
        System.out.println("Browser Protocol Version : " + response.getProtocolVersion());
        System.out.println("Browser JS Version : " + response.getJsVersion());
    }

    @Test
    public void get_browser_window_height_width() {
        driver.get("https://duckduckgo.com");
        Bounds bounds = devTools.send(org.openqa.selenium.devtools.v97.browser.Browser.getWindowBounds(new WindowID(1)));

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
