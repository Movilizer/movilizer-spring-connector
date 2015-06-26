package com.movilizer.connector.v12.persistence.entities;


import com.movilitas.movilizer.v12.MovilizerUploadDataContainer;
import com.movilizer.connector.v12.persistence.entities.listeners.DatacontainerFromMovilizerQueueCompressorListener;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@EntityListeners(DatacontainerFromMovilizerQueueCompressorListener.class)
public class DatacontainerFromMovilizerQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Short containerUploadPriority;

    //Generic fields for data container
    private String key;

    private String moveletKey;

    private String moveletKeyExtension;

    private Long moveletVersion;

    private String participantKey;

    private String deviceAddress;

    private Calendar creationTimestamp;

    private Calendar syncTimestamp;

    private Integer timedif;

    private String encryptionAlgorithm;

    private String encryptionIV;

    private String encryptionHMAC;

    @Lob
    @Column(length = 800000)
    //TODO: check buffer enough for most datacontainers in use (specially those with pictures)
    private byte[] compressedDatacontainer;

    private Integer decompressedSize;

    @Transient
    private MovilizerUploadDataContainer datacontainer;

    public DatacontainerFromMovilizerQueue() {
    }

    public DatacontainerFromMovilizerQueue(MovilizerUploadDataContainer datacontainer) {
        this.datacontainer = datacontainer;

        //Fill all of the other fields
        containerUploadPriority = datacontainer.getContainerUploadPriority();
        if (datacontainer.getContainer() != null) {
            key = datacontainer.getContainer().getKey();
            moveletKey = datacontainer.getContainer().getMoveletKey();
            moveletKeyExtension = datacontainer.getContainer().getMoveletKeyExtension();
            moveletVersion = datacontainer.getContainer().getMoveletVersion();
            participantKey = datacontainer.getContainer().getParticipantKey();
            deviceAddress = datacontainer.getContainer().getDeviceAddress();
            if (datacontainer.getContainer().getCreationTimestamp() != null) {
                creationTimestamp = datacontainer.getContainer().getCreationTimestamp().toGregorianCalendar();
            }
            if (datacontainer.getContainer().getSyncTimestamp() != null) {
                syncTimestamp = datacontainer.getContainer().getSyncTimestamp().toGregorianCalendar();
            }
            timedif = datacontainer.getContainer().getTimedif();
            encryptionAlgorithm = datacontainer.getContainer().getEncryptionAlgorithm();
            encryptionIV = datacontainer.getContainer().getEncryptionIV();
            encryptionHMAC = datacontainer.getContainer().getEncryptionHMAC();
        }

    }

    @Override
    public String toString() {
        return "DatacontainerFromMovilizerQueue{" + "id=" + id + ", containerUploadPriority=" +
                containerUploadPriority + ", key='" + key + '\'' + ", moveletKey='" + moveletKey + '\'' +
                ", moveletKeyExtension='" + moveletKeyExtension + '\'' + ", moveletVersion=" +
                moveletVersion + ", participantKey='" + participantKey + '\'' + ", deviceAddress='" +
                deviceAddress + '\'' + ", creationTimestamp=" + creationTimestamp + ", syncTimestamp=" +
                syncTimestamp + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DatacontainerFromMovilizerQueue))
            return false;

        DatacontainerFromMovilizerQueue that = (DatacontainerFromMovilizerQueue) o;

        if (id != that.id)
            return false;
        if (containerUploadPriority != null
                ? !containerUploadPriority.equals(that.containerUploadPriority)
                : that.containerUploadPriority != null)
            return false;
        if (creationTimestamp != null ? !creationTimestamp.getTime().equals(
                that.creationTimestamp.getTime()) : that.creationTimestamp != null)
            return false;
        if (deviceAddress != null
                ? !deviceAddress.equals(that.deviceAddress)
                : that.deviceAddress != null)
            return false;
        if (encryptionAlgorithm != null
                ? !encryptionAlgorithm.equals(that.encryptionAlgorithm)
                : that.encryptionAlgorithm != null)
            return false;
        if (encryptionHMAC != null
                ? !encryptionHMAC.equals(that.encryptionHMAC)
                : that.encryptionHMAC != null)
            return false;
        if (encryptionIV != null ? !encryptionIV.equals(that.encryptionIV) : that.encryptionIV != null)
            return false;
        if (key != null ? !key.equals(that.key) : that.key != null)
            return false;
        if (moveletKey != null ? !moveletKey.equals(that.moveletKey) : that.moveletKey != null)
            return false;
        if (moveletKeyExtension != null
                ? !moveletKeyExtension.equals(that.moveletKeyExtension)
                : that.moveletKeyExtension != null)
            return false;
        if (moveletVersion != null
                ? !moveletVersion.equals(that.moveletVersion)
                : that.moveletVersion != null)
            return false;
        if (participantKey != null
                ? !participantKey.equals(that.participantKey)
                : that.participantKey != null)
            return false;
        if (syncTimestamp != null
                ? !syncTimestamp.getTime().equals(that.syncTimestamp.getTime())
                : that.syncTimestamp != null)
            return false;
        if (timedif != null ? !timedif.equals(that.timedif) : that.timedif != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result +
                (containerUploadPriority != null ? containerUploadPriority.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (moveletKey != null ? moveletKey.hashCode() : 0);
        result = 31 * result + (moveletKeyExtension != null ? moveletKeyExtension.hashCode() : 0);
        result = 31 * result + (moveletVersion != null ? moveletVersion.hashCode() : 0);
        result = 31 * result + (participantKey != null ? participantKey.hashCode() : 0);
        result = 31 * result + (deviceAddress != null ? deviceAddress.hashCode() : 0);
        result = 31 * result + (creationTimestamp != null ? creationTimestamp.hashCode() : 0);
        result = 31 * result + (syncTimestamp != null ? syncTimestamp.hashCode() : 0);
        result = 31 * result + (timedif != null ? timedif.hashCode() : 0);
        result = 31 * result + (encryptionAlgorithm != null ? encryptionAlgorithm.hashCode() : 0);
        result = 31 * result + (encryptionIV != null ? encryptionIV.hashCode() : 0);
        result = 31 * result + (encryptionHMAC != null ? encryptionHMAC.hashCode() : 0);
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Short getContainerUploadPriority() {
        return containerUploadPriority;
    }

    public void setContainerUploadPriority(Short containerUploadPriority) {
        this.containerUploadPriority = containerUploadPriority;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMoveletKey() {
        return moveletKey;
    }

    public void setMoveletKey(String moveletKey) {
        this.moveletKey = moveletKey;
    }

    public String getMoveletKeyExtension() {
        return moveletKeyExtension;
    }

    public void setMoveletKeyExtension(String moveletKeyExtension) {
        this.moveletKeyExtension = moveletKeyExtension;
    }

    public Long getMoveletVersion() {
        return moveletVersion;
    }

    public void setMoveletVersion(Long moveletVersion) {
        this.moveletVersion = moveletVersion;
    }

    public String getParticipantKey() {
        return participantKey;
    }

    public void setParticipantKey(String participantKey) {
        this.participantKey = participantKey;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public Calendar getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Calendar creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Calendar getSyncTimestamp() {
        return syncTimestamp;
    }

    public void setSyncTimestamp(Calendar syncTimestamp) {
        this.syncTimestamp = syncTimestamp;
    }

    public Integer getTimedif() {
        return timedif;
    }

    public void setTimedif(Integer timedif) {
        this.timedif = timedif;
    }

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public String getEncryptionIV() {
        return encryptionIV;
    }

    public void setEncryptionIV(String encryptionIV) {
        this.encryptionIV = encryptionIV;
    }

    public String getEncryptionHMAC() {
        return encryptionHMAC;
    }

    public void setEncryptionHMAC(String encryptionHMAC) {
        this.encryptionHMAC = encryptionHMAC;
    }

    public byte[] getCompressedDatacontainer() {
        return compressedDatacontainer;
    }

    public void setCompressedDatacontainer(byte[] compressedDatacontainer) {
        this.compressedDatacontainer = compressedDatacontainer;
    }

    public Integer getDecompressedSize() {
        return decompressedSize;
    }

    public void setDecompressedSize(Integer decompressedSize) {
        this.decompressedSize = decompressedSize;
    }

    public MovilizerUploadDataContainer getDatacontainer() {
        return datacontainer;
    }

    public void setDatacontainer(MovilizerUploadDataContainer datacontainer) {
        this.datacontainer = datacontainer;
    }
}
