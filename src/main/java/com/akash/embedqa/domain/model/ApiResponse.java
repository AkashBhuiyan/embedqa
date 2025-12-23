package com.akash.embedqa.domain.model;

import com.akash.embedqa.domain.valueobject.Header;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Author: akash
 * Date: 23/12/25
 */
@Getter
@ToString
@EqualsAndHashCode
public class ApiResponse {

    private final int statusCode;
    private final String statusText;
    private final String body;
    private final Long bodySize;
    private final String contentType;
    private final List<Header> headers;
    private final long responseTimeMs;
    private final String requestUrl;
    private final String requestMethod;
    private final LocalDateTime timestamp;
    private final boolean success;
    private final String errorMessage;
    private final String errorType;
    private final String protocol;

    private ApiResponse(Builder builder) {
        this.statusCode = builder.statusCode;
        this.statusText = builder.statusText;
        this.body = builder.body;
        this.bodySize = builder.bodySize;
        this.contentType = builder.contentType;
        this.headers = builder.headers != null
                ? List.copyOf(builder.headers)
                : Collections.emptyList();
        this.responseTimeMs = builder.responseTimeMs;
        this.requestUrl = builder.requestUrl;
        this.requestMethod = builder.requestMethod;
        this.timestamp = builder.timestamp != null
                ? builder.timestamp
                : LocalDateTime.now();
        this.success = builder.success;
        this.errorMessage = builder.errorMessage;
        this.errorType = builder.errorType;
        this.protocol = builder.protocol;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int statusCode;
        private String statusText;
        private String body;
        private Long bodySize;
        private String contentType;
        private List<Header> headers;
        private long responseTimeMs;
        private String requestUrl;
        private String requestMethod;
        private LocalDateTime timestamp;
        private boolean success = true;
        private String errorMessage;
        private String errorType;
        private String protocol;

        public Builder statusCode(int statusCode) { this.statusCode = statusCode; return this; }
        public Builder statusText(String statusText) { this.statusText = statusText; return this; }
        public Builder body(String body) { this.body = body; return this; }
        public Builder bodySize(Long bodySize) { this.bodySize = bodySize; return this; }
        public Builder contentType(String contentType) { this.contentType = contentType; return this; }
        public Builder headers(List<Header> headers) { this.headers = headers; return this; }
        public Builder responseTimeMs(long responseTimeMs) { this.responseTimeMs = responseTimeMs; return this; }
        public Builder requestUrl(String requestUrl) { this.requestUrl = requestUrl; return this; }
        public Builder requestMethod(String requestMethod) { this.requestMethod = requestMethod; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public Builder success(boolean success) { this.success = success; return this; }
        public Builder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public Builder errorType(String errorType) { this.errorType = errorType; return this; }
        public Builder protocol(String protocol) { this.protocol = protocol; return this; }

        public ApiResponse build() {
            return new ApiResponse(this);
        }
    }
}
