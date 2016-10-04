package com.movilizer.connector.persistence.entities;

import com.movilitas.movilizer.v15.*;
import com.movilizer.connector.persistence.entities.listeners.ParticipantToMovilizerQueueListener;

import javax.persistence.*;
import java.util.Calendar;


@Entity
@EntityListeners(ParticipantToMovilizerQueueListener.class)
public class ParticipantToMovilizerQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Action action;

    private Calendar syncTimestamp;

    //Generic fields for participant
    private String participantKey;

    private String participantPassword;

    private Integer participantPasswordHashType;

    private String name;

    private String deviceAddress;

    private String responseQueue;

    //Generic fields for movelet assignment
    private String moveletKey;

    private String moveletKeyExtension;

    //Generic fields for movelet assignment delete
    private Boolean hardDelete;

    @Transient
    private MovilizerParticipantReset participantReset;

    @Transient
    private MovilizerMoveletAssignment assignment;

    @Transient
    private MovilizerMoveletAssignmentDelete assignmentDelete;

    @Transient
    private MovilizerParticipantConfiguration participantConfiguration;

    public ParticipantToMovilizerQueue() {
    }

    public ParticipantToMovilizerQueue(MovilizerParticipantReset participantReset) {
        action = Action.RESET;
        syncTimestamp = Calendar.getInstance();
        deviceAddress = participantReset.getDeviceAddress();

        this.participantReset = participantReset;
    }

    public ParticipantToMovilizerQueue(MovilizerMoveletAssignment assignment) {
        action = Action.ASSIGN;
        syncTimestamp = Calendar.getInstance();
        if (!assignment.getParticipant().isEmpty()) {
            MovilizerParticipant participant = assignment.getParticipant().get(0);
            participantKey = participant.getParticipantKey();
            name = participant.getName();
            deviceAddress = participant.getDeviceAddress();
            responseQueue = participant.getResponseQueue();
        }
        moveletKey = assignment.getMoveletKey();
        moveletKeyExtension = assignment.getMoveletKeyExtension();

        this.assignment = assignment;
    }

    public ParticipantToMovilizerQueue(MovilizerMoveletAssignmentDelete assignmentDelete) {
        action = Action.ASSIGN_DELETE;
        syncTimestamp = Calendar.getInstance();
        deviceAddress = assignmentDelete.getDeviceAddress();
        moveletKey = assignmentDelete.getMoveletKey();
        moveletKeyExtension = assignmentDelete.getMoveletKeyExtension();
        hardDelete = assignmentDelete.isHardDelete();

        this.assignmentDelete = assignmentDelete;
    }

    public ParticipantToMovilizerQueue(MovilizerParticipantConfiguration participantConfiguration) {
        action = Action.CONFIGURATION;
        participantPassword = participantConfiguration.getPasswordHashValue();
        participantPasswordHashType = participantConfiguration.getPasswordHashType();
        deviceAddress = participantConfiguration.getDeviceAddress();
        name = participantConfiguration.getName();

        this.participantConfiguration = participantConfiguration;
    }


    public void addToRequest(MovilizerRequest request) {
        switch (action) {
            case ASSIGN:
                request.getMoveletAssignment().add(assignment);
                break;
            case ASSIGN_DELETE:
                request.getMoveletAssignmentDelete().add(assignmentDelete);
                break;
            case RESET:
                request.getParticipantReset().add(participantReset);
            case CONFIGURATION:
                request.getParticipantConfiguration().add(participantConfiguration);
                break;
        }
    }

    @Override
    public String toString() {
        return "ParticipantToMovilizerQueue{" +
                "id=" + id +
                ", action=" + action +
                ", syncTimestamp=" + syncTimestamp +
                ", participantKey='" + participantKey + '\'' +
                ", participantPassword='" + participantPassword + '\'' +
                ", participantPasswordHashType=" + participantPasswordHashType +
                ", name='" + name + '\'' +
                ", deviceAddress='" + deviceAddress + '\'' +
                ", responseQueue='" + responseQueue + '\'' +
                ", moveletKey='" + moveletKey + '\'' +
                ", moveletKeyExtension='" + moveletKeyExtension + '\'' +
                ", hardDelete=" + hardDelete +
                ", participantReset=" + participantReset +
                ", assignment=" + assignment +
                ", assignmentDelete=" + assignmentDelete +
                ", participantConfiguration=" + participantConfiguration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParticipantToMovilizerQueue that = (ParticipantToMovilizerQueue) o;

        if (id != that.id) return false;
        if (action != that.action) return false;
        if (assignment != null ? !assignment.equals(that.assignment) : that.assignment != null) return false;
        if (assignmentDelete != null ? !assignmentDelete.equals(that.assignmentDelete) : that.assignmentDelete != null)
            return false;
        if (deviceAddress != null ? !deviceAddress.equals(that.deviceAddress) : that.deviceAddress != null)
            return false;
        if (hardDelete != null ? !hardDelete.equals(that.hardDelete) : that.hardDelete != null) return false;
        if (moveletKey != null ? !moveletKey.equals(that.moveletKey) : that.moveletKey != null) return false;
        if (moveletKeyExtension != null ? !moveletKeyExtension.equals(that.moveletKeyExtension) : that.moveletKeyExtension != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (participantConfiguration != null ? !participantConfiguration.equals(that.participantConfiguration) : that.participantConfiguration != null)
            return false;
        if (participantKey != null ? !participantKey.equals(that.participantKey) : that.participantKey != null)
            return false;
        if (participantPassword != null ? !participantPassword.equals(that.participantPassword) : that.participantPassword != null)
            return false;
        if (participantPasswordHashType != null ? !participantPasswordHashType.equals(that.participantPasswordHashType) : that.participantPasswordHashType != null)
            return false;
        if (participantReset != null ? !participantReset.equals(that.participantReset) : that.participantReset != null)
            return false;
        if (responseQueue != null ? !responseQueue.equals(that.responseQueue) : that.responseQueue != null)
            return false;
        if (syncTimestamp != null ? !syncTimestamp.equals(that.syncTimestamp) : that.syncTimestamp != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (syncTimestamp != null ? syncTimestamp.hashCode() : 0);
        result = 31 * result + (participantKey != null ? participantKey.hashCode() : 0);
        result = 31 * result + (participantPassword != null ? participantPassword.hashCode() : 0);
        result = 31 * result + (participantPasswordHashType != null ? participantPasswordHashType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (deviceAddress != null ? deviceAddress.hashCode() : 0);
        result = 31 * result + (responseQueue != null ? responseQueue.hashCode() : 0);
        result = 31 * result + (moveletKey != null ? moveletKey.hashCode() : 0);
        result = 31 * result + (moveletKeyExtension != null ? moveletKeyExtension.hashCode() : 0);
        result = 31 * result + (hardDelete != null ? hardDelete.hashCode() : 0);
        result = 31 * result + (participantReset != null ? participantReset.hashCode() : 0);
        result = 31 * result + (assignment != null ? assignment.hashCode() : 0);
        result = 31 * result + (assignmentDelete != null ? assignmentDelete.hashCode() : 0);
        result = 31 * result + (participantConfiguration != null ? participantConfiguration.hashCode() : 0);
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

    public String getParticipantKey() {
        return participantKey;
    }

    public void setParticipantKey(String participantKey) {
        this.participantKey = participantKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getResponseQueue() {
        return responseQueue;
    }

    public void setResponseQueue(String responseQueue) {
        this.responseQueue = responseQueue;
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

    public Boolean getHardDelete() {
        return hardDelete;
    }

    public void setHardDelete(Boolean hardDelete) {
        this.hardDelete = hardDelete;
    }

    public MovilizerParticipantReset getParticipantReset() {
        return participantReset;
    }

    public void setParticipantReset(MovilizerParticipantReset participantReset) {
        this.participantReset = participantReset;
    }

    public MovilizerMoveletAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(MovilizerMoveletAssignment assignment) {
        this.assignment = assignment;
    }

    public MovilizerMoveletAssignmentDelete getAssignmentDelete() {
        return assignmentDelete;
    }

    public void setAssignmentDelete(MovilizerMoveletAssignmentDelete assignmentDelete) {
        this.assignmentDelete = assignmentDelete;
    }

    public String getParticipantPassword() {
        return participantPassword;
    }

    public void setParticipantPassword(String participantPassword) {
        this.participantPassword = participantPassword;
    }

    public Integer getParticipantPasswordHashType() {
        return participantPasswordHashType;
    }

    public void setParticipantPasswordHashType(Integer participantPasswordHashType) {
        this.participantPasswordHashType = participantPasswordHashType;
    }

    public MovilizerParticipantConfiguration getParticipantConfiguration() {
        return participantConfiguration;
    }

    public void setParticipantConfiguration(MovilizerParticipantConfiguration participantConfiguration) {
        this.participantConfiguration = participantConfiguration;
    }

    public enum Action {
        ASSIGN, ASSIGN_DELETE, RESET, CONFIGURATION
    }
}
