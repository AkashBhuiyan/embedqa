package com.akash.embedqa.domain.enums;

/**
 * Author: akash
 * Date: 23/12/25
 */
public enum BodyType {
    NONE(""),
    JSON("application/json"),
    XML("application/xml"),
    FORM_DATA("application/x-www-form-urlencoded"),
    RAW("text/plain"),
    BINARY("application/octet-stream");

    private final String contentType;

    BodyType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
