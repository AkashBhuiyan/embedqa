package com.akash.embedqa.domain.model;

import com.akash.embedqa.model.entities.ApiRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Author: akash
 * Date: 23/12/25
 */
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Collection {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String description;
    private final List<ApiRequest> requests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor - use factory methods
    private Collection(
            Long id,
            String name,
            String description,
            List<ApiRequest> requests,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = description;
        this.requests = requests != null ? new ArrayList<>(requests) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory: Create new collection
    public static Collection create(String name, String description) {
        validateName(name);
        LocalDateTime now = LocalDateTime.now();
        return new Collection(null, name, description, new ArrayList<>(), now, now);
    }

    // Factory: Reconstitute from database
    public static Collection reconstitute(
            Long id,
            String name,
            String description,
            List<ApiRequest> requests,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Collection(id, name, description, requests, createdAt, updatedAt);
    }

    // Business methods
    public void rename(String newName) {
        validateName(newName);
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void addRequest(ApiRequest request) {
        Objects.requireNonNull(request);
        this.requests.add(request);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeRequest(ApiRequest request) {
        this.requests.remove(request);
        this.updatedAt = LocalDateTime.now();
    }

    public int getRequestCount() {
        return requests.size();
    }

    public List<ApiRequest> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("ID already assigned");
        }
        this.id = id;
    }

    // Validation
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Collection name cannot be empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Collection name cannot exceed 255 characters");
        }
    }
}
