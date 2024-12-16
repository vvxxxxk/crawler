package com.example.crawler.model.dto.request;

import com.example.crawler.model.dto.common.TopicDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlingRequestDto {
    List<TopicDto> topics;
}
