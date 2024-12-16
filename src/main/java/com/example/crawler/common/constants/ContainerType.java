package com.example.crawler.common.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContainerType {
    CSS("css");

    private String name;

    ContainerType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static ContainerType fromValue(String value) {
        for (ContainerType type : values()) {
            if (type.name.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ContainerType: " + value);
    }

    @JsonValue
    public String toValue() {
        return name;
    }
}
