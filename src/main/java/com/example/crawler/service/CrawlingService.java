package com.example.crawler.service;

import com.example.crawler.common.config.SeleniumConfig;
import com.example.crawler.common.constants.ContainerType;
import com.example.crawler.common.constants.ExtractionType;
import com.example.crawler.model.dto.common.*;
import com.example.crawler.model.dto.request.CrawlingRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final SeleniumConfig seleniumConfig;

    public void executeCrawling(CrawlingRequestDto params) {
        WebDriver webDriver = null;
        Map<Integer, List<SearchContext>> containers = new HashMap<>();
        List<SearchContext> defaultContext = new ArrayList<>();
        try {
            // TODO. 현재는 webDriver 각 스레드별 개별 생성, 추후 webDriver 인스턴스를 풀링하여 재사용하는 방안 고려
            webDriver = seleniumConfig.createWebDriver();
            defaultContext.add(webDriver);

            containers.put(0, defaultContext);
            List<TopicDto> topics = params.getTopics();
            for (TopicDto topic : topics) {
                webDriver.get(topic.getUrl());

                List<SelectorDto> selectors = topic.getSelectors();
                // log.info("selectors.size(): {}", selectors.size());
                for (SelectorDto selector : selectors) {
                    crawlBySelector(selector, containers);

                }


            }
        } catch (Exception e) {
            log.error("Error during crawling: {}", e.getMessage(), e);
        } finally {
            if (webDriver != null) {
                log.info("Closing WebDriver instance...");
                webDriver.quit();
            }
        }
    }

    private void crawlBySelector(SelectorDto selector, Map<Integer, List<SearchContext>> contexts) {
        int selfContextual = selector.getSelfContextual();
        ElementDto element = selector.getElement();
        LinkDto link = selector.getLink();
        List<ExtractionDto> extractions = selector.getExtractions();
        SelectorDto childSelector = selector.getChild();

        crawlByElement(selfContextual, element, contexts);
        crawlByExtraction(extractions, contexts);

        if (childSelector != null) {
            // crawlBySelector(childSelector, contexts);
            log.info("##############child 있음#######################");
        }
    }

    private void crawlByExtraction(List<ExtractionDto> extractions, Map<Integer, List<SearchContext>> contexts) {
        for (ExtractionDto extraction : extractions) {
            int contextual = extraction.getContextual();
            log.info("contextual: {}", contextual);
            String name = extraction.getName();
            Boolean isMultiple = extraction.getIsMultiple();
            ExtractionType extractionType = extraction.getExtractionType();
            String attributeName = extraction.getAttributeName();
            ContainerType containerType = extraction.getContainerType();
            String containerValues = extraction.getContainerValues();

            List<SearchContext> searchContexts = contexts.get(contextual);
            log.info("searchContexts.size: {}", searchContexts.size());
            for (SearchContext searchContext : searchContexts) {
                switch (extractionType) {
                    case ExtractionType.TEXT:
                        String text = searchContext.findElement(selectBy(containerType, containerValues)).getText();
                        log.info("{}: {}", name, text);
                        break;
                    case ExtractionType.ATTRIBUTE:
                        String attribute = searchContext.findElement(selectBy(containerType, containerValues)).getAttribute(attributeName);
                        log.info("{}: {}", name, attribute);
                        break;
                }
            }
        }
    }

    private void crawlByElement(int selfContextual, ElementDto element, Map<Integer, List<SearchContext>> contexts) {
        if (element == null) {
            return;
        }

        int contextual = element.getContextual();
        Boolean isMultiple = element.getIsMultiple();
        ContainerType containerType = element.getContainerType();
        String containerValues = element.getContainerValues();
        List<SearchContext> searchContexts = contexts.get(contextual);

        List<SearchContext> newContexts = new ArrayList();
        if (isMultiple) {
            for (SearchContext searchContext : searchContexts) {
                List<SearchContext> cnts = searchContext.findElements(selectBy(containerType, containerValues)).stream()
                        .map(e -> (SearchContext) e)
                        .collect(Collectors.toList());
                newContexts.addAll(cnts);
            }
            contexts.put(selfContextual, newContexts);
        } else {
            for (SearchContext searchContext : searchContexts) {
                WebElement webElement = searchContext.findElement(selectBy(containerType, containerValues));
                newContexts.add(webElement);
            }
            contexts.put(selfContextual, newContexts);
        }
     }

     private By selectBy(ContainerType containerType, String containerValues) {
        switch (containerType) {
            case ContainerType.CSS:
                return By.cssSelector(containerValues);
        }

        throw new IllegalArgumentException("Unknown container type: " + containerType);
     }
}
