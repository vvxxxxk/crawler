package com.example.crawler.model.entity;

import java.util.List;

public class Topic {
    private String name;
    private String url;
    private List<Selector> selectors;
    private int timeout;
}
