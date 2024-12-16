package com.example.crawler.controller;

import com.example.crawler.model.dto.request.CrawlingRequestDto;
import com.example.crawler.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/crawling")
@RequiredArgsConstructor
public class CrawlingController {

    private final CrawlingService crawlingService;

    @PostMapping("/hsd")
    public ResponseEntity<?> hsdCrawling(@RequestBody CrawlingRequestDto params) {
        log.info("params: {}", params);

        crawlingService.executeCrawling(params);

        return null;
    }

}
