package com.example.crawler.model.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectorDto {
    private int selfContextual;
    private ElementDto element;
    private LinkDto link;
    private List<ExtractionDto> extractions;
    private SelectorDto child;
}
