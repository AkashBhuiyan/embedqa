package com.akash.embedqa.domain.model;

import com.akash.embedqa.domain.valueobject.EnvironmentVariable;
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
public class Environment {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String description;
    private boolean active;
    private final List<EnvironmentVariable> variables;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Environment(
            Long id,
            String name,
            String description,
            boolean active,
            List<EnvironmentVariable> variables,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = description;
        this.active = active;
        this.variables = variables != null ? new ArrayList<>(variables) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory methods
    public static Environment create(String name, String description) {
        LocalDateTime now = LocalDateTime.now();
        return new Environment(null, name, description, false, new ArrayList<>(), now, now);
    }

    public static Environment reconstitute(
            Long id,
            String name,
            String description,
            boolean active,
            List<EnvironmentVariable> variables,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Environment(id, name, description, active, variables, createdAt, updatedAt);
    }

    // Business methods
    public void rename(String newName) {
        this.name = Objects.requireNonNull(newName, "Name cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void setVariables(List<EnvironmentVariable> newVariables) {
        this.variables.clear();
        if (newVariables != null) {
            this.variables.addAll(newVariables);
        }
        this.updatedAt = LocalDateTime.now();
    }

    public Map<String, String> getVariablesAsMap() {
        Map<String, String> map = new HashMap<>();
        for (EnvironmentVariable var : variables) {
            if (var.enabled()) {
                map.put(var.name(), var.value());
            }
        }
        return Collections.unmodifiableMap(map);
    }

    public List<EnvironmentVariable> getVariables() {
        return Collections.unmodifiableList(variables);
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("ID already assigned");
        }
        this.id = id;
    }
}
