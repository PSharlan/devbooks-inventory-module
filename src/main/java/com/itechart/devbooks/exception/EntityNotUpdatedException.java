package com.itechart.devbooks.exception;

public class EntityNotUpdatedException extends RuntimeException {

    private Class notUpdatedClass;
    private long entityId;

    public EntityNotUpdatedException(Class notFoundedClass, long entityId){
        this.entityId = entityId;
        this.notUpdatedClass = notFoundedClass;
    }

    public Class getNotUpdatedClass() {
        return notUpdatedClass;
    }

    public void setNotUpdatedClass(Class notUpdatedClass) {
        this.notUpdatedClass = notUpdatedClass;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }
}
