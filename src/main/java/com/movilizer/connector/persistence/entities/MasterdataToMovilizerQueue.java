package com.movilizer.connector.persistence.entities;

import com.movilitas.movilizer.v15.MovilizerMasterdataDelete;
import com.movilitas.movilizer.v15.MovilizerMasterdataPoolUpdate;
import com.movilitas.movilizer.v15.MovilizerMasterdataReference;
import com.movilitas.movilizer.v15.MovilizerMasterdataUpdate;
import com.movilizer.connector.persistence.entities.listeners.MasterdataToMovilizerQueueSerializerListener;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@EntityListeners(MasterdataToMovilizerQueueSerializerListener.class)
public class MasterdataToMovilizerQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Action action;

    private Calendar syncTimestamp;

    private String pool;

    //Generic fields for masterdata
    private String key;

    @Column(name = "md_group", length = 128)
    private String group;

    private String description;

    @Column(length = 100)
    private String filter1;

    @Column(length = 100)
    private String filter2;

    @Column(length = 100)
    private String filter3;

    private Long filter4;

    private Long filter5;

    private Long filter6;

    private String encryptionAlgorithm;

    private String encryptionIV;

    private String encryptionHMAC;

    private Calendar validTillDate;

    @Lob
    @Column(length = 800000)
    //TODO: check buffer enough for most datacontainers in use (specially those with pictures)
    private byte[] serializedMasterdata;

    @Transient
    private MovilizerMasterdataDelete masterdataDelete;

    @Transient
    private MovilizerMasterdataUpdate masterdataUpdate;

    @Transient
    private MovilizerMasterdataReference masterdataReference;

    public MasterdataToMovilizerQueue() {
    }

    public MasterdataToMovilizerQueue(String pool, MovilizerMasterdataDelete masterdata) {
        this.action = Action.DELETE;
        this.syncTimestamp = Calendar.getInstance();
        this.pool = pool;

        this.key = masterdata.getKey();
        this.group = masterdata.getGroup();
        this.masterdataDelete = masterdata;
    }

    public MasterdataToMovilizerQueue(String pool, MovilizerMasterdataUpdate masterdata) {
        this.action = Action.UPDATE;
        this.syncTimestamp = Calendar.getInstance();
        this.pool = pool;

        this.key = masterdata.getKey();
        this.group = masterdata.getGroup();
        this.description = masterdata.getDescription();
        this.filter1 = masterdata.getFilter1();
        this.filter2 = masterdata.getFilter2();
        this.filter3 = masterdata.getFilter3();
        this.filter4 = masterdata.getFilter4();
        this.filter5 = masterdata.getFilter5();
        this.filter6 = masterdata.getFilter6();
        this.encryptionAlgorithm = masterdata.getEncryptionAlgorithm();
        this.encryptionIV = masterdata.getEncryptionIV();
        this.encryptionHMAC = masterdata.getEncryptionHMAC();
        if (masterdata.getValidTillDate() != null) {
            this.validTillDate = masterdata.getValidTillDate().toGregorianCalendar();
        }

        this.masterdataUpdate = masterdata;
    }

    public MasterdataToMovilizerQueue(String pool, MovilizerMasterdataReference masterdata) {
        this.action = Action.REFERENCE;
        this.syncTimestamp = Calendar.getInstance();
        this.pool = pool;

        this.key = masterdata.getKey();
        this.group = masterdata.getGroup();
        if (masterdata.getValidTillDate() != null) {
            this.validTillDate = masterdata.getValidTillDate().toGregorianCalendar();
        }

        this.masterdataReference = masterdata;
    }

    public void addToPoolUpdate(MovilizerMasterdataPoolUpdate poolUpdate) {
        switch (action) {
            case DELETE:
                poolUpdate.getDelete().add(masterdataDelete);
                break;
            case UPDATE:
                poolUpdate.getUpdate().add(masterdataUpdate);
                break;
            case REFERENCE:
                poolUpdate.getReference().add(masterdataReference);
                break;
        }
    }

    @Override
    public String toString() {
        return "MaterdataToMovilizerQueue{" + "id=" + id + ", action=" + action + ", syncTimestamp=" +
                syncTimestamp + ", pool='" + pool + '\'' + ", key='" + key + '\'' + ", group='" + group +
                '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MasterdataToMovilizerQueue))
            return false;

        MasterdataToMovilizerQueue that = (MasterdataToMovilizerQueue) o;

        if (id != that.id)
            return false;
        if (action != that.action)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
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
        if (filter1 != null ? !filter1.equals(that.filter1) : that.filter1 != null)
            return false;
        if (filter2 != null ? !filter2.equals(that.filter2) : that.filter2 != null)
            return false;
        if (filter3 != null ? !filter3.equals(that.filter3) : that.filter3 != null)
            return false;
        if (filter4 != null ? !filter4.equals(that.filter4) : that.filter4 != null)
            return false;
        if (filter5 != null ? !filter5.equals(that.filter5) : that.filter5 != null)
            return false;
        if (filter6 != null ? !filter6.equals(that.filter6) : that.filter6 != null)
            return false;
        if (group != null ? !group.equals(that.group) : that.group != null)
            return false;
        if (key != null ? !key.equals(that.key) : that.key != null)
            return false;
        if (pool != null ? !pool.equals(that.pool) : that.pool != null)
            return false;
        if (syncTimestamp != null
                ? !syncTimestamp.getTime().equals(that.syncTimestamp.getTime())
                : that.syncTimestamp != null)
            return false;
        if (validTillDate != null
                ? !validTillDate.getTime().equals(that.validTillDate.getTime())
                : that.validTillDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (syncTimestamp != null ? syncTimestamp.hashCode() : 0);
        result = 31 * result + (pool != null ? pool.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (filter1 != null ? filter1.hashCode() : 0);
        result = 31 * result + (filter2 != null ? filter2.hashCode() : 0);
        result = 31 * result + (filter3 != null ? filter3.hashCode() : 0);
        result = 31 * result + (filter4 != null ? filter4.hashCode() : 0);
        result = 31 * result + (filter5 != null ? filter5.hashCode() : 0);
        result = 31 * result + (filter6 != null ? filter6.hashCode() : 0);
        result = 31 * result + (encryptionAlgorithm != null ? encryptionAlgorithm.hashCode() : 0);
        result = 31 * result + (encryptionIV != null ? encryptionIV.hashCode() : 0);
        result = 31 * result + (encryptionHMAC != null ? encryptionHMAC.hashCode() : 0);
        result = 31 * result + (validTillDate != null ? validTillDate.hashCode() : 0);
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

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilter1() {
        return filter1;
    }

    public void setFilter1(String filter1) {
        this.filter1 = filter1;
    }

    public String getFilter2() {
        return filter2;
    }

    public void setFilter2(String filter2) {
        this.filter2 = filter2;
    }

    public String getFilter3() {
        return filter3;
    }

    public void setFilter3(String filter3) {
        this.filter3 = filter3;
    }

    public Long getFilter4() {
        return filter4;
    }

    public void setFilter4(Long filter4) {
        this.filter4 = filter4;
    }

    public Long getFilter5() {
        return filter5;
    }

    public void setFilter5(Long filter5) {
        this.filter5 = filter5;
    }

    public Long getFilter6() {
        return filter6;
    }

    public void setFilter6(Long filter6) {
        this.filter6 = filter6;
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

    public byte[] getSerializedMasterdata() {
        return serializedMasterdata;
    }

    public void setSerializedMasterdata(byte[] serializedMasterdata) {
        this.serializedMasterdata = serializedMasterdata;
    }

    public MovilizerMasterdataDelete getMasterdataDelete() {
        return masterdataDelete;
    }

    public void setMasterdataDelete(MovilizerMasterdataDelete masterdataDelete) {
        this.masterdataDelete = masterdataDelete;
    }

    public MovilizerMasterdataUpdate getMasterdataUpdate() {
        return masterdataUpdate;
    }

    public void setMasterdataUpdate(MovilizerMasterdataUpdate masterdataUpdate) {
        this.masterdataUpdate = masterdataUpdate;
    }

    public MovilizerMasterdataReference getMasterdataReference() {
        return masterdataReference;
    }

    public void setMasterdataReference(MovilizerMasterdataReference masterdataReference) {
        this.masterdataReference = masterdataReference;
    }

    public enum Action {
        UPDATE, DELETE, REFERENCE
    }
}
