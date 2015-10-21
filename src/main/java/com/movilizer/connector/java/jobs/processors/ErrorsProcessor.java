package com.movilizer.connector.java.jobs.processors;

import com.movilitas.movilizer.v12.MovilizerMasterdataError;
import com.movilitas.movilizer.v12.MovilizerMoveletError;
import com.movilitas.movilizer.v12.MovilizerParticipantInstallError;
import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilizer.connector.java.model.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class ErrorsProcessor implements Processor<MovilizerResponse> {

    private static Log logger = LogFactory.getLog(ErrorsProcessor.class);

    public ErrorsProcessor() {
    }

    @Override
    public void process(MovilizerResponse response) {
        //TODO better save JSON in the message
        if (!response.getMoveletError().isEmpty()) {
            logger.error("Last response has movelets errors");
        }
        for (MovilizerMoveletError moveletError : response.getMoveletError()) {
            logger.error(ErrorsProcessor.class.getName() + " " + moveletError.getClass().getName() + " " +
                    String.format(
                            "key: %s, extKey: %s, version: %s, timestamp: %s, errorCode: %s, message: %s",
                            moveletError.getMoveletKey(), moveletError.getMoveletKeyExtension(),
                            moveletError.getMoveletVersion(), String.valueOf(moveletError.getTimestamp()),
                            moveletError.getValidationErrorCode(), moveletError.getMessage()));
        }
        if (!response.getMasterdataError().isEmpty()) {
            logger.error("Last response has masterdata errors");
        }
        for (MovilizerMasterdataError masterdataError : response.getMasterdataError()) {
            logger.error(ErrorsProcessor.class.getName() + " " + masterdataError.getClass().getName() + " " +
                    String.format("pool: %s, group: %s, key: %s, timestamp: %s, errorCode: %s, message: %s",
                            masterdataError.getPool(), masterdataError.getGroup(), masterdataError.getKey(),
                            masterdataError.getTimestamp(), masterdataError.getValidationErrorCode(),
                            masterdataError.getMessage()));
        }
        if (!response.getParticipantInstallError().isEmpty()) {
            logger.error("Last response has participant install errors");
        }
        for (MovilizerParticipantInstallError installError : response.getParticipantInstallError()) {
            logger.error(
                    ErrorsProcessor.class.getName() + " " +
                            installError.getClass().getName() + " " +
                            String.format(
                                    "participant key: %s, device address: %s, user agent: %s, ip: %s, timestamp: %s, errorCode: %s",
                                    installError.getParticipantKey(), installError.getDeviceAddress(),
                                    installError.getUserAgent(), installError.getIpAddress(), installError.getTimestamp(),
                                    installError.getErrorCode()));
        }
        //TODO document error
        if (!response.getDocumentError().isEmpty()) {
            logger.error("Last response has documents errors");
        }
    }
}
