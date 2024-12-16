package com.example.crawler.model.dto.common;

import com.example.crawler.common.constants.ContainerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto {
    private int contextual;
    private Boolean isMultiple;
    private ContainerType containerType;
    private String containerValues;
}
