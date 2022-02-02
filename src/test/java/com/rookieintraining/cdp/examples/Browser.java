package com.rookieintraining.cdp.examples;

import com.rookieintraining.cdp.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.devtools.v97.browser.model.Bounds;
import org.openqa.selenium.devtools.v97.browser.model.WindowID;

import java.util.Optional;

public class Browser extends BaseTest {

    boolean isCompleted = false;

    @Test
    public void crash_browser() {
        try {
            driver.get("https://duckduckgo.com");
            devTools.send(org.openqa.selenium.devtools.v97.browser.Browser.crash());
        } catch (WebDriverException wde) {
            System.out.println("Browser successfully crashed!!");
        }
    }

    @Test
    public void download_a_file() throws InterruptedException {
        devTools.send(org.openqa.selenium.devtools.v97.browser.Browser.setDownloadBehavior(
                org.openqa.selenium.devtools.v97.browser.Browser.SetDownloadBehaviorBehavior.ALLOW,
                Optional.empty(),
                Optional.of(System.getProperty("user.dir")),
                Optional.of(true)
        ));
        devTools.addListener(org.openqa.selenium.devtools.v97.browser.Browser.downloadWillBegin(), downloadWillBegin -> {
            System.out.println("Download Started!!");
        });

        devTools.addListener(org.openqa.selenium.devtools.v97.browser.Browser.downloadProgress(), downloadProgress -> {
            System.out.println(downloadProgress.getState() + " : " + downloadProgress.getReceivedBytes()
                    + " : " + downloadProgress.getTotalBytes());
            if (downloadProgress.getState().toString().equalsIgnoreCase("completed")) {
                isCompleted = true;
            }
        });

        driver.get("https://the-internet.herokuapp.com/download");
        Thread.sleep(5000);
        driver.findElement((By.cssSelector("[href=\"download/5mb script.xml\"]"))).click();

        do {
            Thread.sleep(100);
        } while (!isCompleted);

        isCompleted = false;
    }

    @Test
    public void cancel_download_of_file() throws InterruptedException {
        devTools.send(org.openqa.selenium.devtools.v97.browser.Browser.setDownloadBehavior(
                org.openqa.selenium.devtools.v97.browser.Browser.SetDownloadBehaviorBehavior.ALLOW,
                Optional.empty(),
                Optional.of(System.getProperty("java.io.tmpdir")),
                Optional.of(true)
        ));
        devTools.addListener(org.openqa.selenium.devtools.v97.browser.Browser.downloadWillBegin(), downloadWillBegin -> {
            System.out.println("Download Started!!");
        });

        devTools.addListener(org.openqa.selenium.devtools.v97.browser.Browser.downloadProgress(), downloadProgress -> {
            if (downloadProgress.getReceivedBytes().longValue() >= (downloadProgress.getTotalBytes().longValue() / 2L)) {
                devTools.send(org.openqa.selenium.devtools.v97.browser.Browser.cancelDownload(
                        downloadProgress.getGuid(),
                        Optional.empty()
                ));
                isCompleted = true;
            }
        });

        driver.get("https://the-internet.herokuapp.com/download");
        Thread.sleep(5000);
        driver.findElement((By.cssSelector("[href=\"download/5mb script.xml\"]"))).click();

        do {
            Thread.sleep(100);
        } while (!isCompleted);

        isCompleted = false;
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
