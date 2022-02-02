package com.rookieintraining.cdp.examples;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.rookieintraining.cdp.BaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.devtools.v97.network.Network;
import org.openqa.selenium.devtools.v97.network.model.ConnectionType;
import org.openqa.selenium.devtools.v97.network.model.Cookie;
import org.openqa.selenium.devtools.v97.network.model.Headers;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class Networks extends BaseTest {

    @BeforeEach
    public void enable_network() {
        devTools.send(Network.enable(Optional.of(1024), Optional.of(1024), Optional.of(1024)));
    }

    @Test
    public void get_all_cookies() throws InterruptedException {
        driver.get("https://duckduckgo.com");
        Thread.sleep(5000);
        List<Cookie> cookies = devTools.send(Network.getAllCookies());
        cookies.forEach(cookie -> {
            System.out.println(cookie.getName() + " = " + cookie.getValue());
        });
    }

    @Test
    public void block_urls_via_network_commands() throws InterruptedException {
        devTools.send(Network.setBlockedURLs(ImmutableList.of("*.jpg")));
        driver.get("https://amazon.in");
        Thread.sleep(5000);
    }

    @Test
    public void add_custom_headers_via_network_commands() throws InterruptedException {
        devTools.send(Network.setExtraHTTPHeaders(new Headers(ImmutableMap.of("customHeaderName", "customHeaderValue"))));
        devTools.addListener(Network.requestWillBeSent(), requestWillBeSent -> {
            System.out.println(
                requestWillBeSent.getRequest().getHeaders().toJson().get("customHeaderName")
                        .toString().equalsIgnoreCase("customHeaderValue")
            );
        });

        driver.get("https://duckduckgo.com");
        Thread.sleep(5000);
    }

    @Test
    public void disable_network_via_network_commands() throws InterruptedException {
        devTools.send(Network.emulateNetworkConditions(true, 0, 0, 0, Optional.of(ConnectionType.NONE)));
        Thread.sleep(5000);
        try {
            driver.get("https://duckduckgo.com");
        } catch (WebDriverException wde) {
            System.out.println("Encountered an error!!");
        }
    }

    @Test
    public void emulate_network_conditions_via_network_commands() throws InterruptedException {
        devTools.send(Network.emulateNetworkConditions(false, 1, 10000,
                1000, Optional.of(ConnectionType.CELLULAR2G)));
        driver.get("https://duckduckgo.com");
        Thread.sleep(5000);
    }

    @Test
    public void capture_websockets_via_network_commands() throws InterruptedException {
        devTools.addListener(Network.webSocketCreated(), (webSocketCreated) ->  {
            System.out.println("Established Connection with : " + webSocketCreated.getUrl());
        });

        devTools.addListener(Network.webSocketFrameReceived(), (socketFrameReceived) ->  {
            byte[] stringBytes = Base64.getDecoder().decode(socketFrameReceived.getResponse().getPayloadData());
            System.out.println("===========> Received :\n" + new String(stringBytes));
        });

        devTools.addListener(Network.webSocketFrameSent(), (socketFrameSent) ->  {
            byte[] stringBytes = Base64.getDecoder().decode(socketFrameSent.getResponse().getPayloadData());
            System.out.println("<=========== Sent :\n" + new String(stringBytes));
        });

        driver.get("http://www.hivemq.com/demos/websocket-client/");
        Thread.sleep(3000);

        driver.findElement(By.cssSelector("#connectButton")).click();
        Thread.sleep(3000);

        driver.findElement(By.cssSelector("#publishPayload")).sendKeys("Lorem Ipsum! Dolor Si amor!");
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("#publishButton")).click();

        Thread.sleep(1000);
    }

    @AfterEach
    public void disable_network() {
        devTools.send(Network.disable());
    }

}
