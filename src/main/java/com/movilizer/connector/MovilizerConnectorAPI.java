package com.movilizer.connector;


import com.movilitas.movilizer.v15.*;
import com.movilizer.connector.jobs.PollingJob;
import com.movilizer.connector.model.Processor;
import com.movilizer.connector.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.persistence.entities.MoveletToMovilizerQueue;
import com.movilizer.connector.persistence.entities.ParticipantToMovilizerQueue;
import com.movilizer.connector.model.MovilizerCallback;
import com.movilizer.connector.queues.FromMovilizerQueueService;
import com.movilizer.connector.queues.ToMovilizerQueueService;
import com.movilizer.mds.webservice.models.PasswordHashTypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Component
public class MovilizerConnectorAPI {
    private static Log logger = LogFactory.getLog(MovilizerConnectorAPI.class);

    private PollingJob pollingJob;
    private ToMovilizerQueueService toMovilizerQueueService;
    private FromMovilizerQueueService fromMovilizerQueueService;

    @Autowired
    public MovilizerConnectorAPI(PollingJob pollingJob, ToMovilizerQueueService toMovilizerQueueService, FromMovilizerQueueService fromMovilizerQueueService) {
        this.pollingJob = pollingJob;
        this.toMovilizerQueueService = toMovilizerQueueService;
        this.fromMovilizerQueueService = fromMovilizerQueueService;
    }

    /**
     * sync method that synchronizes all the queued messages to the cloud and also gets the responses.
     */
    public void sync()
    {
    	pollingJob.performSyncToCloud();
    }

    /**
     *  Fired once per response
     */
    public void registerCallback(MovilizerCallback callback) {
        pollingJob.registerCallback(callback);
    }

    /**
     * Fired once per instance of T in the response
     */
    public <T> void registerProcessor(Processor<T> processor, Class<T> processorClass) {
        pollingJob.registerDownloadProcessor(processor, processorClass);
    }

    public Calendar getLastSync() {
        return pollingJob.getLastSync();
    }

    @Transactional
    public void createMovelet(MovilizerMovelet movelet) {
        boolean addedToQueue = toMovilizerQueueService.offer(new MoveletToMovilizerQueue(movelet));
        if (!addedToQueue) {
            logger.info(String.format("Movelet with key %s not added to queue", movelet.getMoveletKey()));
        }
    }

    @Transactional
    public void assignMoveletToParticipant(String moveletKey, MovilizerParticipant participant) {
      assignMoveletToParticipant(moveletKey, "", participant);
    }

    @Transactional
    public void assignMoveletToParticipant(String moveletKey, String moveletKeyExtension, MovilizerParticipant participant) {
        MovilizerMoveletAssignment assignment = new MovilizerMoveletAssignment();
        assignment.setMoveletKey(moveletKey);
        assignment.setMoveletKeyExtension(moveletKeyExtension);
        assignment.getParticipant().add(participant);
        boolean addedToQueue = toMovilizerQueueService.offer(new ParticipantToMovilizerQueue(assignment));
        if (!addedToQueue) {
            logger.info(String.format("Assigment for participant %s to movelet %s not added to queue",
                    participant.getDeviceAddress(), moveletKey));
        }
    }

    @Transactional
    public void assignPasswordToParticipant(MovilizerParticipant participant, PasswordHashTypes passwordType, String password) {
        MovilizerParticipantConfiguration movilizerParticipantConf = new MovilizerParticipantConfiguration();
        movilizerParticipantConf.setDeviceAddress(participant.getDeviceAddress());
        movilizerParticipantConf.setPasswordHashType(passwordType.getValue());
        movilizerParticipantConf.setPasswordHashValue(password);

        assignConfigurationToParticipant(movilizerParticipantConf);
    }

    @Transactional
    public void assignConfigurationToParticipant(MovilizerParticipantConfiguration movilizerParticipantConf) {
        boolean addedToQueue = toMovilizerQueueService.offer(new ParticipantToMovilizerQueue(movilizerParticipantConf));
        if (!addedToQueue) {
            logger.info(String.format("Configuration for participant %s with password %s not added to queue",
                    movilizerParticipantConf.getDeviceAddress(), movilizerParticipantConf.getPasswordHashValue()));
        }
    }

    @Transactional
    public void unassignMoveletToParticipant(String moveletKey, MovilizerParticipant participant) {
        MovilizerMoveletAssignmentDelete unassignment = new MovilizerMoveletAssignmentDelete();
        unassignment.setMoveletKey(moveletKey);
        unassignment.setDeviceAddress(participant.getDeviceAddress());
        boolean addedToQueue = toMovilizerQueueService.offer(new ParticipantToMovilizerQueue(
                unassignment));
        if (!addedToQueue) {
            logger.info(String.format("Assigment for participant %s to movelet %s not added to queue",
                    participant.getDeviceAddress(), moveletKey));
        }
    }

    @Transactional
    public void updateMasterdata(Collection<MovilizerMasterdataPoolUpdate> masterdataList) {
        for (MovilizerMasterdataPoolUpdate masterdataPoolUpdate : masterdataList) {
            updateMasterdata(masterdataPoolUpdate);
        }
    }

    @Transactional
    public void updateMasterdata(MovilizerMasterdataPoolUpdate masterdataPoolUpdate) {
        for (MovilizerMasterdataUpdate masterdataUpdate : masterdataPoolUpdate.getUpdate()) {
            boolean addedToQueue = toMovilizerQueueService.offer(new MasterdataToMovilizerQueue(
                    masterdataPoolUpdate.getPool(), masterdataUpdate));
            if (!addedToQueue) {
                logger.info(String.format("Masterdata update for pool %s with key %s not added to queue",
                        masterdataPoolUpdate.getPool(), masterdataUpdate.getKey()));
            }
        }

        for (MovilizerMasterdataDelete masterdataDelete : masterdataPoolUpdate.getDelete()) {
            boolean addedToQueue = toMovilizerQueueService.offer(new MasterdataToMovilizerQueue(
                    masterdataPoolUpdate.getPool(), masterdataDelete));
            if (!addedToQueue) {
                logger.info(String.format("Masterdata delete for pool %s with key %s not added to queue",
                        masterdataPoolUpdate.getPool(), masterdataDelete.getKey()));
            }
        }

        for (MovilizerMasterdataReference masterdataReference : masterdataPoolUpdate.getReference()) {
            boolean addedToQueue = toMovilizerQueueService.offer(new MasterdataToMovilizerQueue(
                    masterdataPoolUpdate.getPool(), masterdataReference));
            if (!addedToQueue) {
                logger.info(String.format(
                        "Masterdata reference for pool %s with key %s not added to queue",
                        masterdataPoolUpdate.getPool(), masterdataReference.getKey()));
            }
        }
    }

    @Transactional(readOnly = true)
    public List<MovilizerUploadDataContainer> getAllDataContainers() {
        List<MovilizerUploadDataContainer> out = new ArrayList<>();
        for (DatacontainerFromMovilizerQueue datacontainerFromMovilizerQueue : fromMovilizerQueueService.getAllDatacontainersOrdered()) {
            out.add(datacontainerFromMovilizerQueue.getDatacontainer());
        }
        return out;
    }

    @Transactional
    public void setDataContainersAsProcessed(Collection<String> datacontainerKeys) {
        if (datacontainerKeys != null && !datacontainerKeys.isEmpty()) {
            fromMovilizerQueueService.removeDatacontainersByKeys(datacontainerKeys);
        }
    }

    @Transactional
    public void removeMovelet(String moveletKey, String moveletKeyExtension,
                              Boolean ignoreExtensionKey) {
        MovilizerMoveletDelete delete = new MovilizerMoveletDelete();
        delete.setMoveletKey(moveletKey);
        delete.setMoveletKeyExtension(moveletKeyExtension);
        delete.setIgnoreExtensionKey(ignoreExtensionKey);

        boolean addedToQueue = toMovilizerQueueService.offer(new MoveletToMovilizerQueue(delete));
        if (!addedToQueue) {
            logger.info(String.format("Movelet delete with key %s not added queue",
                    delete.getMoveletKey()));
        }
    }

    @Transactional
    public void resetParticipant(String deviceAddress) {
        MovilizerParticipantReset reset = new MovilizerParticipantReset();
        reset.setDeviceAddress(deviceAddress);

        boolean addedToQueue = toMovilizerQueueService.offer(new ParticipantToMovilizerQueue(reset));
        if (!addedToQueue) {
            logger.info(String.format("Participant reset for device address %s not added to queue",
                    deviceAddress));
        }
    }
}
