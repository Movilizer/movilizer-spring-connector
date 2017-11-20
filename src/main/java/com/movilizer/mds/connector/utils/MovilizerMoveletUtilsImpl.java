package com.movilizer.mds.connector.utils;

import com.movilitas.movilizer.v15.*;
import com.movilizer.mds.connector.model.MovilizerRequestSink;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovilizerMoveletUtilsImpl implements MovilizerMoveletUtils {
    private static Log logger = LogFactory.getLog(MovilizerMoveletUtilsImpl.class);

    private final MovilizerCoreUtils utils;

    @Autowired
    public MovilizerMoveletUtilsImpl(MovilizerCoreUtils utils) {
        this.utils = utils;
    }

    @Override
    public void createMovelet(MovilizerMovelet movelet, MovilizerRequestSink sink) {
        MovilizerMoveletSet set = new MovilizerMoveletSet();
        set.getMovelet().add(movelet);
        MovilizerRequest request = utils.createUploadRequest();
        request.getMoveletSet().add(set);
        sink.sendRequest(request);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Movelet with key %s added to queue",
                    movelet.getMoveletKey()));
        }
    }

    @Override
    public void assignMoveletToParticipant(String moveletKey, MovilizerParticipant participant,
                                           MovilizerRequestSink sink) {
        assignMoveletToParticipant(moveletKey, "", participant, sink);
    }

    @Override
    public void assignMoveletToParticipant(String moveletKey, String moveletKeyExtension,
                                           MovilizerParticipant participant,
                                           MovilizerRequestSink sink) {
        MovilizerMoveletAssignment assignment = new MovilizerMoveletAssignment();
        assignment.setMoveletKey(moveletKey);
        assignment.setMoveletKeyExtension(moveletKeyExtension);
        assignment.getParticipant().add(participant);
        MovilizerRequest request = utils.createUploadRequest();
        request.getMoveletAssignment().add(assignment);
        sink.sendRequest(request);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Assigment for participant %s to movelet %s added to queue",
                    participant.getDeviceAddress(), moveletKey));
        }
    }

    @Override
    public void removeMovelet(String moveletKey, String moveletKeyExtension,
                              Boolean ignoreExtensionKey, MovilizerRequestSink sink) {
        MovilizerMoveletDelete delete = new MovilizerMoveletDelete();
        delete.setMoveletKey(moveletKey);
        delete.setMoveletKeyExtension(moveletKeyExtension);
        delete.setIgnoreExtensionKey(ignoreExtensionKey);
        MovilizerRequest request = utils.createUploadRequest();
        request.getMoveletDelete().add(delete);
        sink.sendRequest(request);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Movelet delete with key %s added queue",
                    delete.getMoveletKey()));
        }
    }
}
