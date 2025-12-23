package com.akash.embedqa.domain.exception;

/**
 * Author: akash
 * Date: 23/12/25
 */
public class EntityNotFoundException extends DomainException {
    private final String entityName;
    private final Object entityId;

    public EntityNotFoundException(String entityName, Object entityId) {
        super(String.format("%s not found with id: %s", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public String getEntityName() { return entityName; }
    public Object getEntityId() { return entityId; }
}
