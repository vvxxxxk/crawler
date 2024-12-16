package com.example.crawler.common.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Slf4j
@Configuration
public class SeleniumConfig {

    @Value("${selenium.browser}")
    private String browser;

    @Value("${selenium.headless}")
    private boolean headless;

    @Value("${selenium.driver-path}")
    private String driverPath;

    @Value("${selenium.window-size.width}")
    private int windowWidth;

    @Value("${selenium.window-size.height}")
    private int windowHeight;

    private WebDriver webDriver;

    @PreDestroy
    public void tearDown() {
        if (this.webDriver != null) {
            log.info("Shutting down WebDriver...");
            this.webDriver.quit();
        }
    }

    public WebDriver createWebDriver() throws IOException {
        String absoluteDriverPath = getAbsolutePath(driverPath);
        if (browser.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", absoluteDriverPath);
            ChromeOptions options = new ChromeOptions();

            if (headless) {
                options.addArguments("--headless");
            }
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=" + windowWidth + "," + windowHeight);

            return new ChromeDriver(options);
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private String getAbsolutePath(String driverPath) {
        URL resource = getClass().getClassLoader().getResource(driverPath.replace("classpath:", ""));
        if (resource == null) {
            throw new IllegalArgumentException("리소스를 찾을 수 없습니다: " + driverPath);
        }

        return new File(resource.getFile()).getAbsolutePath();
    }
}
