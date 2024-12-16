package com.example.crawler.controller;

import com.example.crawler.common.config.SeleniumConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppController {

    private final SeleniumConfig seleniumConfig;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/test/webdriver")
    public ResponseEntity<String> webDriver() throws InterruptedException, IOException {
        WebDriver webDriver = null;
        webDriver = seleniumConfig.createWebDriver();

        String url = "https://www.hsd.co.kr/menu/menu_list";
        webDriver.get(url);

        List<WebElement> elementsDepth1 = webDriver.findElements(By.cssSelector(".lnb .dp1"));
        log.info("elementsDepth1.size(): {}", elementsDepth1.size());
        for (WebElement elementDepth1 : elementsDepth1) {
            String main_title = elementDepth1.findElement(By.cssSelector(".dp1_tit > a")).getText();
            log.info("main_title: {}", main_title);

            // 1뎁스 클릭
            elementDepth1.findElement(By.cssSelector(".dp1_tit > a")).click();
            Thread.sleep(100);

            List<WebElement> elementsDepth2 = elementDepth1.findElements(By.cssSelector(".dp2 li"));
            for (WebElement elementDepth2 : elementsDepth2) {
                // 2뎁스 타이틀 가져오기
                String subTitle = elementDepth2.findElement(By.cssSelector("a")).getText();
                log.info("  Sub Title: {}", subTitle);

                // 2뎁스 클릭
                elementDepth2.findElement(By.cssSelector("a")).click();
                Thread.sleep(100);

                List<WebElement> elementsDepth3 = webDriver.findElements(By.cssSelector(".menu_cont .item"));
                for (WebElement elementDepth3 : elementsDepth3) {
                    String title = elementDepth3.findElement(By.cssSelector(".item-text h4")).getText();
                    String price = elementDepth3.findElement(By.cssSelector(".item-price strong")).getText();
                    String imgUrl = elementDepth3.findElement(By.cssSelector(".item-img img")).getAttribute("src");
                    log.info("      title: {}", title);
                    log.info("      price: {}", price);
                    log.info("      imgUrl: {}", imgUrl);
                }
            }
        }

        return null;
    }

}
