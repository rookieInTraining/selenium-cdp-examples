package com.rookieintraining.cdp.examples;

import com.rookieintraining.cdp.BaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.devtools.v90.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v90.fetch.model.RequestStage;
import org.openqa.selenium.devtools.v90.network.model.ResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fetch extends BaseTest {

    @BeforeEach
    public void enable_fetch() {
        List<RequestPattern> reqPattern = new ArrayList<>();
        RequestPattern xhrReqPattern = new RequestPattern(
                Optional.of("*"),
                Optional.of(ResourceType.XHR),
                Optional.of(RequestStage.REQUEST));
        reqPattern.add(xhrReqPattern);
        devTools.send(org.openqa.selenium.devtools.v90.fetch.Fetch.enable(Optional.of(reqPattern), Optional.of(false)));
    }

    @Test
    public void get_browser_version_sample() throws InterruptedException {
        devTools.addListener(org.openqa.selenium.devtools.v91.fetch.Fetch.requestPaused(), requestIntercepted -> {
            System.out.println("=====================Request Id=========================");
            System.out.println(requestIntercepted.getRequestId());
            System.out.println("========================================================");

            requestIntercepted.getRequest().getPostData().ifPresent((data) ->{
                System.out.println("=====================Response Data=========================");
                System.out.println(data);
                System.out.println("===========================================================");
            });

            devTools.send(org.openqa.selenium.devtools.v91.fetch.Fetch.continueRequest(
                    requestIntercepted.getRequestId(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty()));
        });
        driver.get("https://www.flipkart.com");
        Thread.sleep(5000);
    }

    @AfterEach
    public void disable_fetch() {
        devTools.send(org.openqa.selenium.devtools.v91.fetch.Fetch.disable());
    }

}
