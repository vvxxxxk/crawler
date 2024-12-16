package com.example.crawler.model.entity;

import com.example.crawler.common.constants.ContainerType;
import com.example.crawler.common.constants.ExtractionType;

public class Extraction {
    private String name;
    private int contextual;
    private Boolean isMultiple;
    private ExtractionType extractionType;
    private ContainerType containerType;
    private String containerValues;
}
