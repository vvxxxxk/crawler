package com.example.crawler.controller;

import com.example.crawler.model.dto.response.ResponseDto;
import com.example.crawler.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/crawling")
@RequiredArgsConstructor
public class CrawlingController {

    private final CrawlingService crawlingService;

    @GetMapping("/hsd/menus")
    public ResponseEntity<ResponseDto> getHsdMenus() throws InterruptedException {
        Map<String, Object> result = crawlingService.crawlingHsdMenus();
        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(result)
                .build());
    }
}
