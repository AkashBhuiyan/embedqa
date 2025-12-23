package com.akash.embedqa.domain.valueobject;

import java.util.Objects;

/**
 * Author: akash
 * Date: 23/12/25
 */
public record Header(
        String key,
        String value,
        boolean enabled
) {
    public Header {
        Objects.requireNonNull(key, "Header key cannot be null");
        value = value != null ? value : "";
    }

    public static Header of(String key, String value) {
        return new Header(key, value, true);
    }
}

