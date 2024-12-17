package com.example.crawler.service;

import com.example.crawler.common.config.SeleniumConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final SeleniumConfig seleniumConfig;

    private static final String HSD_MENUS_URL = "https://www.hsd.co.kr/menu/menu_list";

    public Map<String, Object> crawlingHsdMenus() throws InterruptedException {
        Map<String, Object> result = new LinkedHashMap<>();
        WebDriver webDriver = null;
        try {
            // TODO. 현재는 webDriver 각 스레드별 개별 생성, 추후 webDriver 인스턴스를 풀링하여 재사용하는 방안 고려
            webDriver = seleniumConfig.createWebDriver();
            webDriver.get(HSD_MENUS_URL);

            List<WebElement> mainCategoryElements = webDriver.findElements(By.cssSelector(".lnb .dp1"));
            for (WebElement mainCategoryElement : mainCategoryElements) {
                String mainCategory = mainCategoryElement.findElement(By.cssSelector(".dp1_tit > a")).getText();
                log.info("mainCategory: {}", mainCategory);
                mainCategoryElement.findElement(By.cssSelector(".dp1_tit > a")).click();
                Thread.sleep(100);

                // 서브 카테고리 Map
                Map<String, List<Map<String, String>>> subCategoryMap = new LinkedHashMap<>();
                List<WebElement> subCategoryElements = mainCategoryElement.findElements(By.cssSelector(".dp2 li"));
                for (WebElement subCategoryElement : subCategoryElements) {
                    String subCategory = subCategoryElement.findElement(By.cssSelector("a")).getText();
                    log.info("subCategory: {}", subCategory);
                    subCategoryElement.findElement(By.cssSelector("a")).click();
                    Thread.sleep(100);

                    // 메뉴 리스트
                    List<Map<String, String>> menuList = new ArrayList<>();
                    List<WebElement> menuElements = webDriver.findElements(By.cssSelector(".menu_cont .item"));
                    for (WebElement menuElement : menuElements) {
                        String name = menuElement.findElement(By.cssSelector(".item-text h4")).getText();
                        String price = menuElement.findElement(By.cssSelector(".item-price strong")).getText();
                        String img = menuElement.findElement(By.cssSelector(".item-img img")).getAttribute("src");
                        log.info("name: {}, price: {}, img: {}", name, price, img);

                        // 메뉴 정보 Map
                        Map<String, String> menuInfo = new LinkedHashMap<>();
                        menuInfo.put("name", name);
                        menuInfo.put("price", price);
                        menuInfo.put("img", img);

                        menuList.add(menuInfo);
                    }
                    subCategoryMap.put(subCategory, menuList);
                }
                result.put(mainCategory, subCategoryMap);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            quitWebDriver(webDriver);
        }

        return result;
    }

    private void quitWebDriver(WebDriver webDriver) {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
