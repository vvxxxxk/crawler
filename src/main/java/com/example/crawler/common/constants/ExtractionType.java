package com.example.crawler.common.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExtractionType {

    TEXT("text"),
    ATTRIBUTE("attribute");

    private String name;

    ExtractionType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static ExtractionType fromValue(String value) {
        for (ExtractionType type : values()) {
            if (type.name.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ExtractionType: " + value);
    }

    @JsonValue
    public String toValue() {
        return name;
    }
}

