package com.movilizer.connector.java.jobs.processors;


import com.movilitas.movilizer.v12.*;
import com.movilizer.connector.java.jobs.processors.download.DatacontainerProcessor;
import com.movilizer.connector.java.model.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DownloadProcessor {

    private static Log logger = LogFactory.getLog(DownloadProcessor.class);

    @Autowired
    @Qualifier("datacontainerProcessor")
    private Processor<MovilizerResponse> datacontainerProcessor;
    
    private List<Processor<MovilizerResponse>> responseProcesssors= new ArrayList<>();
    private List<Processor<MovilizerMasterdataAck>> masterdataAckProcesssors= new ArrayList<>();
    private List<Processor<MovilizerMasterdataError>> masterdataErrorProcesssors= new ArrayList<>();
    private List<Processor<MovilizerMasterdataDeleted>> masterdataDeletedProcesssors= new ArrayList<>();
    private List<Processor<MovilizerDocumentAck>> documentAckProcesssors= new ArrayList<>();
    private List<Processor<MovilizerDocumentError>> documentErrorProcesssors= new ArrayList<>();
    private List<Processor<MovilizerDocumentDeleted>> documentDeletedProcesssors= new ArrayList<>();
    private List<Processor<MovilizerMoveletAck>> moveletAckProcesssors= new ArrayList<>();
    private List<Processor<MovilizerMoveletError>> moveletErrorProcesssors= new ArrayList<>();
    private List<Processor<MovilizerMoveletDeleted>> moveletDeletedProcesssors= new ArrayList<>();
    private List<Processor<MovilizerMoveletAssignmentDeleted>> moveletAssignmentDeletedProcesssors= new ArrayList<>();
    private List<Processor<MovilizerMoveletSynced>> moveletSyncedProcesssors= new ArrayList<>();
    private List<Processor<MovilizerMetaMoveletReply>> metaMoveletReplyProcesssors= new ArrayList<>();
    private List<Processor<MovilizerReplyMovelet>> replyMoveletProcesssors= new ArrayList<>();
    private List<Processor<MovilizerUploadDataContainer>> uploadContainerProcesssors= new ArrayList<>();
    private List<Processor<MovilizerParticipantAck>> participantAckProcesssorss= new ArrayList<>();
    private List<Processor<MovilizerParticipantDeploymentSmsSent>> participantDeploymentSmsSentProcesssors= new ArrayList<>();
    private List<Processor<MovilizerParticipantInstall>> participantInstallProcesssors= new ArrayList<>();
    private List<Processor<MovilizerParticipantInstallAck>> participantInstallAckProcesssors= new ArrayList<>();
    private List<Processor<MovilizerParticipantInstallError>> participantInstallErrorProcesssors= new ArrayList<>();
    private List<Processor<MovilizerParticipantDeleted>> participantDeletedProcesssors= new ArrayList<>();
    private List<Processor<MovilizerStatusMessage>> statusMessageProcesssors= new ArrayList<>();

    public DownloadProcessor() {}

    public void process(MovilizerResponse response) {
        datacontainerProcessor.process(response);

        for (Processor<MovilizerResponse> responseProcesssor : responseProcesssors) {
            responseProcesssor.process(response);
        }

        if (!response.getMasterdataAck().isEmpty()) {
            for (Processor<MovilizerMasterdataAck> processor : masterdataAckProcesssors) {
                for (MovilizerMasterdataAck item : response.getMasterdataAck()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getMasterdataError().isEmpty()) {
            for (Processor<MovilizerMasterdataError> processor : masterdataErrorProcesssors) {
                for (MovilizerMasterdataError item : response.getMasterdataError()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getMasterdataDeleted().isEmpty()) {
            for (Processor<MovilizerMasterdataDeleted> processor : masterdataDeletedProcesssors) {
                for (MovilizerMasterdataDeleted item : response.getMasterdataDeleted()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getDocumentAck().isEmpty()) {
            for (Processor<MovilizerDocumentAck> processor : documentAckProcesssors) {
                for (MovilizerDocumentAck item : response.getDocumentAck()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getDocumentError().isEmpty()) {
            for (Processor<MovilizerDocumentError> processor : documentErrorProcesssors) {
                for (MovilizerDocumentError item : response.getDocumentError()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getDocumentDeleted().isEmpty()) {
            for (Processor<MovilizerDocumentDeleted> processor : documentDeletedProcesssors) {
                for (MovilizerDocumentDeleted item : response.getDocumentDeleted()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getMoveletAck().isEmpty()) {
            for (Processor<MovilizerMoveletAck> processor : moveletAckProcesssors) {
                for (MovilizerMoveletAck item : response.getMoveletAck()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getMoveletError().isEmpty()) {
            for (Processor<MovilizerMoveletError> processor : moveletErrorProcesssors) {
                for (MovilizerMoveletError item : response.getMoveletError()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getMoveletDeleted().isEmpty()) {
            for (Processor<MovilizerMoveletDeleted> processor : moveletDeletedProcesssors) {
                for (MovilizerMoveletDeleted item : response.getMoveletDeleted()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getMoveletAssignmentDeleted().isEmpty()) {
            for (Processor<MovilizerMoveletAssignmentDeleted> processor : moveletAssignmentDeletedProcesssors) {
                for (MovilizerMoveletAssignmentDeleted item : response.getMoveletAssignmentDeleted()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getMoveletSynced().isEmpty()) {
            for (Processor<MovilizerMoveletSynced> processor : moveletSyncedProcesssors) {
                for (MovilizerMoveletSynced item : response.getMoveletSynced()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getMetaMoveletReply().isEmpty()) {
            for (Processor<MovilizerMetaMoveletReply> processor : metaMoveletReplyProcesssors) {
                for (MovilizerMetaMoveletReply item : response.getMetaMoveletReply()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getReplyMovelet().isEmpty()) {
            for (Processor<MovilizerReplyMovelet> processor : replyMoveletProcesssors) {
                for (MovilizerReplyMovelet item : response.getReplyMovelet()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getUploadContainer().isEmpty()) {
            for (Processor<MovilizerUploadDataContainer> processor : uploadContainerProcesssors) {
                for (MovilizerUploadDataContainer item : response.getUploadContainer()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getParticipantAck().isEmpty()) {
            for (Processor<MovilizerParticipantAck> processor : participantAckProcesssorss) {
                for (MovilizerParticipantAck item : response.getParticipantAck()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getParticipantDeploymentSmsSent().isEmpty()) {
            for (Processor<MovilizerParticipantDeploymentSmsSent> processor : participantDeploymentSmsSentProcesssors) {
                for (MovilizerParticipantDeploymentSmsSent item : response.getParticipantDeploymentSmsSent()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getParticipantInstall().isEmpty()) {
            for (Processor<MovilizerParticipantInstall> processor : participantInstallProcesssors) {
                for (MovilizerParticipantInstall item : response.getParticipantInstall()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getParticipantInstallAck().isEmpty()) {
            for (Processor<MovilizerParticipantInstallAck> processor : participantInstallAckProcesssors) {
                for (MovilizerParticipantInstallAck item : response.getParticipantInstallAck()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getParticipantInstallError().isEmpty()) {
            for (Processor<MovilizerParticipantInstallError> processor : participantInstallErrorProcesssors) {
                for (MovilizerParticipantInstallError item : response.getParticipantInstallError()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getParticipantDeleted().isEmpty()) {
            for (Processor<MovilizerParticipantDeleted> processor : participantDeletedProcesssors) {
                for (MovilizerParticipantDeleted item : response.getParticipantDeleted()) {
                    processor.process(item);
                }
            }
        }
        if (!response.getStatusMessage().isEmpty()) {
            for (Processor<MovilizerStatusMessage> processor : statusMessageProcesssors) {
                for (MovilizerStatusMessage item : response.getStatusMessage()) {
                    processor.process(item);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void registerProcessor(Processor<T> processor, Class<T> processorClass) {
        if (MovilizerResponse.class.equals(processorClass)) {
            responseProcesssors.add((Processor<MovilizerResponse>) processor);
        } else if (MovilizerMasterdataAck.class.equals(processorClass)) {
            masterdataAckProcesssors.add((Processor<MovilizerMasterdataAck>) processor);
        } else if (MovilizerMasterdataError.class.equals(processorClass)) {
            masterdataErrorProcesssors.add((Processor<MovilizerMasterdataError>) processor);
        } else if (MovilizerMasterdataDeleted.class.equals(processorClass)) {
            masterdataDeletedProcesssors.add((Processor<MovilizerMasterdataDeleted>) processor);
        } else if (MovilizerDocumentAck.class.equals(processorClass)) {
            documentAckProcesssors.add((Processor<MovilizerDocumentAck>) processor);
        } else if (MovilizerDocumentError.class.equals(processorClass)) {
            documentErrorProcesssors.add((Processor<MovilizerDocumentError>) processor);
        } else if (MovilizerDocumentDeleted.class.equals(processorClass)) {
            documentDeletedProcesssors.add((Processor<MovilizerDocumentDeleted>) processor);
        } else if (MovilizerMoveletAck.class.equals(processorClass)) {
            moveletAckProcesssors.add((Processor<MovilizerMoveletAck>) processor);
        } else if (MovilizerMoveletError.class.equals(processorClass)) {
            moveletErrorProcesssors.add((Processor<MovilizerMoveletError>) processor);
        } else if (MovilizerMoveletDeleted.class.equals(processorClass)) {
            moveletDeletedProcesssors.add((Processor<MovilizerMoveletDeleted>) processor);
        } else if (MovilizerMoveletAssignmentDeleted.class.equals(processorClass)) {
            moveletAssignmentDeletedProcesssors.add((Processor<MovilizerMoveletAssignmentDeleted>) processor);
        } else if (MovilizerMoveletSynced.class.equals(processorClass)) {
            moveletSyncedProcesssors.add((Processor<MovilizerMoveletSynced>) processor);
        } else if (MovilizerMetaMoveletReply.class.equals(processorClass)) {
            metaMoveletReplyProcesssors.add((Processor<MovilizerMetaMoveletReply>) processor);
        } else if (MovilizerReplyMovelet.class.equals(processorClass)) {
            replyMoveletProcesssors.add((Processor<MovilizerReplyMovelet>) processor);
        } else if (MovilizerUploadDataContainer.class.equals(processorClass)) {
            uploadContainerProcesssors.add((Processor<MovilizerUploadDataContainer>) processor);
        } else if (MovilizerParticipantAck.class.equals(processorClass)) {
            participantAckProcesssorss.add((Processor<MovilizerParticipantAck>) processor);
        } else if (MovilizerParticipantDeploymentSmsSent.class.equals(processorClass)) {
            participantDeploymentSmsSentProcesssors.add((Processor<MovilizerParticipantDeploymentSmsSent>) processor);
        } else if (MovilizerParticipantInstall.class.equals(processorClass)) {
            participantInstallProcesssors.add((Processor<MovilizerParticipantInstall>) processor);
        } else if (MovilizerParticipantInstallAck.class.equals(processorClass)) {
            participantInstallAckProcesssors.add((Processor<MovilizerParticipantInstallAck>) processor);
        } else if (MovilizerParticipantInstallError.class.equals(processorClass)) {
            participantInstallErrorProcesssors.add((Processor<MovilizerParticipantInstallError>) processor);
        } else if (MovilizerParticipantDeleted.class.equals(processorClass)) {
            participantDeletedProcesssors.add((Processor<MovilizerParticipantDeleted>) processor);
        } else if (MovilizerStatusMessage.class.equals(processorClass)) {
            statusMessageProcesssors.add((Processor<MovilizerStatusMessage>) processor);
        } else {
            logger.warn("Ignoring unknown processor for type: " + processorClass.getSimpleName());
        }
    }
}
