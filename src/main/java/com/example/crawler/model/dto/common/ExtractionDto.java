package com.example.crawler.model.dto.common;

import com.example.crawler.common.constants.ContainerType;
import com.example.crawler.common.constants.ExtractionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtractionDto {
    private String name;
    private int contextual;
    private Boolean isMultiple;
    private ExtractionType extractionType;
    private String attributeName;
    private ContainerType containerType;
    private String containerValues;
}
