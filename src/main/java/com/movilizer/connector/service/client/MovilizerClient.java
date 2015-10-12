package com.movilizer.connector.service.client;


import com.movilitas.movilizer.v14.*;
import com.movilizer.connector.model.PasswordTypes;
import com.movilizer.connector.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.persistence.entities.MasterdataToMovilizerQueue;
import com.movilizer.connector.persistence.entities.MoveletToMovilizerQueue;
import com.movilizer.connector.persistence.entities.ParticipantToMovilizerQueue;
import com.movilizer.connector.service.JavaSpringConnectorCallback;
import com.movilizer.connector.service.MovilizerGenericClient;
import com.movilizer.connector.service.OXMUtility;
import com.movilizer.connector.service.processors.AckProcessor;
import com.movilizer.connector.service.processors.ErrorsProcessor;
import com.movilizer.connector.service.processors.InboundDataProcessor;
import com.movilizer.connector.service.processors.UpdatesProcessor;
import com.movilizer.connector.service.queues.DCFromQueueService;
import com.movilizer.connector.service.queues.ToMovilizerQueueService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Component
public class MovilizerClient {

    private static Log logger = LogFactory.getLog(MovilizerClient.class);

    MovilizerResponse lastResponse;

    MovilizerRequest currentRequest;

    Calendar lastSync;

    private List<JavaSpringConnectorCallback> callbacks;

    @Value("${movilizer.systemId}")
    private Long systemId;

    @Value("${movilizer.password}")
    private String password;

    @Value("${movilizer.synchronous.response}")
    private boolean synchronousResponse;

    @Autowired
    private MovilizerGenericClient movilizerCloudService;

    @Autowired
    private OXMUtility movilizerXMLParserService;

    @Autowired
    private ToMovilizerQueueService toMovilizerQueueService;

    @Autowired
    private DCFromQueueService fromMovilizerQueueService;

    @Autowired
    private InboundDataProcessor dataProcessor;

    @Autowired
    private ErrorsProcessor errorsProcessor;

    @Autowired
    private AckProcessor ackProcessor;

    @Autowired
    private UpdatesProcessor updatesProcessor;

    public MovilizerClient() {
        callbacks = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        generateCleanRequest();
    }

    //This is performed in other thread than the main execution
    @Scheduled(fixedDelayString = "${movilizer.pollingRate}")
    public void perfomSyncToCloud() {
        logger.info(String.format("Performing cloud sync for systemId %d", systemId));
        if (currentRequest == null)
            generateCleanRequest();
        //Perform task to gather the data to send to the cloud first (movelets, assignment, masterdata)
        updatesProcessor.process(currentRequest);
        lastResponse = movilizerCloudService.getReplyFromCloud(currentRequest);
        lastSync = Calendar.getInstance();
        generateCleanRequest();
        errorsProcessor.process(lastResponse);
        ackProcessor.process(lastResponse);
        dataProcessor.process(lastResponse, currentRequest);

        //Execute callbacks
        for (JavaSpringConnectorCallback callback : callbacks) {
            callback.execute();
        }
    }

    public void registerCallback(JavaSpringConnectorCallback callback) {
        callbacks.add(callback);
    }

    private void generateCleanRequest() {
        currentRequest = movilizerCloudService.getRequest(systemId, password);
        currentRequest.setSynchronousResponse(synchronousResponse);
        if(synchronousResponse == false)
        {
        	currentRequest.setNumResponses(0);
        }
    }

    public Calendar getLastSync() {
        return lastSync;
    }

    //================================================================================================================ EXTERNAL API

    @Transactional
    public void createMovelet(MovilizerMovelet movelet) {
        boolean addedToQueue = toMovilizerQueueService.offer(new MoveletToMovilizerQueue(movelet));
        if (!addedToQueue) {
            logger.info(String.format("Movelet with key %s not added to queue", movelet.getMoveletKey()));
        }
    }

    public MovilizerMovelet unmarshallMoveletFromFile(String filePath) {
        return movilizerXMLParserService.getMoveletFromFile(filePath);
    }

    @Transactional
    public void assignMoveletToParticipant(String moveletKey, MovilizerParticipant participant) {
        MovilizerMoveletAssignment assignment = new MovilizerMoveletAssignment();
        assignment.setMoveletKey(moveletKey);
        assignment.getParticipant().add(participant);
        boolean addedToQueue = toMovilizerQueueService.offer(new ParticipantToMovilizerQueue(assignment));
        if (!addedToQueue) {
            logger.info(String.format("Assigment for participant %s to movelet %s not added to queue",
                    participant.getDeviceAddress(), moveletKey));
        }
    }

    @Transactional
    public void assignPasswordToParticipant(MovilizerParticipant participant, PasswordTypes passwordType, String password) {
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
    public void assignConfigurationToParticipant(Collection<MovilizerParticipantConfiguration> collection) {
    	for(MovilizerParticipantConfiguration configuration : collection)
    	{
    		assignConfigurationToParticipant(configuration);
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
    public void updateMasterdata(List<MovilizerMasterdataPoolUpdate> masterdataList) {
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
        for (DatacontainerFromMovilizerQueue datacontainerFromMovilizerQueue : fromMovilizerQueueService.getAllOrdered()) {
            out.add(datacontainerFromMovilizerQueue.getDatacontainer());
        }
        return out;
    }

    @Transactional
    public void setDataContainersAsProcessed(
            Collection<MovilizerUploadDataContainer> dataContainerList) {
        if (dataContainerList != null && dataContainerList.isEmpty() == false) {
            fromMovilizerQueueService.remove(dataContainerList);
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

    // ---- getters

    public Long getSystemId() {
        return systemId;
    }

    public String getPassword() {
        return password;
    }
}
