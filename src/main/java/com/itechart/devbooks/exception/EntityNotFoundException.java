package com.itechart.devbooks.exception;

public class EntityNotFoundException extends RuntimeException {

    private Class notFoundedClass;
    private long entityId;

    public EntityNotFoundException(Class notFoundedClass, long entityId){
        this.entityId = entityId;
        this.notFoundedClass = notFoundedClass;
    }

    public Class getNotFoundedClass() {
        return notFoundedClass;
    }

    public void setNotFoundedClass(Class notFoundedClass) {
        this.notFoundedClass = notFoundedClass;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }
}
