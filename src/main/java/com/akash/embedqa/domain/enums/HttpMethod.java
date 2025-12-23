package com.akash.embedqa.domain.enums;

/**
 * Author: akash
 * Date: 23/12/25
 */
public enum HttpMethod {
    GET(false),
    POST(true),
    PUT(true),
    DELETE(false),
    PATCH(true),
    HEAD(false),
    OPTIONS(false);

    private final boolean supportsBody;

    HttpMethod(boolean supportsBody) {
        this.supportsBody = supportsBody;
    }

    public boolean supportsBody() {
        return supportsBody;
    }
}
