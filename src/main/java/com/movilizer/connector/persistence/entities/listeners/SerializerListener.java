package com.movilizer.connector.persistence.entities.listeners;

import org.nustaq.serialization.FSTConfiguration;


abstract class SerializerListener<T> {

    /**
     * Thread-safe 3r party fast serialization engine
     */
    private FSTConfiguration serializationEngine;

    public SerializerListener() {
        this.serializationEngine = FSTConfiguration.createDefaultConfiguration();
    }

    abstract public void setSerializedFieldsBeforeInsert(T queueRecord);

    abstract public void setDeserializedFieldsAfterSelect(T queueRecord);

    protected <T> byte[] getBytes(T serializableObject) {
        return serializationEngine.asByteArray(serializableObject);
    }

    protected <T> T getObject(byte[] serializedObject) {
        return (T) serializationEngine.asObject(serializedObject);
    }

}
