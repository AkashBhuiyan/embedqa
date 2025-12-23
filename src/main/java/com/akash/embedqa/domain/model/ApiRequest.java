package com.akash.embedqa.domain.model;

import com.akash.embedqa.domain.enums.AuthType;
import com.akash.embedqa.domain.enums.BodyType;
import com.akash.embedqa.domain.enums.HttpMethod;
import com.akash.embedqa.domain.valueobject.AuthConfig;
import com.akash.embedqa.domain.valueobject.Header;
import com.akash.embedqa.domain.valueobject.QueryParam;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Author: akash
 * Date: 23/12/25
 */

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApiRequest {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String url;
    private HttpMethod method;
    private String description;
    private final List<Header> headers;
    private final List<QueryParam> queryParams;
    private String body;
    private BodyType bodyType;
    private AuthType authType;
    private AuthConfig authConfig;
    private Long collectionId;
    private Long environmentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ApiRequest(Builder builder) {
        this.id = builder.id;
        this.name = Objects.requireNonNull(builder.name, "name cannot be null");
        this.url = Objects.requireNonNull(builder.url, "url cannot be null");
        this.method = Objects.requireNonNull(builder.method, "method cannot be null");
        this.description = builder.description;
        this.headers = builder.headers != null ? new ArrayList<>(builder.headers) : new ArrayList<>();
        this.queryParams = builder.queryParams != null ? new ArrayList<>(builder.queryParams) : new ArrayList<>();
        this.body = builder.body;
        this.bodyType = builder.bodyType != null ? builder.bodyType : BodyType.NONE;
        this.authType = builder.authType != null ? builder.authType : AuthType.NONE;
        this.authConfig = builder.authConfig;
        this.collectionId = builder.collectionId;
        this.environmentId = builder.environmentId;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    // Business methods
    public void updateUrl(String newUrl) {
        this.url = Objects.requireNonNull(newUrl);
        this.updatedAt = LocalDateTime.now();
    }

    public void setHeaders(List<Header> headers) {
        this.headers.clear();
        if (headers != null) this.headers.addAll(headers);
        this.updatedAt = LocalDateTime.now();
    }

    public void setQueryParams(List<QueryParam> params) {
        this.queryParams.clear();
        if (params != null) this.queryParams.addAll(params);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateBody(String body, BodyType bodyType) {
        this.body = body;
        this.bodyType = bodyType != null ? bodyType : BodyType.NONE;
        this.updatedAt = LocalDateTime.now();
    }

    public void configureAuth(AuthType authType, AuthConfig authConfig) {
        this.authType = authType != null ? authType : AuthType.NONE;
        this.authConfig = authConfig;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasBody() {
        return method.supportsBody() && body != null && !body.isEmpty();
    }

    public List<Header> getEnabledHeaders() {
        return headers.stream().filter(Header::enabled).toList();
    }

    public List<QueryParam> getEnabledQueryParams() {
        return queryParams.stream().filter(QueryParam::enabled).toList();
    }

    public List<Header> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    public List<QueryParam> getQueryParams() {
        return Collections.unmodifiableList(queryParams);
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("ID already assigned");
        }
        this.id = id;
    }

    // Custom Builder (keep this!)
    public static class Builder {
        private Long id;
        private String name;
        private String url;
        private HttpMethod method = HttpMethod.GET;
        private String description;
        private List<Header> headers;
        private List<QueryParam> queryParams;
        private String body;
        private BodyType bodyType;
        private AuthType authType;
        private AuthConfig authConfig;
        private Long collectionId;
        private Long environmentId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder url(String url) { this.url = url; return this; }
        public Builder method(HttpMethod method) { this.method = method; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder headers(List<Header> headers) { this.headers = headers; return this; }
        public Builder queryParams(List<QueryParam> queryParams) { this.queryParams = queryParams; return this; }
        public Builder body(String body) { this.body = body; return this; }
        public Builder bodyType(BodyType bodyType) { this.bodyType = bodyType; return this; }
        public Builder authType(AuthType authType) { this.authType = authType; return this; }
        public Builder authConfig(AuthConfig authConfig) { this.authConfig = authConfig; return this; }
        public Builder collectionId(Long collectionId) { this.collectionId = collectionId; return this; }
        public Builder environmentId(Long environmentId) { this.environmentId = environmentId; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ApiRequest build() {
            return new ApiRequest(this);
        }
    }
}

