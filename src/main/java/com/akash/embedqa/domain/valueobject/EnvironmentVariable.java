package com.akash.embedqa.domain.valueobject;

import java.util.Objects;

/**
 * Author: akash
 * Date: 23/12/25
 */

public record EnvironmentVariable(
        String name,
        String value,
        boolean enabled,
        boolean secret
) {
    public EnvironmentVariable {
        Objects.requireNonNull(name, "name cannot be null");
    }
}
