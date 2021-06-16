package com.rookieintraining.cdp.examples;

import com.rookieintraining.cdp.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.devtools.v91.dom.model.Rect;
import org.openqa.selenium.devtools.v91.emulation.Emulation;
import org.openqa.selenium.devtools.v91.page.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Pages extends BaseTest {

    @Test
    public void crash_a_page() throws InterruptedException {
        driver.get("https://duckduckgo.com");
        Thread.sleep(5000);
        devTools.send(Page.crash());
    }

    @Test
    public void take_fullscreen_screenshot() throws InterruptedException, IOException {
        driver.get("https://duckduckgo.com");
        Thread.sleep(15000);
        Page.GetLayoutMetricsResponse layout =
                devTools.send(Page.getLayoutMetrics());

        Rect page = layout.getContentSize();

        devTools.send(Emulation.setDeviceMetricsOverride(page.getWidth().intValue(), page.getHeight().intValue(),
                1, false, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));

        String data = devTools.send(
                Page.captureScreenshot(Optional.of(Page.CaptureScreenshotFormat.JPEG),
                Optional.of(100), Optional.empty(), Optional.of(true), Optional.of(true)));

        byte[] imgData = Base64.getDecoder().decode(data);
        Files.write(Paths.get("./sample.jpeg"), imgData);

        devTools.send(Emulation.clearDeviceMetricsOverride());
    }

    @Test
    public void test_screencast_protocol() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger();
        devTools.addListener(Page.screencastFrame(), screencastFrame -> {
            System.out.println(String.format("SCREENCAST EVENT # =====================> %s", counter.get()));
            byte[] imgData = Base64.getDecoder().decode(screencastFrame.getData());
            try {
                Files.write(Paths.get("./screencast-img-" + counter.getAndIncrement() + ".png"), imgData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            devTools.send(Page.screencastFrameAck(screencastFrame.getSessionId()));
        });
        devTools.send(Page.startScreencast(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.of(1)));

        driver.get("https://duckduckgo.com");
        Thread.sleep(1500);
        devTools.send(Page.stopScreencast());
    }

}
