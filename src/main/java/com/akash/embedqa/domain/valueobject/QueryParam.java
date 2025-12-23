package com.akash.embedqa.domain.valueobject;

import lombok.Getter;

import java.util.Objects;

/**
 * Author: akash
 * Date: 23/12/25
 */
public record QueryParam(
        String key,
        String value,
        boolean enabled
) {
    public QueryParam {
        Objects.requireNonNull(key, "key cannot be null");
        value = value != null ? value : "";
    }
}
