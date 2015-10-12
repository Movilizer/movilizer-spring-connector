package com.movilizer.connector.persistence.entities;

import java.util.Arrays;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.movilitas.movilizer.v14.MovilizerMetaMoveletReply;
import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;
import com.movilizer.connector.persistence.entities.listeners.DatacontainerFromMovilizerQueueCompressorListener;


@Entity
@EntityListeners(DatacontainerFromMovilizerQueueCompressorListener.class)
public class ReplyFromMovilizerQueue {

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
    private MovilizerMetaMoveletReply reply;

    public ReplyFromMovilizerQueue() {
    }

    public ReplyFromMovilizerQueue(MovilizerMetaMoveletReply reply) {
        this.reply = reply;

        //Fill all of the other fields
        if (reply != null) {
            key = reply.getMetaAnswerKey();
            moveletKey = reply.getMoveletKey();
            moveletKeyExtension = reply.getMoveletKeyExtension();
            moveletVersion = reply.getMoveletVersion();
            participantKey = reply.getParticipantKey();
            deviceAddress = reply.getDeviceAddress();
            if (reply.getTimestamp() != null) {
                creationTimestamp = reply.getTimestamp().toGregorianCalendar();
            }
            encryptionAlgorithm = reply.getEncryptionAlgorithm();
            encryptionIV = reply.getEncryptionIV();
            encryptionHMAC = reply.getEncryptionHMAC();
        }

    }


    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(compressedDatacontainer);
		result = prime * result + ((containerUploadPriority == null) ? 0 : containerUploadPriority.hashCode());
		result = prime * result + ((creationTimestamp == null) ? 0 : creationTimestamp.hashCode());
		result = prime * result + ((decompressedSize == null) ? 0 : decompressedSize.hashCode());
		result = prime * result + ((deviceAddress == null) ? 0 : deviceAddress.hashCode());
		result = prime * result + ((encryptionAlgorithm == null) ? 0 : encryptionAlgorithm.hashCode());
		result = prime * result + ((encryptionHMAC == null) ? 0 : encryptionHMAC.hashCode());
		result = prime * result + ((encryptionIV == null) ? 0 : encryptionIV.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((moveletKey == null) ? 0 : moveletKey.hashCode());
		result = prime * result + ((moveletKeyExtension == null) ? 0 : moveletKeyExtension.hashCode());
		result = prime * result + ((moveletVersion == null) ? 0 : moveletVersion.hashCode());
		result = prime * result + ((participantKey == null) ? 0 : participantKey.hashCode());
		result = prime * result + ((reply == null) ? 0 : reply.hashCode());
		result = prime * result + ((syncTimestamp == null) ? 0 : syncTimestamp.hashCode());
		result = prime * result + ((timedif == null) ? 0 : timedif.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReplyFromMovilizerQueue other = (ReplyFromMovilizerQueue) obj;
		if (!Arrays.equals(compressedDatacontainer, other.compressedDatacontainer))
			return false;
		if (containerUploadPriority == null) {
			if (other.containerUploadPriority != null)
				return false;
		} else if (!containerUploadPriority.equals(other.containerUploadPriority))
			return false;
		if (creationTimestamp == null) {
			if (other.creationTimestamp != null)
				return false;
		} else if (!creationTimestamp.equals(other.creationTimestamp))
			return false;
		if (decompressedSize == null) {
			if (other.decompressedSize != null)
				return false;
		} else if (!decompressedSize.equals(other.decompressedSize))
			return false;
		if (deviceAddress == null) {
			if (other.deviceAddress != null)
				return false;
		} else if (!deviceAddress.equals(other.deviceAddress))
			return false;
		if (encryptionAlgorithm == null) {
			if (other.encryptionAlgorithm != null)
				return false;
		} else if (!encryptionAlgorithm.equals(other.encryptionAlgorithm))
			return false;
		if (encryptionHMAC == null) {
			if (other.encryptionHMAC != null)
				return false;
		} else if (!encryptionHMAC.equals(other.encryptionHMAC))
			return false;
		if (encryptionIV == null) {
			if (other.encryptionIV != null)
				return false;
		} else if (!encryptionIV.equals(other.encryptionIV))
			return false;
		if (id != other.id)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (moveletKey == null) {
			if (other.moveletKey != null)
				return false;
		} else if (!moveletKey.equals(other.moveletKey))
			return false;
		if (moveletKeyExtension == null) {
			if (other.moveletKeyExtension != null)
				return false;
		} else if (!moveletKeyExtension.equals(other.moveletKeyExtension))
			return false;
		if (moveletVersion == null) {
			if (other.moveletVersion != null)
				return false;
		} else if (!moveletVersion.equals(other.moveletVersion))
			return false;
		if (participantKey == null) {
			if (other.participantKey != null)
				return false;
		} else if (!participantKey.equals(other.participantKey))
			return false;
		if (reply == null) {
			if (other.reply != null)
				return false;
		} else if (!reply.equals(other.reply))
			return false;
		if (syncTimestamp == null) {
			if (other.syncTimestamp != null)
				return false;
		} else if (!syncTimestamp.equals(other.syncTimestamp))
			return false;
		if (timedif == null) {
			if (other.timedif != null)
				return false;
		} else if (!timedif.equals(other.timedif))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReplyFromMovilizerQueue [id=" + id + ", containerUploadPriority=" + containerUploadPriority + ", key="
				+ key + ", moveletKey=" + moveletKey + ", moveletKeyExtension=" + moveletKeyExtension
				+ ", moveletVersion=" + moveletVersion + ", participantKey=" + participantKey + ", deviceAddress="
				+ deviceAddress + ", creationTimestamp=" + creationTimestamp + ", syncTimestamp=" + syncTimestamp
				+ ", timedif=" + timedif + ", encryptionAlgorithm=" + encryptionAlgorithm + ", encryptionIV="
				+ encryptionIV + ", encryptionHMAC=" + encryptionHMAC + ", compressedDatacontainer="
				+ Arrays.toString(compressedDatacontainer) + ", decompressedSize=" + decompressedSize + ", reply="
				+ reply + "]";
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

	public MovilizerMetaMoveletReply getReply() {
		return reply;
	}

	public void setReply(MovilizerMetaMoveletReply reply) {
		this.reply = reply;
	}

}