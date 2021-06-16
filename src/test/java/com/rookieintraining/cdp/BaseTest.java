package com.rookieintraining.cdp;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.remote.Augmenter;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected WebDriver driver;
    protected DevTools devTools;

    @BeforeAll
    public void init() {
        WebDriverManager.chromedriver().setup();
        driver = new Augmenter().augment(new ChromeDriver());
        devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSessionIfThereIsNotOne();
    }

    @AfterAll
    public void quit() {
        driver.quit();
    }

}
