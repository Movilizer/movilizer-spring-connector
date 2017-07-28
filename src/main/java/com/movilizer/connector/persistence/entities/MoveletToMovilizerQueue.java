package com.movilizer.connector.persistence.entities;

import com.movilitas.movilizer.v15.*;
import com.movilizer.connector.persistence.entities.listeners.MoveletToMovilizerQueueSerializerListener;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@EntityListeners(MoveletToMovilizerQueueSerializerListener.class)
public class MoveletToMovilizerQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Action action;

    private Calendar syncTimestamp;

    //Generic fields for masterdata
    private String moveletKey;

    private String moveletKeyExtension;

    private Long moveletVersion;

    private String name;

    private String moveletType;

    private String namespace;

    private Boolean privateNamespace;

    private Boolean transactional;

    private Long priority;

    private Integer appGroup;

    private Boolean visible;

    private String encryptionAlgorithm;

    private String encryptionIV;

    private String encryptionHMAC;

    private Calendar validTillDate;

    private Boolean ignoreExtensionKey;

    @Lob
    @Column(length = 800000)
    //TODO: check buffer enough for most datacontainers in use (specially those with pictures)
    private byte[] serializedMovelet;

    @Transient
    private MovilizerMovelet movelet;

    @Transient
    private MovilizerMoveletDelete moveletDelete;

    public MoveletToMovilizerQueue() {
    }

    public MoveletToMovilizerQueue(MovilizerMovelet movelet) {
        action = Action.UPDATE;
        syncTimestamp = Calendar.getInstance();
        moveletKey = movelet.getMoveletKey();
        moveletKeyExtension = movelet.getMoveletKeyExtension();
        moveletVersion = movelet.getMoveletVersion();
        name = movelet.getName();
        moveletType = movelet.getMoveletType().toString();
        namespace = movelet.getNamespace();
        privateNamespace = movelet.isPrivateNamespace();
        transactional = movelet.isTransactional();
        priority = movelet.getPriority();
        appGroup = movelet.getAppGroup();
        visible = movelet.isVisible();
        encryptionAlgorithm = movelet.getEncryptionAlgorithm();
        encryptionIV = movelet.getEncryptionIV();
        encryptionHMAC = movelet.getEncryptionHMAC();
        if (movelet.getValidTillDate() != null) {
            validTillDate = movelet.getValidTillDate().toGregorianCalendar();
        }

        this.movelet = movelet;
    }

    public MoveletToMovilizerQueue(MovilizerMoveletDelete moveletDelete) {
        action = Action.DELETE;
        syncTimestamp = Calendar.getInstance();
        moveletKey = moveletDelete.getMoveletKey();
        moveletKeyExtension = moveletDelete.getMoveletKeyExtension();
        ignoreExtensionKey = moveletDelete.isIgnoreExtensionKey();

        this.moveletDelete = moveletDelete;
    }

    public void addToRequest(MovilizerRequest request) {
        switch (action) {
            case DELETE:
                request.getMoveletDelete().add(moveletDelete);
                break;
            case UPDATE:
                List<MovilizerMoveletSet> sets = request.getMoveletSet();
                if (sets.isEmpty()) {
                    sets.add(new MovilizerMoveletSet());
                }
                request.getMoveletSet().get(0).getMovelet().add(movelet);
                break;
        }
    }

    @Override
    public String toString() {
        return "MoveletToMovilizerQueue{" + "id=" + id + ", action=" + action + ", syncTimestamp=" +
                syncTimestamp + ", moveletKey='" + moveletKey + '\'' + ", moveletKeyExtension='" +
                moveletKeyExtension + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MoveletToMovilizerQueue))
            return false;

        MoveletToMovilizerQueue that = (MoveletToMovilizerQueue) o;

        if (id != that.id)
            return false;
        if (action != that.action)
            return false;
        if (appGroup != null ? !appGroup.equals(that.appGroup) : that.appGroup != null)
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
        if (ignoreExtensionKey != null
                ? !ignoreExtensionKey.equals(that.ignoreExtensionKey)
                : that.ignoreExtensionKey != null)
            return false;
        if (moveletKey != null ? !moveletKey.equals(that.moveletKey) : that.moveletKey != null)
            return false;
        if (moveletKeyExtension != null
                ? !moveletKeyExtension.equals(that.moveletKeyExtension)
                : that.moveletKeyExtension != null)
            return false;
        if (moveletType != null ? !moveletType.equals(that.moveletType) : that.moveletType != null)
            return false;
        if (moveletVersion != null
                ? !moveletVersion.equals(that.moveletVersion)
                : that.moveletVersion != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (namespace != null ? !namespace.equals(that.namespace) : that.namespace != null)
            return false;
        if (priority != null ? !priority.equals(that.priority) : that.priority != null)
            return false;
        if (privateNamespace != null
                ? !privateNamespace.equals(that.privateNamespace)
                : that.privateNamespace != null)
            return false;
        if (syncTimestamp != null
                ? !syncTimestamp.equals(that.syncTimestamp)
                : that.syncTimestamp != null)
            return false;
        if (transactional != null
                ? !transactional.equals(that.transactional)
                : that.transactional != null)
            return false;
        if (validTillDate != null
                ? !validTillDate.equals(that.validTillDate)
                : that.validTillDate != null)
            return false;
        if (visible != null ? !visible.equals(that.visible) : that.visible != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (syncTimestamp != null ? syncTimestamp.hashCode() : 0);
        result = 31 * result + (moveletKey != null ? moveletKey.hashCode() : 0);
        result = 31 * result + (moveletKeyExtension != null ? moveletKeyExtension.hashCode() : 0);
        result = 31 * result + (moveletVersion != null ? moveletVersion.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (moveletType != null ? moveletType.hashCode() : 0);
        result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
        result = 31 * result + (privateNamespace != null ? privateNamespace.hashCode() : 0);
        result = 31 * result + (transactional != null ? transactional.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (appGroup != null ? appGroup.hashCode() : 0);
        result = 31 * result + (visible != null ? visible.hashCode() : 0);
        result = 31 * result + (encryptionAlgorithm != null ? encryptionAlgorithm.hashCode() : 0);
        result = 31 * result + (encryptionIV != null ? encryptionIV.hashCode() : 0);
        result = 31 * result + (encryptionHMAC != null ? encryptionHMAC.hashCode() : 0);
        result = 31 * result + (validTillDate != null ? validTillDate.hashCode() : 0);
        result = 31 * result + (ignoreExtensionKey != null ? ignoreExtensionKey.hashCode() : 0);
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Calendar getSyncTimestamp() {
        return syncTimestamp;
    }

    public void setSyncTimestamp(Calendar syncTimestamp) {
        this.syncTimestamp = syncTimestamp;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoveletType() {
        return moveletType;
    }

    public void setMoveletType(String moveletType) {
        this.moveletType = moveletType;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Boolean getPrivateNamespace() {
        return privateNamespace;
    }

    public void setPrivateNamespace(Boolean privateNamespace) {
        this.privateNamespace = privateNamespace;
    }

    public Boolean getTransactional() {
        return transactional;
    }

    public void setTransactional(Boolean transactional) {
        this.transactional = transactional;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Integer getAppGroup() {
        return appGroup;
    }

    public void setAppGroup(Integer appGroup) {
        this.appGroup = appGroup;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
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

    public Calendar getValidTillDate() {
        return validTillDate;
    }

    public void setValidTillDate(Calendar validTillDate) {
        this.validTillDate = validTillDate;
    }

    public Boolean getIgnoreExtensionKey() {
        return ignoreExtensionKey;
    }

    public void setIgnoreExtensionKey(Boolean ignoreExtensionKey) {
        this.ignoreExtensionKey = ignoreExtensionKey;
    }

    public byte[] getSerializedMovelet() {
        return serializedMovelet;
    }

    public void setSerializedMovelet(byte[] serializedMovelet) {
        this.serializedMovelet = serializedMovelet;
    }

    public MovilizerMovelet getMovelet() {
        return movelet;
    }

    public void setMovelet(MovilizerMovelet movelet) {
        this.movelet = movelet;
    }

    public MovilizerMoveletDelete getMoveletDelete() {
        return moveletDelete;
    }

    public void setMoveletDelete(MovilizerMoveletDelete moveletDelete) {
        this.moveletDelete = moveletDelete;
    }

    public enum Action {
        UPDATE, DELETE
    }
}
